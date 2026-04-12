package com.lalit.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    private Long id; // Same ID from AuthUser

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String originCountry;

    private String currentCity;

    private String visaType; // F1, H1B, OPT, etc.

    private String skills;

    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
