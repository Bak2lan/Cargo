package aist.cargo.service.impl;
import aist.cargo.dto.user.*;
import aist.cargo.entity.Sending;
import aist.cargo.entity.User;
import aist.cargo.enums.Status;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.SendingRepository;
import aist.cargo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import aist.cargo.service.SendingService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SendingServiceImpl implements SendingService {

    private final UserRepository userRepository;
    private final SendingRepository sendingRepository;
    private final UserServiceImpl userServiceImpl;
    private static final Logger log = LoggerFactory.getLogger(Sending.class);

    public SendingServiceImpl(UserRepository userRepository, SendingRepository sendingRepository, UserServiceImpl userServiceImpl) {
        this.userRepository = userRepository;
        this.sendingRepository = sendingRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public SimpleResponseCreate createSending(SendingRequest sendingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.hasActiveSubscriptionForPackage(sendingRequest.getPackageType())) {
            return new SimpleResponseCreate("User does not have an active subscription for this package type.", false);
        }

        Sending sending = new Sending();
        sending.setUser(user);
        sending.setPackageType(sendingRequest.getPackageType());
        sending.setSize(sendingRequest.getSize());
        sending.setDescription(sendingRequest.getDescription());
        sending.setFromWhere(sendingRequest.getFromWhere());
        sending.setToWhere(sendingRequest.getToWhere());
        sending.setDispatchDate(sendingRequest.getDispatchDate());
        sending.setArrivalDate(sendingRequest.getArrivalDate());

        sendingRepository.save(sending);
        return new SimpleResponseCreate("Sending created successfully.", true);
    }


    @Override
    public List<SendingResponse> getAllSendings() {
        List<Sending> allSendings = sendingRepository.findAll();
        return allSendings.stream()
                .map(sending -> SendingResponse.builder()
                        .id(sending.getId())
                        .fullName(sending.getUser().getFirstName() + " " + sending.getUser().getLastName())
                        .phoneNumber(sending.getUser().getPhoneNumber())
                        .description(sending.getDescription())
                        .fromWhere(sending.getFromWhere())
                        .toWhere(sending.getToWhere())
                        .dispatchDate(sending.getDispatchDate())
                        .arrivalDate(sending.getArrivalDate())
                        .packageType(sending.getPackageType())
                        .size(sending.getSize())
                        .build())
                .toList();
    }

    @Override
    public SendingResponseGetId getSendingById(Long sendingId) {
        Sending sending = sendingRepository.findById(sendingId)
                .orElseThrow(() -> new RuntimeException("Отправка не найдена"));
        User user = sending.getUser();
        if (user == null) {
            throw new NotFoundException("User for sending with ID " + sendingId + " not found");
        }
        String fullName = user.getFirstName() + " " + user.getLastName();

        return SendingResponseGetId.builder()
                .id(sending.getId())
                .fullName(sending.getUser().getFirstName() + " " + sending.getUser().getLastName())
                .phoneNumber(sending.getUser().getPhoneNumber())
                .description(sending.getDescription())
                .fromWhere(sending.getFromWhere())
                .toWhere(sending.getToWhere())
                .dispatchDate(sending.getDispatchDate())
                .arrivalDate(sending.getArrivalDate())
                .imageUser(user.getUserImage())
                .size(sending.getSize())
                .fullName(fullName)
                .build();
    }

    @Override
    public void updateSending(Long sendingId, SendingRequest sendingRequest) {
        Sending sending = sendingRepository.findById(sendingId)
                .orElseThrow(() -> new RuntimeException("Отправка не найдена"));
        sending.setPackageType(sendingRequest.getPackageType());
        sending.setSize(sendingRequest.getSize());
        sending.setDescription(sendingRequest.getDescription());
        sending.setFromWhere(sendingRequest.getFromWhere());
        sending.setToWhere(sendingRequest.getToWhere());
        sending.setDispatchDate(sendingRequest.getDispatchDate());
        sending.setArrivalDate(sendingRequest.getArrivalDate());

        sendingRepository.save(sending);
    }

    @Override
    public void deleteSending(Long sendingId) {
        Sending sending = sendingRepository.findById(sendingId)
                .orElseThrow(() -> new RuntimeException("Отправка не найдена"));
        sendingRepository.delete(sending);
    }

    @Override
    public ResponseEntity<String> archiveSending(Long senderId) {
        User user = userServiceImpl.getAuthenticatedUser();

        Sending sending = sendingRepository.findById(senderId)
                .filter(s -> s.getUser().getId().equals(user.getId()))  // Проверка принадлежности отправления пользователю
                .orElseThrow(() -> new RuntimeException("Sending not found or does not belong to the user"));

        if (sending.getStatus() == Status.ARCHIVED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sending is already archived.");
        }
        sending.setStatus(Status.ARCHIVED);
        sendingRepository.save(sending);

        return ResponseEntity.ok("Sending archived successfully.");
    }

    @Override
    public ResponseEntity<String> activateSending(Long senderId) {
        User user = userServiceImpl.getAuthenticatedUser();

        Sending sending = sendingRepository.findById(senderId)
                .filter(s -> s.getUser().getId().equals(user.getId()))  // Проверка принадлежности отправления пользователю
                .orElseThrow(() -> new RuntimeException("Sending not found or does not belong to the user"));

        if (sending.getStatus() == Status.ACTIVE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sending is already active.");
        }
        sending.setStatus(Status.ACTIVE);
        sendingRepository.save(sending);

        return ResponseEntity.ok("Sending activated successfully.");
    }

    @Override
    public List<SendingResponse> getAllArchived() {
        User user = userServiceImpl.getAuthenticatedUser();

        return user.getSendings().stream()
                .filter(sending -> sending.getStatus() == Status.ARCHIVED)
                .map(sending -> SendingResponse.builder()
                        .id(sending.getId())
                        .fullName(sending.getUser().getFullName())
                        .phoneNumber(sending.getUser().getPhoneNumber())
                        .description(sending.getDescription())
                        .fromWhere(sending.getFromWhere())
                        .toWhere(sending.getToWhere())
                        .dispatchDate(sending.getDispatchDate())
                        .arrivalDate(sending.getArrivalDate())
                        .packageType(sending.getPackageType())
                        .size(sending.getSize())
                        .status(sending.getStatus())
                        .build())
                .toList();
    }

    @Override
    public List<SendingResponse> getAllSendingsUser() {
        User authenticatedUser = userServiceImpl.getAuthenticatedUser();

        List<Sending> sendings = sendingRepository.findByUserId(authenticatedUser.getId());

        return sendings.stream()
                .map(sending -> SendingResponse.builder()
                        .id(sending.getId())
                        .fullName(sending.getUser().getFullName())
                        .phoneNumber(sending.getUser().getPhoneNumber())
                        .description(sending.getDescription())
                        .fromWhere(sending.getFromWhere())
                        .toWhere(sending.getToWhere())
                        .dispatchDate(sending.getDispatchDate())
                        .arrivalDate(sending.getArrivalDate())
                        .packageType(sending.getPackageType())
                        .size(sending.getSize())
                        .status(sending.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SimpleResponseCreate createTrueSending(SendingCreateRequest sendingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new SimpleResponseCreate("Unauthorized", false); // Түзөтүлгөн бул жерде "Unauthorized" деп кайтарабыз
        }

        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Колдонуучу табылган жок email: " + userEmail));

        if (!userRepository.existsSubscriptionsByUserEmail(userEmail)) {
            return new SimpleResponseCreate("Колдонуучу үчүн жазылуу табылган жок: " + userEmail, false);
        }

        String fromWhere = sendingRequest.getFromWhere();
        String toWhere = sendingRequest.getToWhere();

        if (isAddressValid(fromWhere)) {
            return new SimpleResponseCreate("Адрес отправления не найден или недействителен: " + fromWhere, false);
        }
        if (isAddressValid(toWhere)) {
            return new SimpleResponseCreate("Адрес назначения не найден или недействителен: " + toWhere, false);
        }
        return new SimpleResponseCreate("Колдонуучу жазылуу менен камсыздалган.", true);
    }

    public boolean isAddressValid(String address) {
        if (address == null || address.trim().isEmpty()) {
            return true;
        }

        String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&addressdetails=1";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "CargoApp/1.0 (zoomaisenbaev269@gmail.com)");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return true;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(response.getBody());
            return node.isEmpty();
        } catch (Exception e) {
            log.error("Error validating address: {}", e.getMessage());
            return true;
        }
    }

    public SimpleResponseCreateSending createSending(SendingCreateUpdateRequest sendingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return SimpleResponseCreateSending.builder()
                    .message("Unauthorized")
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0)
                    .build();
        }

        User user = userRepository.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + authentication.getName()));

        if (!userRepository.existsSubscriptionsByUserEmail(authentication.getName())) {
            return SimpleResponseCreateSending.builder()
                    .message("No subscription found for the user with email: " + authentication.getName())
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0)
                    .build();
        }

        String fromWhere = sendingRequest.getFromWhere();
        String toWhere = sendingRequest.getToWhere();
        log.info("Start checking addresses...");

        boolean fromWhereExists = sendingRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
        boolean toWhereExists = sendingRepository.getSenderByToWhere(toWhere).contains(toWhere);

        if (!fromWhereExists && isAddressValid(fromWhere)) {
            log.info("Sender address invalid: {}", fromWhere);
            return SimpleResponseCreateSending.builder()
                    .message("Адрес отправления не найден: " + fromWhere)
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0)
                    .build();
        }

        if (!toWhereExists && isAddressValid(toWhere)) {
            log.info("Receiver address invalid: {}", toWhere);
            return SimpleResponseCreateSending.builder()
                    .message("Адрес назначения не найден: " + toWhere)
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0)
                    .build();
        }

        Sending sending = new Sending();
        sending.setFromWhere(fromWhere);
        sending.setToWhere(toWhere);
        sending.setDispatchDate(sendingRequest.getDispatchDate());
        sending.setArrivalDate(sendingRequest.getArrivalDate());
        sending.setDescription(sendingRequest.getDescription());
        sending.setUserName(sendingRequest.getUserName());
        sending.setSize(sendingRequest.getSize());
        sending.setUser(user);

        sendingRepository.save(sending);

        int randomCode = 100 + new Random().nextInt(900);

        log.info("Sending successfully created for user: {}", authentication.getName());

        return SimpleResponseCreateSending.builder()
                .message("Sending created successfully for user: " + authentication.getName())
                .success(true)
                .userId(user.getId())
                .id(sending.getId())
                .random(randomCode)
                .build();
    }
    @Transactional
    @Override
    public SimpleResponseCreate updateSending(SendingUpdateRequest sendingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new SimpleResponseCreate("Unauthorized", false);
        }

        String userEmail = authentication.getName();
        User user = userRepository.getUserByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("User not found with email: " + userEmail)
        );

        if (!userRepository.existsSubscriptionsByUserEmail(userEmail)) {
            return new SimpleResponseCreate("No subscription found for the user with email: " + userEmail, false);
        }

        Sending sending = sendingRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Sending not found for user: " + userEmail));

        String fromWhere = sendingRequest.getFromWhere();
        String toWhere = sendingRequest.getToWhere();
        log.info("Start checking addresses...");

        boolean fromWhereExists = sendingRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
        boolean toWhereExists = sendingRepository.getSenderByToWhere(toWhere).contains(toWhere);

        if (!fromWhereExists && isAddressValid(fromWhere)) {
            return new SimpleResponseCreate("Address of sending is not found: " + fromWhere, false);
        }
        if (!toWhereExists && isAddressValid(toWhere)) {
            return new SimpleResponseCreate("Address of destination is not found: " + toWhere, false);
        }

        sending.setFromWhere(fromWhere);
        sending.setToWhere(toWhere);
        sending.setDispatchDate(sendingRequest.getDispatchDate());
        sending.setArrivalDate(sendingRequest.getArrivalDate());
        sending.setDescription(sendingRequest.getDescription());
        sending.setUserName(sendingRequest.getUserName());
        sending.setUser(user);

        sendingRepository.save(sending);

        return new SimpleResponseCreate("Sending updated successfully for user: " + userEmail, true);
    }

}
