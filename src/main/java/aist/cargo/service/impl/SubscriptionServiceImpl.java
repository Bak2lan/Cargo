package aist.cargo.service.impl;

import aist.cargo.dto.user.SubscriptionCreateResponse;
import aist.cargo.dto.user.SubscriptionRequest;
import aist.cargo.entity.Subscription;
import aist.cargo.entity.User;
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

    @Override
    public SubscriptionCreateResponse createSubscription(SubscriptionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Subscription subscription = new Subscription();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(request.getDuration().getMonths());

        double price = 0;
        if (request.getDuration() != null) {
            price = Double.parseDouble(request.getDuration().getPrice().replace(" руб", ""));
        }

        subscription.setPrice(price);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setUser(user);
        subscription.setTransportType(request.getTransportType());
        subscription.setDuration(request.getDuration());

        subscriptionRepository.save(subscription);

        return SubscriptionCreateResponse.builder()
                .id(subscription.getId())
                .userEmail(user.getEmail())
                .endDate(endDate)
                .price(subscription.getPrice())
                .transportType(subscription.getTransportType())
                .startDate(startDate)
                .build();
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
