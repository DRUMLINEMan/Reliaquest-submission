package com.reliaquest.api.controller.impl;

import com.reliaquest.api.controller.dto.EmployeeCreationInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerImplTest {

    @Mock
    private IEmployeeService employeeService;

    private EmployeeControllerImpl controller;

    @BeforeEach
    void setUp() {
        controller = new EmployeeControllerImpl(employeeService);
    }

    @Test
    void getAllEmployees_returnsOkWithList() {
        List<Employee> employees = List.of(mock(Employee.class));
        when(employeeService.getAllEmployees()).thenReturn(employees);
        ResponseEntity<List<Employee>> response = controller.getAllEmployees();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(employees, response.getBody());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void getAllEmployees_whenServiceThrows_returns500() {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("boom"));
        ResponseEntity<List<Employee>> response = controller.getAllEmployees();
        assertTrue(response.getStatusCode().is5xxServerError());
        assertNull(response.getBody());
    }

    @Test
    void getEmployeesByNameSearch_validInput_returnsOk() {
        List<Employee> employees = List.of(mock(Employee.class));
        doReturn(employees).when(employeeService).getEmployeesByNameSearch("john");
        ResponseEntity<List<Employee>> response = controller.getEmployeesByNameSearch("john");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(employees, response.getBody());
        verify(employeeService).getEmployeesByNameSearch("john");
    }

    @Test
    void getEmployeesByNameSearch_validInput_returnsNotFound() {
        when(employeeService.getEmployeesByNameSearch("john")).thenReturn(new ArrayList<>());
        ResponseEntity<List<Employee>> response = controller.getEmployeesByNameSearch("john");
        assertEquals(404, response.getStatusCode().value());
        verify(employeeService).getEmployeesByNameSearch("john");
    }

    @Test
    void getEmployeesByNameSearch_blankInput_returnsBadRequest() {
        ResponseEntity<List<Employee>> response = controller.getEmployeesByNameSearch(" ");
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verifyNoInteractions(employeeService);
    }

    @Test
    void getEmployeesByNameSearch_whenServiceThrows_returns500() {
        when(employeeService.getEmployeesByNameSearch("john")).thenThrow(new RuntimeException("boom"));
        ResponseEntity<List<Employee>> response = controller.getEmployeesByNameSearch("john");
        assertTrue(response.getStatusCode().is5xxServerError());
        assertNull(response.getBody());
    }

    @Test
    void getEmployeeById_validId_returnsOk() {
        Employee employee = mock(Employee.class);
        when(employeeService.getEmployeeById("123")).thenReturn(employee);
        ResponseEntity<Employee> response = controller.getEmployeeById("123");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(employee, response.getBody());
        verify(employeeService).getEmployeeById("123");
    }

    @Test
    void getEmployeeById_validId_returnsNotFound() {
        when(employeeService.getEmployeeById("123")).thenReturn(null);
        ResponseEntity<Employee> response = controller.getEmployeeById("123");
        assertEquals(404, response.getStatusCode().value());
        verify(employeeService).getEmployeeById("123");
    }

    @Test
    void getEmployeeById_blankId_returnsBadRequest() {
        ResponseEntity<Employee> response = controller.getEmployeeById("");
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verifyNoInteractions(employeeService);
    }

    @Test
    void getEmployeeById_whenServiceThrows_returns500() {
        when(employeeService.getEmployeeById("123")).thenThrow(new RuntimeException());
        ResponseEntity<Employee> response = controller.getEmployeeById("123");
        assertTrue(response.getStatusCode().is5xxServerError());
        assertNull(response.getBody());
    }

    @Test
    void getHighestSalary_returnsOk() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(90000);
        ResponseEntity<Integer> response = controller.getHighestSalaryOfEmployees();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(90000, response.getBody());
    }

    @Test
    void getHighestSalary_returnsNotFound() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(null);
        ResponseEntity<Integer> response = controller.getHighestSalaryOfEmployees();
        assertEquals(404,response.getStatusCode().value());
    }

    @Test
    void getHighestSalary_whenServiceThrows_returns500() {
        when(employeeService.getHighestSalaryOfEmployees()).thenThrow(new RuntimeException());
        ResponseEntity<Integer> response = controller.getHighestSalaryOfEmployees();
        assertTrue(response.getStatusCode().is5xxServerError());
        assertNull(response.getBody());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_returnsOk() {
        List<String> names = List.of("Alice", "Bob");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(names);
        ResponseEntity<List<String>> response = controller.getTopTenHighestEarningEmployeeNames();
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(names, response.getBody());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_whenServiceThrows_returns500() {
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenThrow(new RuntimeException());
        ResponseEntity<List<String>> response = controller.getTopTenHighestEarningEmployeeNames();
        assertTrue(response.getStatusCode().is5xxServerError());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_validInput_returnsOk() {
        EmployeeCreationInput input = new EmployeeCreationInput("John", 90000, 35, "Manager", "test@gmail.com");
        Employee employee = mock(Employee.class);
        when(employeeService.createEmployee(input)).thenReturn(employee);
        ResponseEntity<Employee> response = controller.createEmployee(input);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(employee, response.getBody());
    }

    @Test
    void createEmployee_invalidSalary_returnsBadRequest() {
        EmployeeCreationInput input = new EmployeeCreationInput("John", 0, 35, "Manager", "test@gmail.com");
        ResponseEntity<Employee> response = controller.createEmployee(input);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verifyNoInteractions(employeeService);
    }

    @Test
    void createEmployee_invalidAge_returnsBadRequest() {
        EmployeeCreationInput input = new EmployeeCreationInput("John", 90000, 5, "Manager", "test@gmail.com");
        ResponseEntity<Employee> response = controller.createEmployee(input);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verifyNoInteractions(employeeService);

        EmployeeCreationInput input2 = new EmployeeCreationInput("John", 90000, 105, "Manager", "test@gmail.com");
        response = controller.createEmployee(input2);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verifyNoInteractions(employeeService);
    }

    @Test
    void createEmployee_invalidEmail_returnsBadRequest() {
        EmployeeCreationInput input = new EmployeeCreationInput("John", 90000, 35, "Manager", ".test@gmail.com");
        ResponseEntity<Employee> response = controller.createEmployee(input);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
        verifyNoInteractions(employeeService);
    }

    @Test
    void createEmployee_serviceThrows_returns500() {
        EmployeeCreationInput input = mock(EmployeeCreationInput.class);
        when(input.salary()).thenReturn(50000);
        when(input.age()).thenReturn(30);
        when(input.email()).thenReturn("test@example.com");
        when(employeeService.createEmployee(input)).thenThrow(new RuntimeException());
        ResponseEntity<Employee> response = controller.createEmployee(input);
        assertTrue(response.getStatusCode().is5xxServerError());
        assertNull(response.getBody());
    }

    @Test
    void deleteEmployeeById_validId_employeeExists_returnsOk() {
        when(employeeService.getEmployeeById("123")).thenReturn(mock(Employee.class));
        when(employeeService.deleteEmployeeById("123")).thenReturn(true);
        ResponseEntity<String> response = controller.deleteEmployeeById("123");
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("Successfully deleted employee with id 123", response.getBody());
    }

    @Test
    void deleteEmployeeById_blankId_returnsBadRequest() {
        ResponseEntity<String> response = controller.deleteEmployeeById("");
        assertTrue(response.getStatusCode().is4xxClientError());
        assertEquals("Invalid employee id.", response.getBody());
        verifyNoInteractions(employeeService);
    }

    @Test
    void deleteEmployeeById_employeeNotFound_returnsNotFound() {
        when(employeeService.getEmployeeById("123")).thenReturn(null);
        ResponseEntity<String> response = controller.deleteEmployeeById("123");
        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void deleteEmployeeById_deleteFails_returns500() {
        when(employeeService.getEmployeeById("123")).thenReturn(mock(Employee.class));
        when(employeeService.deleteEmployeeById("123")).thenReturn(false);
        ResponseEntity<String> response = controller.deleteEmployeeById("123");
        assertTrue(response.getStatusCode().is5xxServerError());
        assertEquals("Failed to delete employee with id 123", response.getBody());
    }

    @Test
    void deleteEmployeeById_serviceThrows_returns500() {
        when(employeeService.getEmployeeById("123")).thenThrow(new RuntimeException());
        ResponseEntity<String> response = controller.deleteEmployeeById("123");
        assertTrue(response.getStatusCode().is5xxServerError());
        assertEquals("Failed to delete employee with id 123", response.getBody());
    }
}
