package org.example.unittests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SecureCalculatorTests {


    @Test
    public void testGenerateRandomInt_LargeBound() {
        int bound = 1_000_000;
        SecureCalculator calc = new SecureCalculator();
        for (int i = 0; i < 100; i++) {
            int randomValue = calc.getRandomNumber(bound);
            Assertions.assertTrue(randomValue >= 0 && randomValue < bound,
                    "El número aleatorio debe estar en [0, " + bound + ")");
        }
    }


    @Test
    public void testIsEven_EdgeCases() {
        SecureCalculator calc = new SecureCalculator();
        Assertions.assertFalse(calc.isEven(Integer.MAX_VALUE));
        Assertions.assertTrue(calc.isEven(Integer.MIN_VALUE));
    }

    @Test
    public void testIsOdd_EdgeCases() {
        SecureCalculator calc = new SecureCalculator();
        Assertions.assertTrue(calc.isOdd(Integer.MAX_VALUE));
        Assertions.assertFalse(calc.isOdd(Integer.MIN_VALUE));
    }

    @Test
    public void testMod_PositiveNumbers() {
        // 5 mod 2 debe ser 1
        SecureCalculator calc = new SecureCalculator();
        int result = calc.mod(5, 2);
        Assertions.assertEquals(1, result);

        // 10 mod 3 debe ser 1 (10 % 3 = 1)
        result = calc.mod(10, 3);
        Assertions.assertEquals(1, result);
    }


    @Test
    public void testMod_ZeroDividend() {
        SecureCalculator calc = new SecureCalculator();
        int result = calc.mod(0, 5);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testModWithNegativeDivisor() {
        SecureCalculator calc = new SecureCalculator();
        int resultado = calc.mod(5, -3);
        Assertions.assertEquals( 2, resultado);
    }
    @Test
    public void testIsOdd_IntegerMinValue() {
        SecureCalculator calc = new SecureCalculator();

        boolean result = calc.isOdd(Integer.MIN_VALUE);
        Assertions.assertFalse(result, "Integer.MIN_VALUE debería ser par, isOdd devolvió true");
    }
    @Test
    public void testGetRandomNumberWithinBounds() {
        SecureCalculator calc = new SecureCalculator();
        int bound = 10;
        int result = calc.getRandomNumber(bound);
        Assertions.assertTrue(result >= 0 && result < bound, "Random number out of bounds");
    }

    @Test
    public void testDivideIntegerMinValueAndMinusOne() {
        SecureCalculator calc = new SecureCalculator();
        double resultado = calc.divide((double) Integer.MIN_VALUE, -1.0);
        Assertions.assertEquals( 2147483648.0, resultado, 0.0);
    }

    @Test
    public void testMod_BoundaryCase() {
        SecureCalculator calc = new SecureCalculator();
        int result = calc.mod(14, 7);
        Assertions.assertEquals(0, result);

        result = calc.mod(-14, 7);
        Assertions.assertEquals(0, result);
    }
    @Test
    public void testGenerateUniqueNumber_Range() {
        SecureCalculator calc = new SecureCalculator();
        int unique = calc.getRandomNumber();
        Assertions.assertTrue(unique >= 0 && unique < Integer.MAX_VALUE,
                "El número único debe estar en el rango [0, Integer.MAX_VALUE)");
    }

    @Test
    public void testGenerateUniqueNumber_Uniqueness() {
        SecureCalculator calc = new SecureCalculator();
        int iterations = 10000;
        Set<Integer> generated = new HashSet<>();
        for (int i = 0; i < iterations; i++) {
            int num = calc.getRandomNumber();
            Assertions.assertFalse(generated.contains(num), "Se encontró un número único duplicado: " + num);
            generated.add(num);
        }
    }

    @Test
    public void testGenerateUniqueNumber_Exhaustion() {
        SecureCalculator calcExhaust = new SecureCalculator() {
            @Override
            public int getRandomNumber() {
                throw new IllegalStateException("Unique numbers exhausted");
            }
        };
        Assertions.assertThrows(IllegalStateException.class, () -> calcExhaust.getRandomNumber());
    }

    @Test
    public void testDefaultConstructorLoggerLevel() {
        SecureCalculator calc = new SecureCalculator();

        Logger logger = Logger.getLogger(SecureCalculator.class.getName());
        Assertions.assertEquals(Level.OFF, logger.getLevel(), "El logger por defecto debe estar en OFF");
    }

    @Test
    public void testLoggingIsInvokedOnModOperation() {
        Logger mockLogger = mock(Logger.class);
        when(mockLogger.getLevel()).thenReturn(Level.ALL);
        SecureCalculator calc = new SecureCalculator(mockLogger);
        calc.mod(5, 2);
        verify(mockLogger, atLeastOnce()).info(contains("mod"));
    }


    @Test
    public void testGetRandomNumberBoundValidation() {
        SecureCalculator calc = new SecureCalculator();
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.getRandomNumber(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.getRandomNumber(-5));
    }

    @Test
    public void testGetRandomNumberUniqueness() {
        SecureCalculator calc = new SecureCalculator();
        Set<Integer> numbers = new HashSet<>();
        int iterations = 10000;
        for (int i = 0; i < iterations; i++) {
            int num = calc.getRandomNumber();
            Assertions.assertFalse(numbers.contains(num), "Número duplicado encontrado: " + num);
            numbers.add(num);
        }
    }
}


