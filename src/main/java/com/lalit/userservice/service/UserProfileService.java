package com.lalit.userservice.service;


import com.lalit.userservice.DTO.*;
import com.lalit.userservice.entity.UserProfile;
import com.lalit.userservice.exception.ProfileAlreadyExistsException;
import com.lalit.userservice.exception.ProfileNotFoundException;
import com.lalit.userservice.exception.ResourceNotFoundException;
import com.lalit.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;


    public CreateProfileResponse createProfile(CreateProfileRequest request) {
        // Check if profile already exists
        if (userProfileRepository.existsById(request.getId())) {
            throw new ProfileAlreadyExistsException("Profile already exists for user: " + request.getId());
        }

        UserProfile profile = UserProfile.builder()
                .id(request.getId()) // Same as AuthUser ID
                .fullName(request.getFullName())
                .originCountry(request.getOriginCountry())
                .currentCity(request.getCurrentCity())
                .visaType(request.getVisaType())
                .skills(request.getSkills())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .build();

        UserProfile savedProfile = userProfileRepository.save(profile);

        return mapToCreateProfileResponse(savedProfile);
    }

    public CreateProfileResponse getProfile(Long userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new 	ProfileNotFoundException("Profile not found for user: " + userId));

        return mapToCreateProfileResponse(profile);
    }
    public List<CreateProfileResponse> getUsersByIds(List<Long> userIds) {

        List<UserProfile> users = userProfileRepository.findAllById(userIds);

        return users.stream()
                .map(this::mapToCreateProfileResponse)
                .collect(Collectors.toList());
    }

    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for user: " + userId));

        // Update fields
        if (request.getFullName() != null) {
            profile.setFullName(request.getFullName());
        }
        if (request.getCurrentCity() != null) {
            profile.setCurrentCity(request.getCurrentCity());
        }
        if (request.getVisaType() != null) {
            profile.setVisaType(request.getVisaType());
        }
        if (request.getSkills() != null) {
            profile.setSkills(request.getSkills());
        }
        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }

        profile.setUpdatedAt(LocalDateTime.now());

        UserProfile updatedProfile = userProfileRepository.save(profile);

        return mapToResponse(updatedProfile);
    }


    public List<SearchResponse> getUsersByCountry(String country) {
        List<UserProfile> profiles = userProfileRepository.findByOriginCountry(country);
        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+country);
        }
        return profiles.stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }


    public List<SearchResponse> getUsersByCity(String city) {
        List<UserProfile> profiles = userProfileRepository.findByCurrentCity(city);

        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+city);
        }

        return profiles.stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }


    public List<SearchResponse> getUsersByVisaType(String visaType) {
        List<UserProfile> profiles = userProfileRepository.findByVisaType(visaType);

        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+visaType);
        }

        return profiles.stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }


    public List<UserProfileResponse> getUsersByCountryAndCity(String country, String city) {
        List<UserProfile> profiles = userProfileRepository.findByOriginCountryAndCurrentCity(country, city);

        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+country+" & "+city);
        }

        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public List<SearchResponse> getUsersBySkill(String skill) {
        List<UserProfile> profiles = userProfileRepository.findBySkillsContaining(skill);

        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+skill);
        }

        return profiles.stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }


    public List<UserProfileResponse> searchUsersByName(String name) {
        List<UserProfile> profiles = userProfileRepository.findByFullNameContainingIgnoreCase(name);

        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+name);
        }

        return profiles.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<SearchResponse> searchUsers(String country, String city, String visaType) {
        List<UserProfile> profiles = userProfileRepository.searchUsers(country, city, visaType);

        if(profiles.isEmpty())
        {
            throw new ResourceNotFoundException("Resource not found with: "+country+" & "+city+" & "+visaType);
        }

        return profiles.stream()
                .map(this::mapToSearchResponse)
                .collect(Collectors.toList());
    }

    public void deleteProfile(Long userId) {
        if (!userProfileRepository.existsById(userId)) {
            throw new ProfileNotFoundException("Profile not found for user: " + userId);
        }
        userProfileRepository.deleteById(userId);
    }

    public boolean profileExists(Long userId) {
        return userProfileRepository.existsById(userId);
    }



    // Helper method to map entity to response
    private UserProfileResponse mapToResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .originCountry(profile.getOriginCountry())
                .currentCity(profile.getCurrentCity())
                .visaType(profile.getVisaType())
                .skills(profile.getSkills())
                .phoneNumber(profile.getPhoneNumber())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
    private CreateProfileResponse mapToCreateProfileResponse(UserProfile profile)
    {
        return CreateProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .originCountry(profile.getOriginCountry())
                .currentCity(profile.getCurrentCity())
                .visaType(profile.getVisaType())
                .skills(profile.getSkills())
                .phoneNumber(profile.getPhoneNumber())
                .createdAt(profile.getCreatedAt())
                .build();
    }
    private SearchResponse mapToSearchResponse(UserProfile profile)
    {
        return SearchResponse.builder()
            .id(profile.getId())
            .fullName(profile.getFullName())
            .originCountry(profile.getOriginCountry())
            .currentCity(profile.getCurrentCity())
            .visaType(profile.getVisaType())
            .build();
    }


}
