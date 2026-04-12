package collections.heap;

import org.example.collections.heap.Employee;
import org.example.collections.heap.EmployeeService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class EmployeeServiceTest {

    EmployeeService service = new EmployeeService();

    @Test
    void testTopKBasic() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, 5000, "IT"),
                new Employee(2, 6000, "HR"),
                new Employee(1, 7000, "IT"),
                new Employee(3, 4000, "Finance"),
                new Employee(2, 3000, "HR")
        );

        List<Map.Entry<Integer, Integer>> result = service.topKSalaryByEmployee(employees, 2);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getKey()); // highest
        assertEquals(12000, result.get(0).getValue());

        assertEquals(2, result.get(1).getKey());
        assertEquals(9000, result.get(1).getValue());
    }

    @Test
    void testSingleEmployee() {
        List<Employee> employees = List.of(
                new Employee(1, 5000, "IT")
        );

        List<Map.Entry<Integer, Integer>> result = service.topKSalaryByEmployee(employees, 1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getKey());
        assertEquals(5000, result.get(0).getValue());
    }

    @Test
    void testKGreaterThanUniqueEmployees() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, 5000, "IT"),
                new Employee(2, 6000, "HR")
        );

        List<Map.Entry<Integer, Integer>> result = service.topKSalaryByEmployee(employees, 5);

        assertEquals(2, result.size());
    }

    @Test
    void testEmptyInput() {
        List<Employee> employees = new ArrayList<>();

        List<Map.Entry<Integer, Integer>> result = service.topKSalaryByEmployee(employees, 2);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSameSalaryAggregation() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, 1000, "IT"),
                new Employee(1, 1000, "IT"),
                new Employee(2, 2000, "HR")
        );

        List<Map.Entry<Integer, Integer>> result = service.topKSalaryByEmployee(employees, 1);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getKey());
        assertEquals(2000, result.get(0).getValue());
    }
}

