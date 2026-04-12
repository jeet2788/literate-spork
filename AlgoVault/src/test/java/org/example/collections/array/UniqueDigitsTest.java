package org.example.collections.array;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class UniqueDigitsTest {

    UniqueDigits obj = new UniqueDigits();

    @Test
    void testBasicRange() {
        List<Integer> result = obj.uniqueDigitNumbers(10, 15);
        assertEquals(Arrays.asList(10, 12, 13, 14, 15), result);
    }

    @Test
    void testSingleDigitRange() {
        List<Integer> result = obj.uniqueDigitNumbers(1, 9);
        assertEquals(Arrays.asList(1,2,3,4,5,6,7,8,9), result);
    }

    @Test
    void testWithDuplicates() {
        List<Integer> result = obj.uniqueDigitNumbers(20, 25);
        assertEquals(Arrays.asList(20,21,23,24,25), result); // 22 excluded
    }

    @Test
    void testZeroIncluded() {
        List<Integer> result = obj.uniqueDigitNumbers(0, 5);
        assertEquals(Arrays.asList(0,1,2,3,4,5), result);
    }

    @Test
    void testSameLowHigh() {
        List<Integer> result = obj.uniqueDigitNumbers(11, 11);
        assertEquals(Collections.emptyList(), result); // 11 has duplicate
    }

    @Test
    void testLargeRangeSample() {
        List<Integer> result = obj.uniqueDigitNumbers(98, 102);
        assertEquals(Arrays.asList(98,102), result); // 99,100,101 excluded
    }

    // Additional comprehensive test cases

    @Test
    void testAllDuplicates() {
        List<Integer> result = obj.uniqueDigitNumbers(33, 44);
        // 33, 44 have duplicates; 34-43 should be checked
        assertEquals(Arrays.asList(34, 35, 36, 37, 38, 39, 40, 41, 42, 43), result);
    }

    @Test
    void testThreeDigitNumbers() {
        List<Integer> result = obj.uniqueDigitNumbers(100, 110);
        // 100, 101, 110 have duplicates
        // 102, 103, 104, 105, 106, 107, 108, 109 are valid
        assertEquals(Arrays.asList(102, 103, 104, 105, 106, 107, 108, 109), result);
    }

    @Test
    void testNumberWithAllDigitsDuplicated() {
        List<Integer> result = obj.uniqueDigitNumbers(111, 111);
        assertEquals(Collections.emptyList(), result); // 111 has repeated digit
    }

    @Test
    void testNumbersWithRepeatedDigitsExcluded() {
        List<Integer> result = obj.uniqueDigitNumbers(88, 92);
        // 88(duplicate 8), 89(unique), 90(unique), 91(unique), 92(unique)
        assertEquals(Arrays.asList(89, 90, 91, 92), result);
    }

    @Test
    void testSingleNumber() {
        List<Integer> result = obj.uniqueDigitNumbers(42, 42);
        assertEquals(Arrays.asList(42), result);
    }

    @Test
    void testSingleNumberWithDuplicate() {
        List<Integer> result = obj.uniqueDigitNumbers(77, 77);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testZeroToNine() {
        List<Integer> result = obj.uniqueDigitNumbers(0, 9);
        assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), result);
    }

    @Test
    void testTwoDigitNumbersStarting() {
        List<Integer> result = obj.uniqueDigitNumbers(10, 19);
        // 11 excluded (duplicate 1)
        assertEquals(Arrays.asList(10, 12, 13, 14, 15, 16, 17, 18, 19), result);
    }

    @Test
    void testMultipleRepeatedDigitsRange() {
        List<Integer> result = obj.uniqueDigitNumbers(121, 125);
        // 121 (repeated 1), 122 (repeated 2), 123 (unique), 124 (unique), 125 (unique)
        assertEquals(Arrays.asList(123, 124, 125), result);
    }

    @Test
    void testEdgeCaseWithNine() {
        List<Integer> result = obj.uniqueDigitNumbers(98, 99);
        // 98 (unique), 99 (duplicate 9)
        assertEquals(Arrays.asList(98), result);
    }

    @Test
    void testLargerRange() {
        List<Integer> result = obj.uniqueDigitNumbers(1234, 1240);
        // All have unique digits
        assertEquals(Arrays.asList(1234, 1235, 1236, 1237, 1238, 1239, 1240), result);
    }

    @Test
    void testNumbersWithDuplicateAtDifferentPositions() {
        List<Integer> result = obj.uniqueDigitNumbers(101, 103);
        // 101 (repeated 1), 102 (unique), 103 (unique)
        assertEquals(Arrays.asList(102, 103), result);
    }

    @Test
    void testResultIsNotEmpty() {
        List<Integer> result = obj.uniqueDigitNumbers(1, 100);
        assertFalse(result.isEmpty());
        assertTrue(result.size() > 0);
    }

    @Test
    void testResultSize() {
        List<Integer> result = obj.uniqueDigitNumbers(1, 9);
        assertEquals(9, result.size());
    }

    @Test
    void testOrderingIsCorrect() {
        List<Integer> result = obj.uniqueDigitNumbers(10, 20);
        // Verify results are in ascending order
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) < result.get(i + 1));
        }
    }

    @Test
    void testNoNegativeNumbers() {
        List<Integer> result = obj.uniqueDigitNumbers(1, 100);
        for (Integer num : result) {
            assertTrue(num >= 0);
        }
    }
}

