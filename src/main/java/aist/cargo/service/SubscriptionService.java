package aist.cargo.service;

import aist.cargo.dto.user.SubscriptionCreateResponse;
import aist.cargo.dto.user.SubscriptionRequest;
import aist.cargo.dto.user.SubscriptionResponse;
import aist.cargo.entity.Subscription;
import aist.cargo.enums.SubsDuration;
import aist.cargo.enums.TransportType;

import java.util.List;
import java.util.Optional;


public interface SubscriptionService {
    SubscriptionCreateResponse createSubscription(SubscriptionRequest subscriptionRequest);

    Optional<Subscription> getSubscriptionById(Long id);

    List<Subscription> getUserSubscription(Long userId);

    void cancelSubscription(Long id);

}