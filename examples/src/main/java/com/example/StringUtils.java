package com.example;

/**
 * Utility class for common string operations.
 * Used as a dependency by CalculatorService.
 */
public class StringUtils {

    /**
     * Checks whether a string is a palindrome.
     *
     * @param text the string to check (must not be null)
     * @return true if the string reads the same forwards and backwards
     */
    public boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        String normalized = text.replaceAll("\\s", "").toLowerCase();
        int left = 0;
        int right = normalized.length() - 1;
        while (left < right) {
            if (normalized.charAt(left) != normalized.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Reverses the given string.
     *
     * @param text the string to reverse (must not be null)
     * @return the reversed string
     * @throws IllegalArgumentException if text is null
     */
    public String reverse(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text must not be null");
        }
        StringBuilder sb = new StringBuilder(text);
        return sb.reverse().toString();
    }

    /**
     * Counts the number of vowels (a, e, i, o, u) in the given string.
     *
     * @param text the string to analyze (must not be null)
     * @return the count of vowels (case-insensitive)
     */
    public int countVowels(String text) {
        if (text == null) {
            return 0;
        }
        int count = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks whether the string is null or empty.
     *
     * @param text the string to check
     * @return true if the string is null or has zero length
     */
    public boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}