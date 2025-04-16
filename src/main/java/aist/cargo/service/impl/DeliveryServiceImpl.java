package aist.cargo.service.impl;
import aist.cargo.dto.user.*;
import aist.cargo.entity.Delivery;
import aist.cargo.entity.User;
import aist.cargo.enums.Status;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.DeliveryRepository;
import aist.cargo.repository.UserRepository;
import aist.cargo.repository.jdbcTemplate.DeliveryJDBCTemplate;
import aist.cargo.service.DeliveryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryJDBCTemplate deliveryJDBCTemplate;
    private final UserServiceImpl userServiceImpl;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    public CargoDeliveryResponse getDeliveryById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Delivery delivery = deliveryRepository.findByUserEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("Delivery not found for user: " + currentUserEmail));

        User user = delivery.getUser();
        if (user == null) {
            throw new NotFoundException("User for delivery with ID " + delivery.getId() + " not found");
        }

        String fullName = user.getFirstName() + " " + user.getLastName();

        return new CargoDeliveryResponse(
                delivery.getId(),
                user.getId(),
                user.getUserImage(),
                fullName,
                delivery.getTransportNumber(),
                delivery.getDescription(),
                delivery.getFromWhere(),
                delivery.getDispatchDate(),
                delivery.getToWhere(),
                delivery.getArrivalDate(),
                delivery.getSize(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }


    @Override
    public List<CargoResponseGetAll> getAllCargo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        List<Delivery> allDeliveries = deliveryRepository.findAll();

        return allDeliveries.stream()
                .map(delivery -> {
                    User user = delivery.getUser();
                    String fullName = (user != null ?
                            (user.getFirstName() != null ? user.getFirstName() : "") + " " +
                                    (user.getLastName() != null ? user.getLastName() : "")
                            : "No User");

                    boolean isOwner = user != null && user.getEmail().equals(currentUserEmail);
                    if (delivery.getRandom() == null) {
                        throw new NotFoundException("Random number must not be null.");
                    }

                    if (!deliveryRepository.existsByRandom(delivery.getRandom())) {
                        throw new NotFoundException("Random number already exists in the database!");
                    }

                    return CargoResponseGetAll.builder()
                            .id(delivery.getId())
                            .fromWhere(delivery.getFromWhere())
                            .toWhere(delivery.getToWhere())
                            .size(delivery.getSize())
                            .fullName(fullName)
                            .arrivalDate(delivery.getArrivalDate())
                            .transportNumber(delivery.getTransportNumber())
                            .dispatchDate(delivery.getDispatchDate())
                            .random(delivery.getRandom())
                            .isOwner(isOwner)
                            .build();
                })
                .toList();
    }


    public SimpleResponseCreate TrueDelivery(DeliveryForRequest deliveryRequest, String userEmail) {
        User user = userRepository.getUserByEmail(userEmail).orElseThrow(() -> new NotFoundException("User not found email " + userEmail));

        if (!userRepository.existsSubscriptionsByUserEmail(userEmail)) {
            return new SimpleResponseCreate("No subscription found for the user with email: " + userEmail, false);
        }

        String fromWhere = deliveryRequest.getFromWhere();
        String toWhere = deliveryRequest.getToWhere();
        log.info("Checking addresses...");

        boolean fromWhereExists = deliveryRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
        boolean toWhereExists = deliveryRepository.getSenderByToWhere(toWhere).contains(toWhere);

        if (!fromWhereExists && isAddressValid(fromWhere)) {
            log.info("Sender address invalid");
            return new SimpleResponseCreate("Адрес отправления не найден: " + fromWhere, false);
        }

        if (!toWhereExists && isAddressValid(toWhere)) {
            log.info("Receiver address invalid");
            return new SimpleResponseCreate("Адрес назначения не найден: " + toWhere, false);
        }

        log.info("All checks passed");

        return new SimpleResponseCreate("Delivery details are valid.", true);
    }

    public boolean isAddressValid(String address) {

        try {
            log.info("test8");
            String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&addressdetails=1";
            log.info("test9");
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("test10");

            if (!response.getStatusCode().is2xxSuccessful()) {
                return true;
            }
            log.info("test11");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(response.getBody());
            return node.isEmpty();
        } catch (Exception e) {
            log.error("Error validating address: {}", e.getMessage());
            return true;
        }
    }

    @Override
    public SimpleResponseCreateDelivery createDelivery(DeliveryUpdateRequest deliveryRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return SimpleResponseCreateDelivery.builder()
                    .message("Unauthorized")
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0L)
                    .build();
        }

        String userEmail = authentication.getName();
        User user = userRepository.getUserByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("User not found with email: " + userEmail)
        );

        if (!userRepository.existsSubscriptionsByUserEmail(userEmail)) {
            return SimpleResponseCreateDelivery.builder()
                    .message("No subscription found for the user with email: " + userEmail)
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0L)
                    .build();
        }

        String fromWhere = deliveryRequest.getFromWhere();
        String toWhere = deliveryRequest.getToWhere();
        log.info("Start checking addresses...");

        boolean fromWhereExists = deliveryRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
        boolean toWhereExists = deliveryRepository.getSenderByToWhere(toWhere).contains(toWhere);

        if (!fromWhereExists && isAddressValid(fromWhere)) {
            return SimpleResponseCreateDelivery.builder()
                    .message("Адрес отправления не найден: " + fromWhere)
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0L)
                    .build();
        }

        if (!toWhereExists && isAddressValid(toWhere)) {
            return SimpleResponseCreateDelivery.builder()
                    .message("Адрес назначения не найден: " + toWhere)
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0L)
                    .build();
        }

        if (deliveryRepository.existsByTransportNumber(deliveryRequest.getTransportNumber())) {
            return SimpleResponseCreateDelivery.builder()
                    .message("Transport number already exists: " + deliveryRequest.getTransportNumber())
                    .success(false)
                    .userId(null)
                    .id(null)
                    .random(0L)
                    .build();
        }
        Long randomCode = 100 + new Random().nextLong(900);

        Delivery delivery = new Delivery();
        delivery.setFromWhere(fromWhere);
        delivery.setToWhere(toWhere);
        delivery.setDispatchDate(deliveryRequest.getDispatchDate());
        delivery.setArrivalDate(deliveryRequest.getArrivalDate());
        delivery.setDescription(deliveryRequest.getDescription());
        delivery.setUserName(deliveryRequest.getUserName());
        delivery.setTransportNumber(deliveryRequest.getTransportNumber());
        delivery.setSize(deliveryRequest.getSize());
        delivery.setRandom(randomCode);
        delivery.setUser(user);

        deliveryRepository.save(delivery);



        log.info("Delivery successfully created for user: {}", userEmail);

        return SimpleResponseCreateDelivery.builder()
                .message("Delivery created successfully for user: " + userEmail)
                .success(true)
                .userId(user.getId())
                .id(delivery.getId())
                .random(randomCode)
                .build();
    }


    @Override
    public List<CargoResponse> getAllArchivedDeliveries() {
        User user = userServiceImpl.getAuthenticatedUser();

        return user.getDeliveries().stream()
                .filter(delivery -> delivery.getStatus() == Status.ARCHIVED)
                .map(delivery -> CargoResponse.builder()
                        .id(delivery.getId())
                        .fullName(delivery.getUser().getFullName())
                        .phoneNumber(delivery.getUser().getPhoneNumber())
                        .description(delivery.getDescription())
                        .fromWhere(delivery.getFromWhere())
                        .toWhere(delivery.getToWhere())
                        .dispatchDate(delivery.getDispatchDate())
                        .arrivalDate(delivery.getArrivalDate())
                        .packageType(delivery.getPackageType())
                        .size(delivery.getSize())
                        .build())
                .toList();
    }

    @Override
    public ResponseEntity<String> archiveDelivery(Long deliveryId) {
        User user = userServiceImpl.getAuthenticatedUser();

        if (!deliveryJDBCTemplate.isDeliveryOwnedByUser(deliveryId, user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You do not have permission to archive this delivery.");
        }
        String currentStatus = deliveryJDBCTemplate.getDeliveryStatus(deliveryId);

        if ("ARCHIVED".equals(currentStatus)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The delivery is already archived!");
        }

        deliveryJDBCTemplate.updateDeliveryStatus(deliveryId, "ARCHIVED");

        return ResponseEntity.ok("Delivery successfully archived.");
    }


    @Override
    public ResponseEntity<String> activateDelivery(Long deliveryId) {
        User user = userServiceImpl.getAuthenticatedUser();

        if (deliveryJDBCTemplate.isDeliveryOwnedByUser(deliveryId, user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to activate this delivery.");
        }
        String currentStatus = deliveryJDBCTemplate.getDeliveryStatus(deliveryId);

        if ("ACTIVE".equals(currentStatus)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The delivery is already active!");
        }

        deliveryJDBCTemplate.updateDeliveryStatus(deliveryId, "ACTIVE");
        return ResponseEntity.ok("Delivery successfully activated.");
    }

    @Transactional
    @Override
    public SimpleResponseCreate updateDelivery(DeliveryUpdateForRequest deliveryRequest) {
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

        Delivery delivery = deliveryRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("Delivery not found for user: " + userEmail));

        String fromWhere = deliveryRequest.getFromWhere();
        String toWhere = deliveryRequest.getToWhere();
        log.info("Start checking addresses...");

        boolean fromWhereExists = deliveryRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
        boolean toWhereExists = deliveryRepository.getSenderByToWhere(toWhere).contains(toWhere);

        if (!fromWhereExists && isAddressValid(fromWhere)) {
            return new SimpleResponseCreate("Адрес отправления не найден: " + fromWhere, false);
        }
        if (!toWhereExists && isAddressValid(toWhere)) {
            return new SimpleResponseCreate("Адрес назначения не найден: " + toWhere, false);
        }

        if (deliveryRepository.existsByTransportNumber(deliveryRequest.getTransportNumber()) &&
                !delivery.getTransportNumber().equals(deliveryRequest.getTransportNumber())) {
            return new SimpleResponseCreate("Transport number already exists: " + deliveryRequest.getTransportNumber(), false);
        }

        delivery.setFromWhere(fromWhere);
        delivery.setToWhere(toWhere);
        delivery.setDispatchDate(deliveryRequest.getDispatchDate());
        delivery.setArrivalDate(deliveryRequest.getArrivalDate());
        delivery.setDescription(deliveryRequest.getDescription());
        delivery.setUserName(deliveryRequest.getUserName());
        delivery.setTransportNumber(deliveryRequest.getTransportNumber());
        delivery.setUser(user);

        deliveryRepository.save(delivery);

        return new SimpleResponseCreate("Delivery updated successfully for user: " + userEmail, true);
    }
}
