package com.software.software_development.model.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "department")
public class DepartmentEntity extends BaseEntity {
    @Check(constraints = "length(name) >= 1")
    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Column(nullable = false)
    private int specialty;

    @Column(nullable = false)
    private int efficiency;

    @OneToMany(mappedBy = "department")
    @OrderBy("id ASC")
    private Set<EmployeeEntity> employees = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "department_task",
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<TaskEntity> tasks = new HashSet<>();

    public DepartmentEntity(String name, int specialty, int efficiency) {
        this.name = name;
        this.specialty = specialty;
        this.efficiency = efficiency;
    }

    public void addTask(TaskEntity task) {
        this.tasks.add(task);
        task.getDepartments().add(this);
    }

    public void deleteTask(TaskEntity task) {
        this.tasks.remove(task);
        task.getDepartments().remove(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialty, efficiency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final DepartmentEntity other = (DepartmentEntity) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.name, other.name)
            && Objects.equals(this.specialty, other.specialty)
            && Objects.equals(this.efficiency, other.efficiency);
    }

    @PreRemove
    private void preRemove() {
        for (EmployeeEntity employee : employees) {
            employee.setDepartment(null);
        }
    }
}
