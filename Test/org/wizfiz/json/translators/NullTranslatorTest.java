package org.wizfiz.json.translators;

import org.junit.Test;

import static org.junit.Assert.*;

public class NullTranslatorTest {
    NullTranslator testObj = new NullTranslator();

    @Test
    public void isApplicableReturnsTrueIfValueIsStringNull() {
        assertTrue(testObj.isApplicable("null"));
    }

    @Test
    public void isApplicableReturnsTrueIfValueIsNull() {
        assertTrue(testObj.isApplicable(null));
    }

    @Test
    public void isApplicableReturnsFalseIfValueIsNotNull() {
        assertFalse(testObj.isApplicable("invalid"));
    }

    @Test
    public void translateWillReturnNull() {
        assertNull(testObj.translate("null"));
    }
}