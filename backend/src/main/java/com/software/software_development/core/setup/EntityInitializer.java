package com.software.software_development.core.setup;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.log.Loggable;
import com.software.software_development.core.utility.Formatter;
import com.software.software_development.model.entity.DepartmentEntity;
import com.software.software_development.model.entity.EmployeeEntity;
import com.software.software_development.model.entity.SoftwareEntity;
import com.software.software_development.model.entity.TaskEntity;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.model.enums.UserRole;
import com.software.software_development.service.entity.DepartmentService;
import com.software.software_development.service.entity.EmployeeService;
import com.software.software_development.service.entity.SoftwareService;
import com.software.software_development.service.entity.TaskService;
import com.software.software_development.service.entity.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityInitializer {

    private final SoftwareService softwareService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final TaskService taskService;
    private final UserService userService;


    @Loggable
    @Transactional
    public void initializeAll() {
        List<SoftwareEntity> softwares = createSoftware();
        List<DepartmentEntity> departments = createDepartments();
        List<EmployeeEntity> employees = createEmployees(departments);
        List<TaskEntity> tasks = createTasks(softwares);

        departments.get(0).addTask(tasks.get(0));
        departments.get(0).addTask(tasks.get(1));
        departments.get(0).addTask(tasks.get(5));

        departments.get(1).addTask(tasks.get(2));

        departments.get(2).addTask(tasks.get(3));

        departments.get(3).addTask(tasks.get(4));

        createUsers(employees);
    }

    @Loggable
    private List<SoftwareEntity> createSoftware() {
        List<SoftwareEntity> softwares = new ArrayList<>();
        softwares.add(softwareService.create(new SoftwareEntity("Project Management Tool", "Программное обеспечение для управления проектами и задачами.", Formatter.parse("2023-01-01T00:00:00Z"), Formatter.parse("2025-12-31T23:59:59Z"))));
        softwares.add(softwareService.create(new SoftwareEntity("CRM System", "Система управления взаимоотношениями с клиентами.", Formatter.parse("2022-05-15T00:00:00Z"), Formatter.parse("2026-05-15T23:59:59Z"))));
        softwares.add(softwareService.create(new SoftwareEntity("HR Management Suite", "Комплекс программ для управления персоналом.", Formatter.parse("2024-02-01T00:00:00Z"), Formatter.parse("2027-02-01T23:59:59Z"))));
        softwares.add(softwareService.create(new SoftwareEntity("Financial Accounting Software", "Программное обеспечение для финансовых операций и отчетности.", Formatter.parse("2023-11-01T00:00:00Z"), Formatter.parse("2028-11-01T23:59:59Z"))));
        softwares.add(softwareService.create(new SoftwareEntity("Inventory Management System", "Управление товарными запасами и уровнями склада.", Formatter.parse("2024-03-10T00:00:00Z"), Formatter.parse("2029-03-10T23:59:59Z"))));
        return softwares;
    }

    @Loggable
    private List<DepartmentEntity> createDepartments() {
        List<DepartmentEntity> departments = new ArrayList<>();
        departments.add(departmentService.create(new DepartmentEntity("Development", 90, 85)));
        departments.add(departmentService.create(new DepartmentEntity("Marketing", 70, 90)));
        departments.add(departmentService.create(new DepartmentEntity("Finance", 95, 92)));
        departments.add(departmentService.create(new DepartmentEntity("Human Resources", 80, 88)));
        departments.add(departmentService.create(new DepartmentEntity("Customer Support", 75, 95)));
        departments.add(departmentService.create(new DepartmentEntity("Quality Assurance", 88, 87)));
        return departments;
    }

    @Loggable
    private List<EmployeeEntity> createEmployees(List<DepartmentEntity> departments) {
        List<EmployeeEntity> employees = new ArrayList<>();
        employees.add(employeeService.create(new EmployeeEntity("Иванов", departments.get(0))));
        employees.add(employeeService.create(new EmployeeEntity("Петрова", departments.get(1))));
        employees.add(employeeService.create(new EmployeeEntity("Путинцев", departments.get(0))));
        employees.add(employeeService.create(new EmployeeEntity("Козлова", departments.get(3))));
        employees.add(employeeService.create(new EmployeeEntity("Смирнов", departments.get(2))));
        employees.add(employeeService.create(new EmployeeEntity("Федорова", departments.get(4))));
        return employees;
    }

    @Loggable
    private List<TaskEntity> createTasks(List<SoftwareEntity> softwares) {
        List<TaskEntity> tasks = new ArrayList<>();
        tasks.add(taskService.create(new TaskEntity("Implement new feature X", 7, Formatter.parse("2025-06-01T09:00:00Z"), Formatter.parse("2025-06-15T17:00:00Z"), 80.0f, softwares.get(0))));
        tasks.add(taskService.create(new TaskEntity("Fix bug in module Y", 5, Formatter.parse("2025-06-03T10:00:00Z"), Formatter.parse("2025-06-10T18:00:00Z"), 40.0f, softwares.get(0))));
        tasks.add(taskService.create(new TaskEntity("Develop marketing campaign Z", 6, Formatter.parse("2025-06-05T09:30:00Z"), Formatter.parse("2025-06-20T17:30:00Z"), 120.0f, softwares.get(1))));
        tasks.add(taskService.create(new TaskEntity("Prepare quarterly financial report", 8, Formatter.parse("2025-06-10T09:00:00Z"), Formatter.parse("2025-06-30T17:00:00Z"), 160.0f, softwares.get(3))));
        tasks.add(taskService.create(new TaskEntity("Onboard new employees", 4, Formatter.parse("2025-06-07T09:00:00Z"), Formatter.parse("2025-06-14T17:00:00Z"), 30.0f, softwares.get(2))));
        tasks.add(taskService.create(new TaskEntity("Optimize database queries", 9, Formatter.parse("2025-06-01T10:00:00Z"), Formatter.parse("2025-06-25T18:00:00Z"), 100.0f, softwares.get(0))));
        return tasks;
    }

    @Loggable
    private void createUsers(List<EmployeeEntity> employees) {
        userService.create(new UserEntity("developer@example.com", "DsD#Ys9dS_cQ", "+79876349406", UserRole.DEVELOPER, employees.get(0)));
        userService.create(new UserEntity("daniilputincev91@gmail.com", "DsD#Ys9dS_cQ", "+79876349406", UserRole.MANAGER, employees.get(2)));
    }
}