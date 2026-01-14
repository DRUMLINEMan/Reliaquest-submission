package com.reliaquest.api.service.impl;

import com.reliaquest.api.controller.dto.EmployeeCreationInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.IEmployeeService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    final Map<String, Employee> mockEmployeeTable = new HashMap<>();

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(mockEmployeeTable.values());
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        return mockEmployeeTable.values().stream()
                .filter(employee -> employee.employee_name().contains(searchString))
                .toList();
    }

    @Override
    public Employee getEmployeeById(String id) {
        if (!mockEmployeeTable.containsKey(id)) log.warn("No employee found with id {}", id);
        return mockEmployeeTable.get(id);
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        return mockEmployeeTable.values().stream()
                .map(Employee::employee_salary)
                .max(Integer::compareTo)
                .orElse(null);
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        return mockEmployeeTable.values().stream()
                .sorted(Comparator.comparingInt(Employee::employee_salary).reversed())
                .limit(10)
                .map(Employee::employee_name)
                .toList();
    }

    @Override
    public Employee createEmployee(EmployeeCreationInput employeeInput) {
        Employee newEmployee = Employee.fromCreationInput(employeeInput);
        mockEmployeeTable.put(newEmployee.id(), newEmployee);
        return newEmployee;
    }

    @Override
    public boolean deleteEmployeeById(String id) {
        if (!mockEmployeeTable.containsKey(id)) {
            log.warn("No employee with id {} exists. Skipping delete.", id);
            return true;
        }
        return mockEmployeeTable.remove(id) != null;
    }
}
