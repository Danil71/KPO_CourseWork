package com.timecalc.timecalculation.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.timecalc.timecalculation.model.entity.SoftwareEntity;

public interface SoftwareRepository extends CrudRepository<SoftwareEntity, Long>, PagingAndSortingRepository<SoftwareEntity, Long>{
    @Query("""
        SELECT s FROM SoftwareEntity s
        WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
           OR LOWER(:name) LIKE LOWER(CONCAT('%', s.name, '%'))
    """)
    Page<SoftwareEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);    
    
    @Query("""
        SELECT DISTINCT s FROM SoftwareEntity s
        LEFT JOIN s.tasks t
        WHERE
            (COALESCE(:startDateFrom, null) IS NULL OR s.startDate >= :startDateFrom)
            AND (COALESCE(:startDateTo, null) IS NULL OR s.startDate <= :startDateTo)
            AND (COALESCE(:endDateFrom, null) IS NULL OR s.endDate >= :endDateFrom)
            AND (COALESCE(:endDateTo, null) IS NULL OR s.endDate <= :endDateTo)
            AND (:taskIds IS NULL OR t.id IN :taskIds)
            AND (
                :searchInfo IS NULL
                OR LOWER(s.name) LIKE %:searchInfo%
                OR LOWER(s.description) LIKE %:searchInfo%
                OR EXISTS (
                    SELECT t2 FROM TaskEntity t2
                    WHERE t2.software = s
                    AND (
                        LOWER(t2.description) LIKE %:searchInfo%
                    )
                )
            )
    """)
    Page<SoftwareEntity> findByFilters(
        @Param("startDateFrom") Date startDateFrom,
        @Param("startDateTo") Date startDateTo,
        @Param("endDateFrom") Date endDateFrom,
        @Param("endDateTo") Date endDateTo,
        @Param("taskIds") List<Long> taskIds,
        @Param("searchInfo") String searchInfo,
        Pageable pageable
    );

    @Query("SELECT s FROM SoftwareEntity s " +
        "LEFT JOIN FETCH s.tasks t " +
        "LEFT JOIN FETCH t.departments " +
        "ORDER BY s.id ASC")
    List<SoftwareEntity> findAllWithTasksAndDepartments();
}
