package aist.cargo.service.impl;

import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.dto.user.SimpleResponseCreate;
import aist.cargo.entity.Sending;
import aist.cargo.entity.User;
import aist.cargo.enums.Status;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.SendingRepository;
import aist.cargo.repository.UserRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import aist.cargo.service.SendingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
    public SendingResponse getSendingById(Long sendingId) {
        Sending sending = sendingRepository.findById(sendingId)
                .orElseThrow(() -> new RuntimeException("Отправка не найдена"));

        return SendingResponse.builder()
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
    public SimpleResponseCreate createSending(SendingRequest sendingRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        if (!userRepository.existsSubscriptionsByUserEmail(userEmail)) {
            return new SimpleResponseCreate("No subscription found for the user with email: " + userEmail,false);
        }

        String fromWhere = sendingRequest.getFromWhere();
        String toWhere = sendingRequest.getToWhere();

        if (isAddressValid(fromWhere)) {
            throw new NotFoundException("Адрес отправления не найден или недействителен: " + fromWhere);
        }
        if (isAddressValid(toWhere)) {
            throw new NotFoundException("Адрес назначения не найден или недействителен: " + toWhere);
        }

        Sending sending = mapToSending(sendingRequest, user);
        user.getSendings().add(sending);
        sendingRepository.save(sending);
        userRepository.save(user);

        return new SimpleResponseCreate("Sending created successfully. ID: " + sending.getId(),true);
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

    public Sending mapToSending(SendingRequest sendingRequest, User user) {
        Sending sending = new Sending();
        sending.setFromWhere(sendingRequest.getFromWhere());
        sending.setToWhere(sendingRequest.getToWhere());
        sending.setDispatchDate(sendingRequest.getDispatchDate());
        sending.setArrivalDate(sendingRequest.getArrivalDate());
        sending.setDescription(sendingRequest.getDescription());
        sending.setPackageType(sendingRequest.getPackageType());
        sending.setSize(sendingRequest.getSize());
        sending.setUser(user);
        return sending;
    }
}

