package org.wizfiz.json.translators;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DateTranslatorTest {
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
    public static final String DATE = "12/03/2016 10:11:12.123";

    DateTranslator testObj = new DateTranslator();

    @Test
    public void isApplicableReturnsTrueIfDate() {
        assertTrue(testObj.isApplicable(DATE));
    }

    @Test
    public void isApplicableReturnsTrueIfDatePrefixedWithDoubleQuote() {
        assertTrue(testObj.isApplicable("\"" + DATE + "\""));
    }

    @Test
    public void isApplicableReturnsTrueIfDatePrefixedWithSingleQuote() {
        assertTrue(testObj.isApplicable("'" + DATE + "'"));
    }

    @Test
    public void isApplicableReturnsFalseIfNotDate() {
        assertFalse(testObj.isApplicable("invalid"));
    }

    @Test
    public void translateReturnsDate() throws Exception {
        Date expected = new SimpleDateFormat(DATE_TIME_FORMAT).parse(DATE);
        Date translated = (Date)testObj.translate(DATE);
        assertEquals(expected, translated);
    }

    @Test
    public void translateReturnsDateWhenSingleQuotes() throws Exception {
        Date expected = new SimpleDateFormat(DATE_TIME_FORMAT).parse(DATE);
        Date translated = (Date)testObj.translate("'" + DATE + "'");
        assertEquals(expected, translated);
    }
    @Test
    public void translateReturnsDateWhenDoubleQuotes() throws Exception {
        Date expected = new SimpleDateFormat(DATE_TIME_FORMAT).parse(DATE);
        Date translated = (Date)testObj.translate("\"" + DATE + "\"");
        assertEquals(expected, translated);
    }

}