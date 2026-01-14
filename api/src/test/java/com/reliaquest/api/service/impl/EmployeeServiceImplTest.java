package com.reliaquest.api.service.impl;

import com.reliaquest.api.controller.dto.EmployeeCreationInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    private Map<String,Employee> mockEmployeeTable;
    private IEmployeeService service;

    @BeforeEach
    void setUp() {
        mockEmployeeTable = new HashMap<>();
        service = new EmployeeServiceImpl();
        ReflectionTestUtils.setField(service, "mockEmployeeTable", mockEmployeeTable);
    }

    private void createMockEmployee(String id, String name, int salary) {
        Employee employee = Employee.fromCreationInput(new EmployeeCreationInput(name, salary, 30, "manager",
                name + "@mail.com"));
        mockEmployeeTable.put(id, employee);
    }

    @Test
    void getAllEmployees_returnsAllEmployees() {
        createMockEmployee("1", "Alice", 50000);
        createMockEmployee("2", "Bob", 60000);
        List<Employee> result = service.getAllEmployees();
        assertEquals(2, result.size());
    }

    @Test
    void getAllEmployees_emptyTable_returnsEmptyList() {
        List<Employee> result = service.getAllEmployees();
        assertTrue(result.isEmpty());
    }

    @Test
    void getEmployeesByNameSearch_returnsMatchingEmployees() {
        createMockEmployee("1", "Alice", 50000);
        createMockEmployee("2", "Alicia", 60000);
        createMockEmployee("3", "Bob", 70000);
        List<Employee> result = service.getEmployeesByNameSearch("Ali");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(e -> e.employee_name().contains("Ali")));
    }

    @Test
    void getEmployeesByNameSearch_noMatches_returnsEmptyList() {
        createMockEmployee("1", "Alice", 50000);
        List<Employee> result = service.getEmployeesByNameSearch("ZZZ");
        assertTrue(result.isEmpty());
    }

    @Test
    void getEmployeeById_existingId_returnsEmployee() {
        createMockEmployee("1", "Alice", 50000);
        Employee result = service.getEmployeeById("1");
        assertNotNull(result);
        assertEquals("Alice", result.employee_name());
    }

    @Test
    void getEmployeeById_missingId_returnsNull() {
        Employee result = service.getEmployeeById("999");
        assertNull(result);
    }

    @Test
    void getHighestSalaryOfEmployees_returnsMaxSalary() {
        createMockEmployee("1", "Alice", 50000);
        createMockEmployee("2", "Bob", 90000);
        createMockEmployee("3", "Charlie", 70000);
        Integer result = service.getHighestSalaryOfEmployees();
        assertEquals(90000, result);
    }

    @Test
    void getHighestSalaryOfEmployees_emptyTable_returnsNull() {
        Integer result = service.getHighestSalaryOfEmployees();
        assertNull(result);
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_returnsSortedTopTen() {
        for (int i = 1; i <= 15; i++) {
            createMockEmployee(String.valueOf(i), "Emp" + i, i * 1000);
        }
        List<String> result = service.getTopTenHighestEarningEmployeeNames();
        assertEquals(10, result.size());
        assertEquals("Emp15", result.get(0)); // highest salary
        assertEquals("Emp6", result.get(9)); // 10th highest
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_lessThanTen_returnsAllSorted() {
        createMockEmployee("1", "Alice", 50000);
        createMockEmployee("2", "Bob", 60000);
        List<String> result = service.getTopTenHighestEarningEmployeeNames();
        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0));
        assertEquals("Alice", result.get(1));
    }

    @Test
    void createEmployee_storesAndReturnsEmployee() {
        Employee created = service.createEmployee(new EmployeeCreationInput("Alice", 50000, 30,
                "Manager", "alice@mail.com"));
        assertNotNull(created);
        assertEquals("Alice", created.employee_name());
        assertEquals(created, mockEmployeeTable.get(created.id()));
    }

    @Test
    void deleteEmployeeById_existingId_returnsTrueAndRemoves() {
        createMockEmployee("1", "Alice", 50000);
        boolean result = service.deleteEmployeeById("1");
        assertTrue(result);
        assertFalse(mockEmployeeTable.containsKey("1"));
    }

    @Test
    void deleteEmployeeById_missingId_returnsTrueAndDoesNothing() {
        boolean result = service.deleteEmployeeById("999");
        assertTrue(result);
        assertTrue(mockEmployeeTable.isEmpty());
    }
}
