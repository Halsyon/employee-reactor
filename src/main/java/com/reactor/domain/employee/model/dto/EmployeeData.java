package com.reactor.domain.employee.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeData {

    private String employeeDataId;
    private String description;
    private LocalDateTime dateOfCompletion;

    private EmployeeDto employee;
}
