package com.example.demo.cal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void shouldAdd() {
        Integer a = 10;
        Integer b = 5;
        Integer expected = 15;

        assertEquals(expected,calculator.addNums(a,b),"");
    }

    @Test
    public void shouldThrowArithmeticException() {
        Integer a = null;
        Integer b = 15;

        assertThatThrownBy(() -> calculator.addNums(a, b)).isInstanceOf(ArithmeticException.class).hasMessage("can not add nulls");
        assertThrows(ArithmeticException.class, () -> calculator.addNums(a, b));

        Integer c = 20;
        Integer d = null;
        assertThatThrownBy(() -> calculator.addNums(a, b)).isInstanceOf(ArithmeticException.class).hasMessage("can not add nulls");
    }
}