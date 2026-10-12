package com.example.activityservice.controller;

import com.example.activityservice.dto.ActivityRequest;
import com.example.activityservice.dto.ActivityResponse;
import com.example.activityservice.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(
            @RequestBody ActivityRequest request,
            Authentication authentication) {

        String authenticatedUserId = authentication.getName();

        request.setUserId(authenticatedUserId);

        return ResponseEntity.ok(
                activityService.trackActivity(request)
        );
    }
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(
            Authentication authentication) {

        String userId = authentication.getName();

        return ResponseEntity.ok(
                activityService.getUserActivities(userId)
        );
    }
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivityById(
            @PathVariable String activityId,
            Authentication authentication) {

        ActivityResponse activity =
                activityService.getActivityById(activityId);

        String authenticatedUserId = authentication.getName();

        if (!authenticatedUserId.equals(activity.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(activity);
    }
}

