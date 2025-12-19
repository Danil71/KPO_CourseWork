package com.software.software_development.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task")
public class TaskEntity extends BaseEntity {
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int difficulty;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "software_id", nullable = false)
    private SoftwareEntity software;

    @Column(nullable = false)
    private float hours;

    @ManyToMany(mappedBy = "tasks")
    @OrderBy("id ASC")
    private Set<DepartmentEntity> departments = new HashSet<>();

    public TaskEntity(String description, int difficulty, Date startDate, Date endDate, float hours, SoftwareEntity software) {
        this.description = description;
        this.difficulty = difficulty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hours = hours;
        this.software = software;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id, description, difficulty, startDate, endDate, hours,
            Optional.ofNullable(software).map(SoftwareEntity::getId).orElse(null)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        final TaskEntity other = (TaskEntity) obj;

        return Objects.equals(this.id, other.id)
                && Objects.equals(this.description, other.description)
                && this.difficulty == other.difficulty
                && Objects.equals(this.startDate, other.startDate)
                && Objects.equals(this.endDate, other.endDate)
                && Float.compare(this.hours, other.hours) == 0
                && Objects.equals(
                    Optional.ofNullable(this.software).map(SoftwareEntity::getId).orElse(null),
                    Optional.ofNullable(other.software).map(SoftwareEntity::getId).orElse(null)
                );
    }

}
