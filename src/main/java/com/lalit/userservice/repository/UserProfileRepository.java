package com.lalit.userservice.repository;

import com.lalit.userservice.entity.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    // Find users by origin country
    List<UserProfile> findByOriginCountry(String originCountry);

    // Find users by current city
    List<UserProfile> findByCurrentCity(String currentCity);

    // Find users by visa type
    List<UserProfile> findByVisaType(String visaType);

    // Find users by origin country and current city
    List<UserProfile> findByOriginCountryAndCurrentCity(String originCountry, String currentCity);

    // Find users by origin country and visa type
    List<UserProfile> findByOriginCountryAndVisaType(String originCountry, String visaType);

    // Search users by skills (using LIKE query)
    @Query("SELECT u FROM UserProfile u WHERE LOWER(u.skills) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<UserProfile> findBySkillsContaining(@Param("skill") String skill);

    // Search users by name (case-insensitive)
    List<UserProfile> findByFullNameContainingIgnoreCase(String fullName);

    // Find users by multiple criteria
    @Query("SELECT u FROM UserProfile u WHERE " +
            "(:originCountry IS NULL OR u.originCountry = :originCountry) AND " +
            "(:currentCity IS NULL OR u.currentCity = :currentCity) AND " +
            "(:visaType IS NULL OR u.visaType = :visaType)")
    List<UserProfile> searchUsers(@Param("originCountry") String originCountry,
                                  @Param("currentCity") String currentCity,
                                  @Param("visaType") String visaType);


}
