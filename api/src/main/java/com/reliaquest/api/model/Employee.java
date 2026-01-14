package com.reliaquest.api.model;

import com.reliaquest.api.controller.dto.EmployeeCreationInput;
import java.util.Objects;
import java.util.UUID;

public record Employee(
        String id,
        String employee_name,
        int employee_salary,
        int employee_age,
        String employee_title,
        String employee_email) {
    public Employee {
        Objects.requireNonNull(id);
        Objects.requireNonNull(employee_name);
        Objects.requireNonNull(employee_title);
        Objects.requireNonNull(employee_email);
    }

    public static Employee fromCreationInput(EmployeeCreationInput input) {
        return new Employee(
                UUID.randomUUID().toString(), input.name(), input.salary(), input.age(), input.title(), input.email());
    }
}
