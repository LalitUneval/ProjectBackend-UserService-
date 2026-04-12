package com.lalit.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    private Long id;

    private String fullName;

    private String originCountry;

    private String currentCity;

    private String visaType;

}
