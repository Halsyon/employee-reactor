/*
Copyright (c) 2016-2023 VMware Inc. or its affiliates, All Rights Reserved. [Halsyon]

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
package com.reactor.config;

import com.reactor.domain.employee.model.dto.EmployeeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReactiveProducerService {
    private final Logger log = LoggerFactory.getLogger(ReactiveProducerService.class);
    private final ReactiveKafkaProducerTemplate<String, EmployeeData> reactiveKafkaProducerTemplate;

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    public ReactiveProducerService(
            ReactiveKafkaProducerTemplate<String, EmployeeData> reactiveKafkaProducerTemplate
    ) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }

    public void send(EmployeeData fakeProducerDTO) {
        log.info("send to topic={}, {}={},", topic, EmployeeData.class.getSimpleName(), fakeProducerDTO);
        reactiveKafkaProducerTemplate.send(topic, fakeProducerDTO)
                .doOnSuccess(senderResult ->
                        log.info("sent {} offset : {}", fakeProducerDTO, senderResult.recordMetadata().offset()
                        )
                )
                .subscribe();
    }
}
