package com.reliaquest.api.controller.dto;

import java.util.Objects;

public record EmployeeCreationInput(String name, Integer salary, Integer age, String title, String email) {
    public EmployeeCreationInput {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Invalid name");
        Objects.requireNonNull(salary);
        Objects.requireNonNull(age);
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
        Objects.requireNonNull(email);
    }
}
