package code.collection;

import java.util.*;

public class UniqueDigits {

    public static void main(String[] args) {
        UniqueDigits obj = new UniqueDigits();

        int low = 10;
        int high = 50;

        List<Integer> result = obj.uniqueDigitNumbers(low, high);

        System.out.println("Numbers with unique digits between " + low + " and " + high + ":");
        System.out.println(result);
    }

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
        boolean[] seen = new boolean[10]; // This is because numbers can be oly from 0-9

        while (num > 0) {
            int digit = num % 10;

            if (seen[digit]) return false;

            seen[digit] = true;
            num /= 10;
        }
        return true;
    }
}
