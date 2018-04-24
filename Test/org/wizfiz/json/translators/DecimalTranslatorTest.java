package org.wizfiz.json.translators;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class DecimalTranslatorTest {
    public static final String DECIMAL_VALUE = "10.10";
    DecimalTranslator testObj = new DecimalTranslator();

    @Test
    public void isApplicableWillReturnTrueIfDecimal() {
        assertTrue(testObj.isApplicable("11.01"));
        assertTrue(testObj.isApplicable("0.01"));
    }

    @Test
    public void isApplicableWillReturnFalseIfNotDecimal() {
        assertFalse(testObj.isApplicable("10"));
        assertFalse(testObj.isApplicable("1"));
        assertFalse(testObj.isApplicable("a"));
    }

    @Test
    public void translateWillReturnDecimalValue() {
        assertEquals(new BigDecimal(DECIMAL_VALUE), testObj.translate(DECIMAL_VALUE));
    }
}