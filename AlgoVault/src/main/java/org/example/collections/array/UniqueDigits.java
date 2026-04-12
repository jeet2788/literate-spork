package org.example.collections.array;

import java.util.ArrayList;
import java.util.List;

public class UniqueDigits {
    public List<Integer> uniqueDigitNumbers(int low, int high) {
        List<Integer> result = new ArrayList<>();

        for (int num = low; num <= high; num++) {
            if (hasUniqueDigits(num)) {
                result.add(num);
            }
        }
        return result;
    }

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
