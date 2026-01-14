package com.reliaquest.api.controller.impl;

import com.reliaquest.api.controller.IEmployeeController;
import com.reliaquest.api.controller.dto.EmployeeCreationInput;
import com.reliaquest.api.controller.exception.EmployeeValidationException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.IEmployeeService;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class EmployeeControllerImpl implements IEmployeeController<Employee, EmployeeCreationInput> {

    private final IEmployeeService employeeService;

    public EmployeeControllerImpl(IEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves all employees in the system.
     *
     * @return a {@link ResponseEntity} containing a list of all {@link Employee} objects,
     *         or an internal server error response if retrieval fails.
     */
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            return ResponseEntity.ok(employeeService.getAllEmployees());
        } catch (Exception e) {
            log.error("Failed to get all employees", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves employees whose names match the provided search string.
     *
     * @param searchString the text used to filter employees by name; must not be null or blank
     * @return a {@link ResponseEntity} containing a list of matching {@link Employee} objects,
     *         a bad request response if the search string is invalid,
     *         or an internal server error response if retrieval fails.
     */
    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        if (searchString == null || searchString.isBlank()) {
            log.error("Invalid search string for name search.");
            return ResponseEntity.badRequest().build();
        }

        try {
            List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
            if (employees.isEmpty()) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Failed to get employees by name", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves a single employee by their unique identifier.
     *
     * @param id the employee's ID; must not be null or blank
     * @return a {@link ResponseEntity} containing the matching {@link Employee},
     *         a bad request response if the ID is invalid,
     *         or an internal server error response if retrieval fails.
     */
    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        if (id == null || id.isBlank()) {
            log.error("Invalid employee id.");
            return ResponseEntity.badRequest().build();
        }

        try {
            Employee employee = employeeService.getEmployeeById(id);
            if (employee == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Failed to get employee by id", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves the highest salary among all employees.
     *
     * @return a {@link ResponseEntity} containing the highest salary value,
     *         or an internal server error response if retrieval fails.
     */
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            Integer salary = employeeService.getHighestSalaryOfEmployees();
            if (salary == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(salary);
        } catch (Exception e) {
            log.error("Failed to get highest employee salary", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves the names of the top ten highest-earning employees.
     *
     * @return a {@link ResponseEntity} containing a list of employee names,
     *         or an internal server error response if retrieval fails.
     */
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
        } catch (Exception e) {
            log.error("Failed to get top 10 highest earning employees", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Creates a new employee using the provided input data.
     *
     * @param input the data required to create a new employee; must pass validation
     * @return a {@link ResponseEntity} containing the created {@link Employee},
     *         a bad request response if validation fails,
     *         or an internal server error response if creation fails.
     */
    @Override
    public ResponseEntity<Employee> createEmployee(EmployeeCreationInput input) {
        try {
            validateEmployeeCreationInput(input);
        } catch (Exception e) {
            log.error("Invalid input to create employee - {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(employeeService.createEmployee(input));
        } catch (Exception e) {
            log.error("Failed to create new employee", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes an employee by their unique identifier.
     *
     * @param id the employee's ID; must not be null or blank
     * @return a {@link ResponseEntity} indicating the result:
     *         <ul>
     *             <li>OK if deletion succeeds</li>
     *             <li>Not Found if the employee does not exist</li>
     *             <li>Bad Request if the ID is invalid</li>
     *             <li>Internal Server Error if deletion fails</li>
     *         </ul>
     */
    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Invalid employee id.");
        }

        try {
            if (employeeService.getEmployeeById(id) == null) {
                log.warn("Employee with id {} doesn't exist.", id);
                return ResponseEntity.notFound().build();
            }
            if (employeeService.deleteEmployeeById(id)) {
                return ResponseEntity.ok("Successfully deleted employee with id " + id);
            } else {
                return ResponseEntity.internalServerError().body("Failed to delete employee with id " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete employee with id " + id);
        }
    }

    private void validateEmployeeCreationInput(EmployeeCreationInput input) throws EmployeeValidationException {
        if (input.salary() <= 0) {
            throw new EmployeeValidationException("Invalid salary");
        }
        if (input.age() < 16 || input.age() > 75) {
            throw new EmployeeValidationException("Age is out of valid range (min=16, max=75)");
        }
        if (!isEmail(input.email())) {
            throw new EmployeeValidationException("Invalid email address provided");
        }
    }

    private boolean isEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return Pattern.compile(
                        "^[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$")
                .matcher(email)
                .matches();
    }
}
