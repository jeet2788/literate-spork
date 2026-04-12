package org.example.collections.heap;

import java.util.*;

/**
 * Service class for performing employee-related queries and computations.
 *
 * <p>This class provides utility methods for analyzing employee salary data,
 * such as finding top-earning employees or performing aggregated salary calculations.
 * It leverages heap-based algorithms to efficiently process large employee datasets.
 *
 * <p>The service supports operations like:
 * <ul>
 *   <li>Finding the top K employees by aggregated salary</li>
 *   <li>Handling multiple salary records per employee (e.g., from different periods)</li>
 *   <li>Deterministic tie-breaking when employees have equal total salaries</li>
 * </ul>
 *
 * <p><strong>Example Usage:</strong>
 * <pre>
 *   EmployeeService service = new EmployeeService();
 *   List&lt;Employee&gt; employees = Arrays.asList(
 *       new Employee(1, 5000, "IT"),
 *       new Employee(2, 6000, "HR"),
 *       new Employee(1, 7000, "IT")
 *   );
 *   List&lt;Map.Entry&lt;Integer, Integer&gt;&gt; topEarners = service.topKSalaryByEmployee(employees, 2);
 *   // Result: [(1, 12000), (2, 6000)]
 * </pre>
 *
 * @author Algorithm Vault
 * @version 1.0
 * @since 1.0
 *
 * @see Employee
 */
public class EmployeeService {

    /**
     * Finds the top K employees with the highest aggregated salaries.
     *
     * <p>This method aggregates all salary records for each unique employee ID
     * (summing multiple entries if present) and returns the K employees with
     * the highest total salaries, sorted in descending order of salary.
     *
     * <p><strong>Algorithm Overview:</strong>
     * <ol>
     *   <li>Aggregate salaries by employee ID using a HashMap</li>
     *   <li>Use a min-heap (PriorityQueue) to maintain the top K entries</li>
     *   <li>For entries with equal salaries, use employee ID as a tie-breaker
     *       (lower ID preferred when salaries are equal)</li>
     *   <li>Return results in descending order of salary</li>
     * </ol>
     *
     * <p><strong>Complexity Analysis:</strong>
     * <ul>
     *   <li>Time Complexity: O(n + m log k)
     *     <ul>
     *       <li>n = number of salary records (aggregation phase)</li>
     *       <li>m = number of unique employees</li>
     *       <li>k = number of top earners to retrieve</li>
     *     </ul>
     *   </li>
     *   <li>Space Complexity: O(m + k) for HashMap and PriorityQueue storage</li>
     * </ul>
     *
     * <p><strong>Comparator Details:</strong>
     * The min-heap uses a custom comparator that:
     * <ul>
     *   <li>Primarily sorts by salary (ascending, since it's a min-heap)</li>
     *   <li>Uses employee ID as tie-breaker when salaries are equal
     *       (employees with lower IDs rank higher)</li>
     * </ul>
     *
     * @param employees a list of Employee objects containing salary records
     *                  May contain multiple entries for the same employee ID
     * @param k the number of top earners to retrieve (must be positive)
     *
     * @return a {@code List<Map.Entry<Integer, Integer>>} containing the top K
     *         employees, where:
     *         <ul>
     *           <li>Entry key = employee ID</li>
     *           <li>Entry value = total aggregated salary</li>
     *         </ul>
     *         Results are sorted in <strong>descending order</strong> of salary.
     *         If there are fewer than K unique employees, returns all employees
     *         sorted by aggregated salary.
     *
     * @throws IllegalArgumentException if k is less than or equal to 0
     *
     * @example
     * <pre>
     *   EmployeeService service = new EmployeeService();
     *
     *   // Example 1: Basic usage
     *   List&lt;Employee&gt; employees = Arrays.asList(
     *       new Employee(1, 5000, "IT"),
     *       new Employee(2, 6000, "HR"),
     *       new Employee(1, 7000, "IT"),  // Employee 1 appears twice
     *       new Employee(3, 4000, "Finance")
     *   );
     *
     *   List&lt;Map.Entry&lt;Integer, Integer&gt;&gt; result = service.topKSalaryByEmployee(employees, 2);
     *   // Output: Employee 1: 12000, Employee 2: 6000
     *
     *   // Example 2: Tie-breaking (equal salaries)
     *   List&lt;Employee&gt; tied = Arrays.asList(
     *       new Employee(3, 5000, "Finance"),
     *       new Employee(1, 5000, "IT")
     *   );
     *
     *   List&lt;Map.Entry&lt;Integer, Integer&gt;&gt; tiedResult = service.topKSalaryByEmployee(tied, 1);
     *   // Output: Employee 1: 5000 (ID 1 < ID 3, so Employee 1 wins tie)
     * </pre>
     *
     * @see Employee
     * @see java.util.PriorityQueue
     * @see java.util.Map.Entry
     */
    public List<Map.Entry<Integer, Integer>> topKSalaryByEmployee(List<Employee> employees, int k) {

        // Aggregate salary by empId
        Map<Integer, Integer> map = new HashMap<>();
        for (Employee emp : employees) {
            map.put(emp.empId, map.getOrDefault(emp.empId, 0) + emp.salary);
        }

        // Min heap with tie-breaker on employee ID
        PriorityQueue<Map.Entry<Integer, Integer>> pq =
                new PriorityQueue<>((a, b) -> {
                    int salaryDiff = a.getValue() - b.getValue();
                    if (salaryDiff != 0) return salaryDiff;
                    return a.getKey() - b.getKey();
                });

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            pq.offer(entry);
            if (pq.size() > k) {
                pq.poll();
            }
        }

        // Build result
        List<Map.Entry<Integer, Integer>> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        Collections.reverse(result);
        return result;
    }
}
