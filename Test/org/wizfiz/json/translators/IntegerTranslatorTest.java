package org.wizfiz.json.translators;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerTranslatorTest {
    IntegerTranslator testObj = new IntegerTranslator();

    @Test
    public void isApplicableReturnsTrueIfInteger() {
        assertTrue(testObj.isApplicable("10"));
    }

    @Test
    public void isApplicableReturnsFalseIfNotInteger() {
        assertFalse(testObj.isApplicable("10.10"));
        assertFalse(testObj.isApplicable("a"));
    }

    @Test
    public void translateReturnsCorrectValue() {
        assertEquals(10, testObj.translate("10"));
    }
}