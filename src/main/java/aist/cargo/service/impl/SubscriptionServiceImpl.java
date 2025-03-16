package aist.cargo.service.impl;

import aist.cargo.dto.user.SubscriptionCreateResponse;
import aist.cargo.entity.Subscription;
import aist.cargo.entity.User;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import aist.cargo.exception.NotFoundException;
import aist.cargo.repository.SubscriptionRepository;
import aist.cargo.repository.UserRepository;
import aist.cargo.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public SubscriptionCreateResponse createSubscription(double price, TransportType transportType, SubsDuration duration) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(duration.getMonths());

        Subscription subscription = new Subscription();
        subscription.setPrice(price);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setUser(user);
        subscription.setTransportType(transportType);
        subscription.setDuration(duration);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        SubscriptionCreateResponse response = new  SubscriptionCreateResponse();
        response.setId(savedSubscription.getId());
        response.setPrice(savedSubscription.getPrice());
        response.setStartDate(savedSubscription.getStartDate());
        response.setEndDate(savedSubscription.getEndDate());
        response.setUserEmail(savedSubscription.getUser().getEmail());
        response.setTransportType(savedSubscription.getTransportType());
        response.setDuration(savedSubscription.getDuration());
        return response;
    }




    @Override
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public List<Subscription> getUserSubscription(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public void cancelSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
}
