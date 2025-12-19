package com.software.software_development.core.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.web.dto.entity.DepartmentDto;

@Configuration
public class MapperConfiguration {
    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(DepartmentEntity.class, DepartmentDto.class).addMappings(mapper -> mapper.skip(DepartmentDto::setTasks));

        return modelMapper;
    }
}
