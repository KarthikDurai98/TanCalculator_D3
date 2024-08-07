package com.example.tangentCalc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TanTest {

    @Test
    void testComputeTangentAtZero() {
        double angle = 0.0;
        double expected = 0.0;
        double result = TrigUtil.computeTangent(angle);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testComputeTangentAtPiOverFour() {
        double angle = Math.PI / 4;
        double expected = 1.0;
        double result = TrigUtil.computeTangent(angle);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testComputeTangentUndefined() {
        double angle = Math.PI / 2;
        assertTrue(Double.isNaN(TrigUtil.computeTangent(angle)));
    }

    @Test
    void testComputeTangentAtNegativePiOverFour() {
        double angle = -Math.PI / 4;
        double expected = -1.0;
        double result = TrigUtil.computeTangent(angle);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testComputeTangentAtPi() {
        double angle = Math.PI;
        double expected = 0.0;
        double result = TrigUtil.computeTangent(angle);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testComputeTangentAtThreePiOverFour() {
        double angle = 3 * Math.PI / 4;
        double expected = -1.0;
        double result = TrigUtil.computeTangent(angle);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testComputeTangentAtNegativePi() {
        double angle = -Math.PI;
        double expected = 0.0;
        double result = TrigUtil.computeTangent(angle);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testParseDoubleValidNumber() {
        String number = "123.456";
        double expected = 123.456;
        double result = TrigUtil.parseDouble(number);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testParseDoubleNegativeNumber() {
        String number = "-789.012";
        double expected = -789.012;
        double result = TrigUtil.parseDouble(number);
        assertEquals(expected, result, 1e-15);
    }

    @Test
    void testParseDoubleInvalidNumber() {
        String number = "123.45.6";
        assertThrows(NumberFormatException.class, () -> TrigUtil.parseDouble(number));
    }

    @Test
    void testParseDoubleWithInvalidCharacter() {
        String number = "12a34";
        assertThrows(NumberFormatException.class, () -> TrigUtil.parseDouble(number));
    }
}
