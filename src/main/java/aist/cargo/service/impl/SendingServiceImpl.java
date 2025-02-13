package aist.cargo.service.impl;

import aist.cargo.dto.user.AutoCompleteRequest;
import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.entity.Sending;
import aist.cargo.entity.User;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.SendingRepository;
import aist.cargo.repository.UserRepository;
import aist.cargo.service.SendingService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SendingServiceImpl implements SendingService {

    private final UserRepository userRepository;
    private final SendingRepository sendingRepository;

    public SendingServiceImpl(UserRepository userRepository, SendingRepository sendingRepository) {
        this.userRepository = userRepository;
        this.sendingRepository = sendingRepository;
    }

    @Override
    public boolean createSending(SendingRequest sendingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.hasActiveSubscriptionForPackage(sendingRequest.getPackageType())) {
            return false;
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
        return true;
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

    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с адресом электронной почты: %s не найден!", email)
                ));
    }

    @Override
    public ResponseEntity<?> userSendPackage(AutoCompleteRequest autoCompleteRequest) {
        User user = this.getAuthUser();
        String fromWhere = autoCompleteRequest.fromWhere();
        String toWhere = autoCompleteRequest.toWhere();
        boolean fromWhereExists = this.sendingRepository.getSenderByFromWhere(fromWhere).contains(fromWhere);
        boolean toWhereExists = this.sendingRepository.getSenderByToWhere(toWhere).contains(toWhere);
        if (!fromWhereExists && !this.isAddressValid(fromWhere)) {
            throw new NotFoundException("Адрес отправления не найден: " + fromWhere);
        } else {
            if (!toWhereExists && !this.isAddressValid(toWhere)) {
                throw new NotFoundException("Адрес назначения не найден: " + toWhere);
            } else {
                Sending newPackage = new Sending();
                newPackage.setFromWhere(fromWhere);
                newPackage.setToWhere(toWhere);
                newPackage.setUser(user);
                this.sendingRepository.save(newPackage);
                return ResponseEntity.ok("Посылка успешно отправлена");
            }

        }
    }

    private boolean isAddressValid(String address) {
        try {
            String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&addressdetails=1";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, new Object[0]);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return false;
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode node = objectMapper.readTree((String)response.getBody());
                return !node.isEmpty();
            }
        } catch (Exception var7) {
            return false;
        }
    }

}

