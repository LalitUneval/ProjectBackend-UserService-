package com.lalit.userservice.controller;

import com.lalit.userservice.DTO.*;
import com.lalit.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")

public class UserProfileController {

    private final UserProfileService userProfileService;



    @PostMapping("/profile")
    public ResponseEntity<CreateProfileResponse> createProfile(
            @Valid @RequestBody CreateProfileRequest request,
            @RequestHeader("X-User-Id") Long userId) {

        // Ensure the profile ID matches the authenticated user
        request.setId(userId);
        CreateProfileResponse response = userProfileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/profile/{userId}")
    public ResponseEntity<CreateProfileResponse> getProfile(@PathVariable Long userId) {
        CreateProfileResponse response = userProfileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public List<CreateProfileResponse> getUsersByIds(@RequestBody List<Long> userIds) {
        return userProfileService.getUsersByIds(userIds);
    }

    /**
     * Get current user's profile
     * GET /api/users/profile/me
     */

    @GetMapping("/profile/me")
    public ResponseEntity<CreateProfileResponse> getMyProfile(@RequestHeader("X-User-Id") Long userId) {
        CreateProfileResponse response = userProfileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }


    //no need to change
    @PutMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateProfileRequest request,
            @RequestHeader("X-User-Id") Long authUserId) {

        // Ensure user can only update their own profile
        if (!userId.equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserProfileResponse response = userProfileService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/by-country/{country}")
    public ResponseEntity<List<SearchResponse>> getUsersByCountry(@PathVariable String country) {
        List<SearchResponse> users = userProfileService.getUsersByCountry(country);
        return ResponseEntity.ok(users);
    }


    //Handle if the country was not found
    @GetMapping("/by-city/{city}")
    public ResponseEntity<List<SearchResponse>> getUsersByCity(@PathVariable String city) {
        List<SearchResponse> users = userProfileService.getUsersByCity(city);
        return ResponseEntity.ok(users);
    }


    //Handle if the country was not found
    @GetMapping("/by-visa/{visaType}")
    public ResponseEntity<List<SearchResponse>> getUsersByVisaType(@PathVariable String visaType) {
        List<SearchResponse> users = userProfileService.getUsersByVisaType(visaType);
        return ResponseEntity.ok(users);
    }

// same one handle of upper
    @GetMapping("/by-skill/{skill}")
    public ResponseEntity<List<SearchResponse>> getUsersBySkill(@PathVariable String skill) {
        List<SearchResponse> users = userProfileService.getUsersBySkill(skill);
        return ResponseEntity.ok(users);
    }



    /**
     * Search users by name
     * GET /api/users/search?name=John
     */
    //Not working
    @GetMapping("/search")
    public ResponseEntity<List<UserProfileResponse>> searchUsersByName(@RequestParam String name) {
        List<UserProfileResponse> users = userProfileService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Advanced search users
     * GET /api/users/advanced-search?country=India&city=SF&visaType=H1B
     */
    @GetMapping("/advanced-search")
    public ResponseEntity<List<SearchResponse>> advancedSearch(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String visaType) {

        List<SearchResponse> users = userProfileService.searchUsers(country, city, visaType);
        return ResponseEntity.ok(users);
    }


    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long authUserId) {

        // Ensure user can only delete their own profile
        if (!userId.equals(authUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userProfileService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
}