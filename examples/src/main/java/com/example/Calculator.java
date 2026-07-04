package com.example;

/**
 * Simple calculator with basic arithmetic operations.
 * Used as a dependency by CalculatorService.
 */
public class Calculator {

    /**
     * Adds two integers.
     *
     * @param a first operand
     * @param b second operand
     * @return the sum of a and b
     */
    public int add(int a, int b) {
        return a + b;
    }

    /**
     * Subtracts the second integer from the first.
     *
     * @param a the minuend
     * @param b the subtrahend
     * @return the result of a - b
     */
    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Multiplies two integers.
     *
     * @param a first factor
     * @param b second factor
     * @return the product of a and b
     */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Divides the first integer by the second.
     *
     * @param dividend the number to be divided
     * @param divisor  the number to divide by
     * @return the quotient of dividend / divisor
     * @throws ArithmeticException if divisor is zero
     */
    public double divide(double dividend, double divisor) {
        if (divisor == 0.0) {
            throw new ArithmeticException("Division by zero");
        }
        return dividend / divisor;
    }
}