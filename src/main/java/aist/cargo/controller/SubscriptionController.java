package aist.cargo.controller;

import aist.cargo.enums.SubsDuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @GetMapping("/plans")
    public ResponseEntity<List<String>> getPlans() {
        List<String> plans = Arrays.stream(SubsDuration.values())
                .map(subsDuration -> subsDuration.name() + " - " + subsDuration.getPrice())
                .collect(Collectors.toList());
        return ResponseEntity.ok(plans);
    }
}
