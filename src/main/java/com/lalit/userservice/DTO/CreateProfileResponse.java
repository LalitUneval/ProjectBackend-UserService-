package com.lalit.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProfileResponse {
    private Long id;

    private String fullName;

    private String originCountry;

    private String currentCity;

    private String visaType;

    private String skills;

    private String phoneNumber;

    private LocalDateTime createdAt;


}
