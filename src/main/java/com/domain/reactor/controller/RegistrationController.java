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
package com.domain.reactor.controller;

import com.domain.reactor.model.dto.EmployeeDto;
import com.domain.reactor.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/employee")
@RequiredArgsConstructor
public class RegistrationController {

    final private EmployeeServiceImpl employeeService;

    @GetMapping
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{employeeId}")
    public Mono<EmployeeDto> getEmployee(@PathVariable String employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> save(@RequestBody EmployeeDto employeeDto) {
        return employeeService.saveEmployee(employeeDto);
    }

    @PutMapping("{employeeId}")
    public Mono<EmployeeDto> update(
            @RequestBody EmployeeDto employeeDto,
            @PathVariable String employeeId
    ) {
        return employeeService.updateEmployee(employeeDto, employeeId);
    }

    @DeleteMapping("{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }
}
