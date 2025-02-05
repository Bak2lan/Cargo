package aist.cargo.service;

import aist.cargo.entity.Subscription;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;

import java.util.Optional;


public interface SubscriptionService {
    Subscription createSubscription(Long userId, double price, TransportType transportType, SubsDuration duration);

    Optional<Subscription> getSubscriptionById(Long id);

    Optional<Subscription> getUserSubscription(Long userId);

    void cancelSubscription(Long id);
}
