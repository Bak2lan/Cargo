package aist.cargo.controller;
import aist.cargo.dto.user.SubscriptionRequest;
import aist.cargo.dto.user.SubscriptionResponse;
import aist.cargo.entity.Subscription;
import aist.cargo.enums.SubsDuration;
import aist.cargo.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Create a subscription", description = "Creates a new subscription for a user")
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest request) {
        Subscription subscription = subscriptionService.createSubscription(
                request.getUserId(),
                request.getPrice(),
                request.getTransportType(),
                request.getDuration()
        );
        return ResponseEntity.ok(mapToResponse(subscription));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID", description = "Retrieve subscription details by its ID")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id)
                .map(subscription -> ResponseEntity.ok(mapToResponse(subscription)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user subscriptions", description = "Retrieve all subscriptions of a specific user")
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
        List<SubscriptionResponse> responseList = subscriptions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel subscription", description = "Cancel a subscription by its ID")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/plans")
    @Operation(summary = "Get subscription plans", description = "Retrieve available subscription plans")
    public ResponseEntity<List<String>> getPlans() {
        List<String> plans = List.of(SubsDuration.values()).stream()
                .map(subsDuration -> subsDuration.name() + " - " + subsDuration.getPrice())
                .collect(Collectors.toList());
        return ResponseEntity.ok(plans);
    }

    private SubscriptionResponse mapToResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getPrice(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getUser().getEmail(),
                subscription.getTransportType(),
                subscription.getDuration(),
                "ACTIVE"
        );
    }
}

