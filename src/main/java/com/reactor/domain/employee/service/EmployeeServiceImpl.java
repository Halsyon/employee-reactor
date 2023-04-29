/*
[Halsyon]
Copyright (c) 2016-2023 VMware Inc. or its affiliates, All Rights Reserved.

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
package com.reactor.domain.employee.service;

import com.reactor.config.ReactiveProducerService;
import com.reactor.domain.employee.model.dto.EmployeeData;
import com.reactor.domain.employee.model.dto.EmployeeDto;
import com.reactor.domain.employee.model.entity.Employee;
import com.reactor.domain.employee.repository.EmployeeRepository;
import com.reactor.domain.employee.utils.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    final private EmployeeRepository employeeRepository;
    private final ReactiveProducerService producerService;
    private EmployeeData employeeData = new EmployeeData();

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> employeeFlux = employeeRepository.findAll();
        return employeeFlux
                .map(EmployeeMapper::mapToEmployeeDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String employeeId) {
        if (Objects.isNull(employeeId)) {
            throw new RuntimeException("Id must not be null");
        }
        Mono<Employee> employeeMono = employeeRepository.findById(employeeId)
                .switchIfEmpty(Mono.error(new RuntimeException("The employee does not exist!")));

        return employeeMono.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        employee.getDctList().forEach(dct -> dct.setDocumentId(initUUID()));
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(
            EmployeeDto employeeDto,
            String employeeId
    ) {
        if (Objects.isNull(employeeId)) {
            throw new RuntimeException("Id must not be null!");
        }

        var employeeDtoUpdated = setIdDct(employeeDto);
        Mono<Employee> employeeMono = employeeRepository.findById(employeeId)
                .switchIfEmpty(Mono.error(new RuntimeException("The employee does not exist!")));

        return employeeMono
                .flatMap((existingEmployee) -> {
                    existingEmployee.setFirstName(employeeDtoUpdated.getFirstName());
                    existingEmployee.setLastName(employeeDtoUpdated.getLastName());
                    existingEmployee.setEmail(employeeDtoUpdated.getEmail());
                    existingEmployee.setDctList(
                            EmployeeMapper.mapToListDct(employeeDtoUpdated.getDctList())
                    );
                    return employeeRepository.save(existingEmployee);
                })
                .map((EmployeeMapper::mapToEmployeeDto));
    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        if (Objects.isNull(employeeId)) {
            throw new RuntimeException("Id must not be null");
        }
        var employeeMono = employeeRepository
                .findById(employeeId)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("The employee does not exist!"))
                );

        employeeMono.doOnNext(this::mapTo).subscribe();
        var rsl = employeeMono
                .flatMap(employeeRepository::delete);

        producerService.send(employeeData);
        return rsl;
    }

    private String initUUID() {
        return UUID.randomUUID().toString();
    }

    private void mapTo(Employee employee) {
        var employeeDto = EmployeeMapper.mapToEmployeeDto(employee);
        employeeData = EmployeeMapper.mapToEmployeeData(employeeDto);
        employeeData.setEmployeeDataId(initUUID());
    }

    private EmployeeDto setIdDct(EmployeeDto employeeDto) {
        employeeDto
                .getDctList()
                .forEach(dct -> {
                            if (Objects.isNull(dct.getDocumentId())) {
                                dct.setDocumentId(initUUID());
                            }
                        }
                );
        return employeeDto;
    }
}
