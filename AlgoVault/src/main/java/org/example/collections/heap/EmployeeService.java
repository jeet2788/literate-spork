package org.example.collections.heap;

import java.util.*;

public class EmployeeService {

    public List<Map.Entry<Integer, Integer>> topKSalaryByEmployee(List<Employee> employees, int k) {

        Map<Integer, Integer> map = new HashMap<>();

        // Aggregate salary by empId
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
