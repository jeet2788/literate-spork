package org.example.collections.heap;

/**
 * Represents an employee with basic employment information.
 *
 * <p>This class encapsulates employee data including unique identifier,
 * salary information, and department assignment. It is commonly used in
 * collection-based algorithms such as finding top-earning employees
 * or aggregating salaries by employee ID.
 *
 * <p>The Employee class serves as a data model for managing employee records
 * and facilitating salary calculations and hierarchical queries.
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 *   Employee emp1 = new Employee(1, 5000, "IT");
 *   Employee emp2 = new Employee(2, 6000, "HR");
 *   System.out.println("Employee " + emp1.empId + " earns $" + emp1.salary);
 * </pre>
 *
 * @author Algorithm Vault
 * @version 1.0
 * @since 1.0
 *
 * @see org.example.collections.heap.EmployeeService
 */
public class Employee {

    /**
     * The unique identifier for this employee.
     *
     * <p>This ID uniquely identifies an employee within the organization.
     * Multiple salary records can exist for the same employee ID (e.g., when
     * an employee has multiple salary entries or transfers between departments).
     *
     * @see #empDept
     */
    int empId;

    /**
     * The salary of the employee in the current period or position.
     *
     * <p>Represents the monetary compensation for the employee. When used in
     * aggregate operations (such as top-K salary queries), multiple salary
     * entries for the same employee ID can be summed.
     *
     * <p>Value is typically in currency units (e.g., USD) and should be non-negative.
     */
    int salary;

    /**
     * The department to which the employee is assigned.
     *
     * <p>Identifies the organizational unit or functional area to which the
     * employee belongs. Common values include "IT", "HR", "Finance", "Sales", etc.
     */
    String empDept;

    /**
     * Constructs an Employee with the specified ID, salary, and department.
     *
     * <p>Creates a new Employee object initialized with the provided values.
     * All parameters are stored as-is without validation or modification.
     *
     * @param empId the unique employee identifier
     *              Must be positive and unique within the organization
     * @param salary the employee's salary amount
     *               Should be a non-negative integer representing monetary compensation
     * @param empDept the department name or code
     *                (e.g., "IT", "HR", "Finance", "Sales")
     *
     * @example
     * <pre>
     *   Employee emp = new Employee(101, 75000, "Engineering");
     *   // Creates an employee with ID 101, earning 75,000, in Engineering dept
     * </pre>
     *
     * @see EmployeeService#topKSalaryByEmployee(java.util.List, int)
     */
    public Employee(int empId, int salary, String empDept) {
        this.empId = empId;
        this.salary = salary;
        this.empDept = empDept;
    }
}
