package com.domain.reactor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DctDto {
    private String documentId;
    private String dateOfIssue;
    private String expirationDate;
    private String fullName;
    private String shortName;
    private String organization;
    private String description;
}
