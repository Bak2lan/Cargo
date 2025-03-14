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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    public CargoResponse getDeliveryById(Long deliveryId) {
        return deliveryJDBCTemplate.getDeliveryById(deliveryId);
    }

    @Override
    public List<CargoResponse> getAllCargo(SearchRequest searchRequest) {
        User user = userServiceImpl.getAuthenticatedUser();
        return deliveryJDBCTemplate.getAllCargo(
                searchRequest.fromWhere(),
                searchRequest.toWhere(),
                searchRequest.dispatchDate(),
                searchRequest.arrivalDate(),
                searchRequest.packageType(),
                searchRequest.size(),
                searchRequest.role(),
                user.getEmail()
        );
    }

    public SimpleResponseCreate createDelivery(DeliveryRequest deliveryRequest, String userEmail) {
        User user = userRepository.getUserByEmail(userEmail).orElseThrow(
                () -> new NotFoundException("User not found email " + userEmail));

        if (userRepository.existsSubscriptionsByUserEmail(userEmail)) {

            String fromWhere = deliveryRequest.getFromWhere();
            String toWhere = deliveryRequest.getToWhere();
            log.info("test1");

            boolean fromWhereExists = deliveryRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
            boolean toWhereExists = deliveryRepository.getSenderByToWhere(toWhere).contains(toWhere);
            log.info("test2");

            if (!fromWhereExists && isAddressValid(fromWhere)) {
                log.info("test3");
                return new SimpleResponseCreate("Адрес отправления не найден: " + fromWhere, false);
            }
            log.info("test4");
            if (!toWhereExists && isAddressValid(toWhere)) {
                return new SimpleResponseCreate("Адрес назначения не найден: " + toWhere, false);
            }
            log.info("test5");

            user.getDeliveries().add(mapToDelivery(deliveryRequest, user));
            userRepository.save(user);

            return new SimpleResponseCreate("Delivery created successfully. User ID: " + user.getId(), true);
        } else {
            return new SimpleResponseCreate("No subscription found for the user with email: " + userEmail, false);
        }
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

    public Delivery mapToDelivery(DeliveryRequest deliveryRequest, User user) {
        Delivery delivery = new Delivery();
        delivery.setUserName(deliveryRequest.getFullName());
        delivery.setFromWhere(deliveryRequest.getFromWhere());
        delivery.setToWhere(deliveryRequest.getToWhere());
        delivery.setDispatchDate(deliveryRequest.getDispatchDate());
        delivery.setArrivalDate(deliveryRequest.getArrivalDate());
        delivery.setDescription(deliveryRequest.getDescription());
        delivery.setPackageType(deliveryRequest.getPackageType());
        delivery.setTruckSize(deliveryRequest.getTruckSize());
        delivery.setTransportType(deliveryRequest.getTransportType());
        delivery.setTransportNumber(deliveryRequest.getTransportNumber());
        delivery.setSize(deliveryRequest.getSize());
        delivery.setRole(deliveryRequest.getRole());
        delivery.setUser(user);
        deliveryRepository.save(delivery);
        return delivery;
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

        if (deliveryJDBCTemplate.isDeliveryOwnedByUser(deliveryId, user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to archive this delivery.");
        }
        String currentStatus = deliveryJDBCTemplate.getDeliveryStatus(deliveryId);

        if ("ARCHIVED".equals(currentStatus)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The delivery is already archived!");
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
}