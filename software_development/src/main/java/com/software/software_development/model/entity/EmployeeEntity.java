package com.software.software_development.model.entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class EmployeeEntity extends BaseEntity {
    @Check(constraints = "length(name) >= 1")
    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Transient
    private Double experience;

    @Transient
    private Double speed;

    @ManyToOne
    @JoinColumn(name = "fk_id_department")
    private DepartmentEntity department;

    public EmployeeEntity(String name, DepartmentEntity department) {
    this.name = name;
    this.department = department;
    }

    @PostLoad
    private void calculateMetrics() {
        this.experience = 0.0;
        this.speed = 0.0;

        if (this.department != null) {

            Set<TaskEntity> departmentTasks = this.department.getTasks();

            if (departmentTasks != null && !departmentTasks.isEmpty()) {
                for (TaskEntity task : departmentTasks) {

                    if (task.getStartDate() != null && task.getEndDate() != null) {


                        LocalDate startDateLocal = task.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        LocalDate endDateLocal = task.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                        long daysBetween = ChronoUnit.DAYS.between(startDateLocal, endDateLocal);

                        if (daysBetween < 0) {
                            return;
                        }
                        this.experience += (double) task.getDifficulty() * daysBetween;

                    }

                    if (task.getHours() > 0 && task.getDifficulty() > 0) {
                        this.speed += (double) task.getDifficulty() * task.getHours();
                    }
                }
            }
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
            id, name, experience, speed,
            Optional.ofNullable(department).map(DepartmentEntity::getId).orElse(null)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final EmployeeEntity other = (EmployeeEntity) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.name, other.name)
            && this.experience == other.experience
            && this.speed == other.speed
            && Objects.equals(
                Optional.ofNullable(this.department).map(DepartmentEntity::getId).orElse(null),
                Optional.ofNullable(other.department).map(DepartmentEntity::getId).orElse(null));
    }
}
