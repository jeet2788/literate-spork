package collections.array;

import org.example.collections.array.UniqueDigits;
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
}
