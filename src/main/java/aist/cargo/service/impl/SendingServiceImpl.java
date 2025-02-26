package aist.cargo.service.impl;

import aist.cargo.dto.user.SendingRequest;
import aist.cargo.dto.user.SendingResponse;
import aist.cargo.entity.Sending;
import aist.cargo.entity.User;
import aist.cargo.enums.Status;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.SendingRepository;
import aist.cargo.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import aist.cargo.service.SendingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SendingServiceImpl implements SendingService {

    private final UserRepository userRepository;
    private final SendingRepository sendingRepository;
    private final UserServiceImpl userServiceImpl;

    public SendingServiceImpl(UserRepository userRepository, SendingRepository sendingRepository, UserServiceImpl userServiceImpl) {
        this.userRepository = userRepository;
        this.sendingRepository = sendingRepository;
        this.userServiceImpl = userServiceImpl;
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
}

