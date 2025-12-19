package com.software.software_development.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.software.software_development.model.entity.EmployeeEntity;

public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long>, PagingAndSortingRepository<EmployeeEntity, Long> {
       Optional<EmployeeEntity> findByNameIgnoreCase(String name);

    @Query("""
    SELECT d FROM EmployeeEntity d
    WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
       OR LOWER(:name) LIKE LOWER(CONCAT('%', d.name, '%'))
    """)
    List<EmployeeEntity> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("""
    SELECT d FROM EmployeeEntity d
    WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
       OR LOWER(:name) LIKE LOWER(CONCAT('%', d.name, '%'))
    """)
    Page<EmployeeEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

}
