package com.timecalc.timecalculation.repository;

import java.util.List;
import java.util.Optional;

import com.timecalc.timecalculation.model.entity.DepartmentEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {
    Optional<DepartmentEntity> findByNameIgnoreCase(String name);

    @Query("""
    SELECT d FROM DepartmentEntity d
    WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
       OR LOWER(:name) LIKE LOWER(CONCAT('%', d.name, '%'))
    """)
    List<DepartmentEntity> findByNameContainingIgnoreCase(@Param("name") String name);
}
