package org.example.unittests;

import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SecureCalculator {

    private static Logger logger;
    private final SecureRandom secureRandom = new SecureRandom();
    private final boolean loggingEnabled;

    /**
     * Internal logging
     * @param message message to log
     */
    private void log(String message, Object... args){
        if (logger != null) {
            logger.info(String.format(message, args));
        }
    }


    /**
     * Create a new calculator with debug logging disabled
     */
    public SecureCalculator() {
        this(Logger.getLogger(SecureCalculator.class.getName()), false);
    }

    public SecureCalculator(Logger logger) {
        this(logger, true);
    }
    /**
     * Create a new calculator with debug logging enabled
     * @param logger Logger object

    public SecureCalculator(Logger logger) {
        this.logger = logger != null ? logger : Logger.getLogger(SecureCalculator.class.getName());
        this.logger.setLevel(java.util.logging.Level.ALL);
    } */

    private SecureCalculator(Logger logger, boolean loggingEnabled) {
        this.logger = (logger != null) ? logger : Logger.getLogger(SecureCalculator.class.getName());
        this.loggingEnabled = loggingEnabled;
        if (!loggingEnabled) {
            this.logger.setLevel(Level.OFF);
        }
    }



    /**
     * Safely multiply two integers so the result never overflows
     * @param a first number
     * @param b second number
     * @return multiplication result as long
     */
    public long multiply(int a, int b) {
        log("Multiply %s * %s", a, b);

        long result = (long) a * (long) b;

        if (a > 0 && b > 0 && result < 0) {
            throw new ArithmeticException(
                    "Overflow detectado: la multiplicación de dos positivos resultó negativa."
            );
        }

        log("Result: %s", result);

        return result;
    }


    /**
     * Safely divide two numbers using decimals as appropriate, throws exception if division by zero
     * @param a first number
     * @param b second number
     * @return division result as double
     */
    public double divide(double a, double b) {

        if (b == 0) {
            throw new ArithmeticException("Divide by zero is not allowed");
        }

        log("Divide %s / %s", a, b);
        double result =  a / b;
        log("Result: %s", result);
        return result;
    }


    /**
     * Safely do modulus between two numbers.
     * Example 5 mod 2 = 1
     * @param a first number
     * @param b second number
     * @return a mod b result, must be in range [0, b)
     */
    public int mod(int a, int b) {
        log("%s mod %s", a, b);

        int divisor = (b == Integer.MIN_VALUE) ? Integer.MIN_VALUE : (b < 0 ? -b : b);
        int result = a % divisor;
        log("Result: %s", result);
        return result;
    }


    /**
     * Safely detect if a number is odd
     * @param a number to test
     * @return true if number is odd (example 1,3,5) false if even (example 2,4,8)
     */
    public boolean isOdd(int a) {
        boolean result = (a & 1) != 0;
        log("isOdd(%d) -> %b", a, result);
        return result;
    }


    /**
     * Safely detect if a number is even
     * @param a number to test
     * @return true if number is even (example 2,4,8) false if odd (example 1,3,5)
     */
    public boolean isEven(int a) {
        boolean result = (a & 1) == 0;
        log("isEven(%d) -> %b", a, result);
        return result;
    }



    /**
     * Safely generate unique numbers
     * @return random number in range [0, MAX_VALUE)
     */
    public synchronized int getRandomNumber() {
        int result;
        result = secureRandom.nextInt(Integer.MAX_VALUE);

        log("Generated random number: %d", result);
        return result;
    }



    /**
     * Safely generate unique numbers, always less than bound
     * @return random number in range [0, bound)
     */
    public int getRandomNumber(int bound) {
        log("Generating rnd with bound %s", bound);

        int result = secureRandom.nextInt(bound);
        log("Generated random number with bound %d: %d", bound, result);
        return result;

    }

}
