package org.wizfiz.json.translators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SystemPropertyTranslatorTest {
    public static final String VALUE_TO_TRANSLATE = "test";
    public static final int TRANSLATED_VALUE = 10;
    SystemPropertyTranslator testObj = new SystemPropertyTranslator();

    @Mock
    IJsonValueTranslator subTranslator1Mock;
    @Mock
    IJsonValueTranslator subTranslator2Mock;

    @Before
    public void setup() {
        System.setProperty("someValue", VALUE_TO_TRANSLATE);
        testObj.setSubTranslators(asList(subTranslator1Mock, subTranslator2Mock));
        when(subTranslator1Mock.isApplicable(VALUE_TO_TRANSLATE)).thenReturn(false);
        when(subTranslator2Mock.isApplicable(VALUE_TO_TRANSLATE)).thenReturn(false);
    }

    @Test
    public void isApplicableReturnsTrueIfStartsWithAtSign() {
        assertTrue(testObj.isApplicable("@someValue"));
    }

    @Test
    public void isApplicableReturnsFalseIfDoesntStartsWithAtSign() {
        assertFalse(testObj.isApplicable("someValue"));
    }

    @Test
    public void translateReturnsSystemValueAsGivenValueIfNoTranslatorHandles() {
        assertEquals(VALUE_TO_TRANSLATE, testObj.translate("@someValue"));
    }

    @Test
    public void translateReturnsSystemValueFromTranslatorIfHandles() {
        when(subTranslator2Mock.isApplicable(VALUE_TO_TRANSLATE)).thenReturn(true);
        when(subTranslator2Mock.translate(VALUE_TO_TRANSLATE)).thenReturn(TRANSLATED_VALUE);

        assertEquals(TRANSLATED_VALUE, testObj.translate("@someValue"));
    }
}