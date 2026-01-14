package com.reliaquest.api.service;

import com.reliaquest.api.controller.dto.EmployeeCreationInput;
import com.reliaquest.api.model.Employee;
import java.util.List;

public interface IEmployeeService { // TODO: add documentation
    /**
     * Retrieves all employees stored in the mock employee table.
     *
     * @return a list containing all {@link Employee} objects currently stored.
     */
    List<Employee> getAllEmployees();

    /**
     * Retrieves all employees whose names contain the given search string.
     *
     * @param searchString the substring to match against employee names; must not be null
     * @return a list of {@link Employee} objects whose names contain the provided search text.
     */
    List<Employee> getEmployeesByNameSearch(String searchString);

    /**
     * Retrieves a single employee by their unique identifier.
     *
     * @param id the employee's ID
     * @return the matching {@link Employee}, or {@code null} if no employee exists with the given ID.
     */
    Employee getEmployeeById(String id);

    /**
     * Determines the highest salary among all employees.
     *
     * @return the highest salary value, or {@code null} if no employees exist.
     */
    Integer getHighestSalaryOfEmployees();

    /**
     * Retrieves the names of the top ten highest‑earning employees.
     *
     * @return a list of employee names sorted by salary in descending order,
     *         limited to the top ten results.
     */
    List<String> getTopTenHighestEarningEmployeeNames();

    /**
     * Creates a new employee using the provided input data and stores it in the mock table.
     *
     * @param employeeInput the input data required to construct a new {@link Employee}
     * @return the newly created {@link Employee} instance.
     */
    Employee createEmployee(EmployeeCreationInput employeeInput);

    /**
     * Deletes an employee with the given identifier.
     *
     * @param id the employee's ID
     * @return {@code true} if the employee was successfully removed or did not exist,
     *         {@code false} if the deletion failed.
     */
    boolean deleteEmployeeById(String id);
}
