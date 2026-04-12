package com.lalit.userservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProfileRequest {


    private Long id; // Same as AuthUser ID

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Origin country is required")
    private String originCountry;

    private String currentCity;
    private String visaType;
    private String skills;
    private String phoneNumber;
}
