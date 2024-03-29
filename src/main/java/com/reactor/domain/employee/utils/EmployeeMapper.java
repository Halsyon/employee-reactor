/*
Copyright [2023] [Halsyon]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.reactor.domain.employee.utils;

import com.reactor.domain.employee.model.dto.DctDto;
import com.reactor.domain.employee.model.dto.EmployeeData;
import com.reactor.domain.employee.model.dto.EmployeeDto;
import com.reactor.domain.employee.model.entity.Dct;
import com.reactor.domain.employee.model.entity.Employee;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description - class to map an entity to Dto
 */
public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                mapToListDctDto(employee.getDctList())
        );
    }

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        return new Employee(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                EmployeeMapper.mapToListDct(employeeDto.getDctList())
        );
    }

    public static Dct mapToDct(DctDto document) {
        return new Dct(
                document.getDocumentId(),
                document.getDateOfIssue(),
                document.getExpirationDate(),
                document.getFullName(),
                document.getShortName(),
                document.getOrganization(),
                document.getDescription()
        );
    }

    public static DctDto mapToDctDto(Dct document) {
        return new DctDto(
                document.getDocumentId(),
                document.getDateOfIssue(),
                document.getExpirationDate(),
                document.getFullName(),
                document.getShortName(),
                document.getOrganization(),
                document.getDescription()
        );
    }

    public static EmployeeData mapToEmployeeData(EmployeeDto employeeDto) {
        return new EmployeeData(null, "some text", LocalDateTime.now(),
                new EmployeeDto(
                employeeDto.getId(),
                employeeDto.getFirstName(),
                employeeDto.getLastName(),
                employeeDto.getEmail(),
                employeeDto.getDctList())
        );
    }

    public static List<Dct> mapToListDct(List<DctDto> list) {
        return list.stream().map(EmployeeMapper::mapToDct).toList();
    }

    public static List<DctDto> mapToListDctDto(List<Dct> list) {
        return list.stream().map(EmployeeMapper::mapToDctDto).toList();
    }
}
