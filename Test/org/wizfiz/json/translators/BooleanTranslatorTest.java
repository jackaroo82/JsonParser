package org.wizfiz.json.translators;

import org.junit.Test;

import static org.junit.Assert.*;

public class BooleanTranslatorTest {
    BooleanTranslator testObj = new BooleanTranslator();

    @Test
    public void isApplicableReturnsTrueIfValueIsTrue() {
        assertTrue(testObj.isApplicable("true"));
    }

    @Test
    public void isApplicableReturnsTrueIfValueIsFalse() {
        assertTrue(testObj.isApplicable("false"));
    }

    @Test
    public void isApplicableReturnsFalseIfNotBoolean() {
        assertFalse(testObj.isApplicable("invalid"));
    }

    @Test
    public void translateReturnsTrueIfTrue() {
        assertTrue((boolean)testObj.translate("true"));
    }

    @Test
    public void translateReturnsFalseIfFalse() {
        assertFalse((boolean)testObj.translate("false"));
    }
}