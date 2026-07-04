package com.example;

/**
 * Service layer that combines Calculator and StringUtils operations.
 * Demonstrates dependency resolution between classes in the same package.
 */
public class CalculatorService {

    private final Calculator calculator;
    private final StringUtils stringUtils;
    private int operationCount;

    /**
     * Constructs a CalculatorService with the default instances.
     */
    public CalculatorService() {
        this.calculator = new Calculator();
        this.stringUtils = new StringUtils();
        this.operationCount = 0;
    }

    /**
     * Adds two numbers and formats the result as a decorated string.
     *
     * @param a first operand
     * @param b second operand
     * @return a string like "Result: <sum>"
     */
    public String addAndFormat(int a, int b) {
        int sum = calculator.add(a, b);
        operationCount++;
        return "Result: " + sum;
    }

    /**
     * Reverses the sum of two integers and returns the reversed string.
     *
     * @param a first operand
     * @param b second operand
     * @return the reversed string representation of the sum
     */
    public String reverseSum(int a, int b) {
        int sum = calculator.add(a, b);
        operationCount++;
        return stringUtils.reverse(String.valueOf(sum));
    }

    /**
     * Checks whether the concatenation of two strings is a palindrome.
     *
     * @param first  the first string
     * @param second the second string
     * @return true if first + second is a palindrome
     */
    public boolean isConcatenatedPalindrome(String first, String second) {
        String combined = first + second;
        operationCount++;
        return stringUtils.isPalindrome(combined);
    }

    /**
     * Divides two numbers and counts the vowels in the string representation
     * of the result.
     *
     * @param dividend the number to divide
     * @param divisor  the divisor
     * @return the number of vowels in the result string
     */
    public int divideAndCountVowels(double dividend, double divisor) {
        double result = calculator.divide(dividend, divisor);
        operationCount++;
        return stringUtils.countVowels(String.valueOf(result));
    }

    /**
     * Returns the total number of operations performed by this service.
     *
     * @return the operation count
     */
    public int getOperationCount() {
        return operationCount;
    }

    /**
     * Prints a summary of the service state.
     */
    public void printSummary() {
        System.out.println("Total operations: " + operationCount);
    }
}