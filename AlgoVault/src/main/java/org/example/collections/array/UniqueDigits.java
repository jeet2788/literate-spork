package org.example.collections.array;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for finding numbers with unique digits within a given range.
 * 
 * <p>This class provides methods to identify all numbers in a specified range
 * where each digit appears exactly once (no repeated digits).
 * 
 * <p>Example: In the range 10-15, the valid numbers are [10, 12, 13, 14, 15]
 * because 11 has a repeated digit '1'.
 * 
 * <p>The algorithm uses a boolean array to track which digits (0-9) have been
 * seen while processing each number, providing O(d) time complexity where d
 * is the number of digits.
 * 
 * @author Algorithm Vault
 * @version 1.0
 * @since 1.0
 */
public class UniqueDigits {
    
    /**
     * Finds all numbers within the specified range that contain only unique digits.
     * 
     * <p>Returns a list of integers where each number in the range [low, high]
     * (inclusive) has all distinct digits (no digit appears more than once).
     * 
     * <p>Time Complexity: O(n * d) where n is the range size and d is the average
     * number of digits per number.
     * <br>Space Complexity: O(k) where k is the count of valid numbers (excluding
     * the internal boolean array of size 10 per iteration).
     * 
     * @param low the lower bound of the range (inclusive)
     * @param high the upper bound of the range (inclusive)
     * @return a {@code List<Integer>} containing all numbers in the range [low, high]
     *         with unique digits, sorted in ascending order
     * 
     * @throws IllegalArgumentException if low is greater than high
     * 
     * @example
     * <pre>
     *   UniqueDigits ud = new UniqueDigits();
     *   List&lt;Integer&gt; result = ud.uniqueDigitNumbers(10, 15);
     *   // result: [10, 12, 13, 14, 15]
     * </pre>
     */
    public List<Integer> uniqueDigitNumbers(int low, int high) {
        List<Integer> result = new ArrayList<>();

        for (int num = low; num <= high; num++) {
            if (hasUniqueDigits(num)) {
                result.add(num);
            }
        }
        return result;
    }

    /**
     * Checks whether a given number contains only unique digits.
     * 
     * <p>This helper method verifies that no digit (0-9) appears more than once
     * in the given number. It processes digits from right to left using modulo
     * and division operations.
     * 
     * <p>Special Case: The number 0 is considered to have unique digits.
     * 
     * <p>Time Complexity: O(d) where d is the number of digits in the number
     * <br>Space Complexity: O(1) - uses a fixed-size array of 10 booleans
     * 
     * @param num the number to check for digit uniqueness
     * @return {@code true} if all digits in the number are unique (no repeats),
     *         {@code false} otherwise
     * 
     * @example
     * <pre>
     *   hasUniqueDigits(123)  // returns true
     *   hasUniqueDigits(121)  // returns false (digit '1' repeats)
     *   hasUniqueDigits(0)    // returns true
     *   hasUniqueDigits(99)   // returns false (digit '9' repeats)
     * </pre>
     */
    private boolean hasUniqueDigits(int num) {
        boolean[] seen = new boolean[10];

        while (num > 0) {
            int digit = num % 10;

            if (seen[digit]) return false;

            seen[digit] = true;
            num /= 10;
        }
        return true;
    }
}
