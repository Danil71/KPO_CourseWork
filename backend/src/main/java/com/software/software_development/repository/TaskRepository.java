package com.timecalc.timecalculation.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.timecalc.timecalculation.model.entity.TaskEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Long>, PagingAndSortingRepository<TaskEntity, Long> {
       Optional<TaskEntity> findByDescriptionIgnoreCase(String description);

    @Query("""
    SELECT t FROM TaskEntity t
    WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))
       OR LOWER(:description) LIKE LOWER(CONCAT('%', t.description, '%'))
    """)
    List<TaskEntity> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    @Query("""
    SELECT t FROM TaskEntity t
    WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))
       OR LOWER(:description) LIKE LOWER(CONCAT('%', t.description, '%'))
    """)
    Page<TaskEntity> findByNameContainingIgnoreCase(@Param("description") String description, Pageable pageable);
}
