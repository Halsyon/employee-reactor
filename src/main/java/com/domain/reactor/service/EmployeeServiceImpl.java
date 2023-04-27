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
package com.domain.reactor.service;

import com.domain.reactor.model.dto.EmployeeDto;
import com.domain.reactor.model.entity.Employee;
import com.domain.reactor.repository.EmployeeRepository;
import com.domain.reactor.utils.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    final private EmployeeRepository employeeRepository;

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
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map(EmployeeMapper::mapToEmployeeDto);
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(
            EmployeeDto employeeDto,
            String employeeId
    ) {
        if (Objects.isNull(employeeId)) {
            throw new RuntimeException("Id must not be null");
        }

        Mono<Employee> employeeMono = employeeRepository.findById(employeeId)
                .switchIfEmpty(Mono.error(new RuntimeException("The employee does not exist!")));

        return employeeMono
                .flatMap((existingEmployee) -> {
                    existingEmployee.setFirstName(employeeDto.getFirstName());
                    existingEmployee.setLastName(employeeDto.getLastName());
                    existingEmployee.setEmail(employeeDto.getEmail());
                    return employeeRepository.save(existingEmployee);
                })
                .map((EmployeeMapper::mapToEmployeeDto));
    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        if (Objects.isNull(employeeId)) {
            throw new RuntimeException("Id must not be null");
        }
        return employeeRepository.findById(employeeId)
                .flatMap(employeeRepository::delete);
    }
}
