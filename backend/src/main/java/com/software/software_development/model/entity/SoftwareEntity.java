package com.software.software_development.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "software")
public class SoftwareEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;
    
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @OneToMany(mappedBy = "software", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskEntity> tasks = new HashSet<>();

    public SoftwareEntity(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addTask(TaskEntity task) {
        tasks.add(task);
        task.setSoftware(this);
    }

    public void removeTask(TaskEntity task) {
        tasks.remove(task);
        task.setSoftware(null);
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, startDate, endDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final SoftwareEntity other = (SoftwareEntity) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.name, other.name)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.startDate, other.startDate)
                && Objects.equals(this.endDate, other.endDate);
    }
}
