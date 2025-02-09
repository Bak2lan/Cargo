package aist.cargo.service.impl;

import aist.cargo.entity.Subscription;
import aist.cargo.entity.User;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;
import aist.cargo.repository.SubscriptionRepository;
import aist.cargo.repository.UserRepository;
import aist.cargo.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    public Subscription createSubscription(Long userId, double price, TransportType transportType, SubsDuration duration) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(duration.getMonths());

        Subscription subscription = new Subscription(price, startDate, endDate, user, null, transportType, duration);
        return subscriptionRepository.save(subscription);
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
