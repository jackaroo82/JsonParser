package org.wizfiz.json.translators;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringTranslatorTest {
    StringTranslator testObj = new StringTranslator();

    @Test
    public void isApplicableReturnsTrueIfStartsAndEndsWithSingleQuote() {
        assertTrue(testObj.isApplicable("'one'"));
    }

    @Test
    public void isApplicableReturnsTrueIfStartsAndEndsWithDoubleQuote() {
        assertTrue(testObj.isApplicable("\"one\""));
    }

    @Test
    public void isApplicableReturnsFalseIfDoesntStartAndEndWithSameQuotes() {
        assertFalse(testObj.isApplicable("'one\""));
        assertFalse(testObj.isApplicable("\"one'"));
        assertFalse(testObj.isApplicable("'one"));
        assertFalse(testObj.isApplicable("one'"));
        assertFalse(testObj.isApplicable("\"one"));
        assertFalse(testObj.isApplicable("one\""));
        assertFalse(testObj.isApplicable("one"));
    }

    @Test
    public void translateWillReturnCorrectValue() {
        assertEquals("one", testObj.translate("'one'"));
        assertEquals("one", testObj.translate("\"one\""));
    }

    @Test
    public void translateWillRemoveEscapes() {
        assertEquals("\"one\"", testObj.translate("'\\\"one\\\"'"));
        assertEquals("'one'", testObj.translate("\"\\'one\\'\""));
    }
}