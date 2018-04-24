package org.wizfiz.json.implementation;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONField;
import org.wizfiz.json.implementation.JSONObject;
import org.wizfiz.json.implementation.JSONParser;
import org.wizfiz.json.implementation.JSONReader;
import org.wizfiz.json.implementation.JSONValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.wizfiz.json.translators.IJsonValueTranslator;

@RunWith(Parameterized.class)
public class JSONParserParamatisedArrayValueTest {
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";

	IJsonValueTranslator translatorMock = mock(IJsonValueTranslator.class);

	@Before
	public void setup() {
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				return value;
			}
		});
	}

	@Parameters(name = "{index} : {0}")
	public static Collection<Object[]> data() {
		return asList(new Object[][] {
				{ "[]", new Object[] {} },
				{ "[1, 2, 3, 4, 5]", new Object[] { "1", "2", "3", "4", "5" } },
				{ "[1]", new Object[] { "1" } },
				{ "[\"one\", \"two\", \"three\", \"four\", \"five\"]",
						new Object[] { "\"one\"", "\"two\"", "\"three\"", "\"four\"", "\"five\"" } },
				{ "[\"one\"]", new Object[] { "\"one\"" } },
				{ "['one', 'two', 'three', 'four', 'five']",
						new Object[] { "'one'", "'two'", "'three'", "'four'", "'five'" } },
				{ "['one']", new Object[] { "'one'" } },
				{
						"[1.1, 2.20, 3.33, 4.444, 5555.555]",
						new Object[] { "1.1",
								"2.20",
								"3.33",
								"4.444",
								"5555.555" } },
				{ "[1.1]", new Object[] { "1.1" } },
				// Dates
				{
						"[\"14/01/2012 10:15:32.123\",\"14/02/2012 10:15:32.123\", \"14/03/2012 10:15:32.123\",\"14/04/2012 10:15:32.123\",\"14/05/2012 10:15:32.123\"]",
						new Object[] { "\"14/01/2012 10:15:32.123\"",
								"\"14/02/2012 10:15:32.123\"",
								"\"14/03/2012 10:15:32.123\"",
								"\"14/04/2012 10:15:32.123\"",
								"\"14/05/2012 10:15:32.123\"" } },
				{ "[\"14/01/2012 10:15:32.123\"]",
						new Object[] { "\"14/01/2012 10:15:32.123\"" } },
				{
						"[14/01/2012 10:15:32.123,14/02/2012 10:15:32.123, 14/03/2012 10:15:32.123,14/04/2012 10:15:32.123,14/05/2012 10:15:32.123]",
						new Object[] { "14/01/2012 10:15:32.123",
								"14/02/2012 10:15:32.123",
								"14/03/2012 10:15:32.123",
								"14/04/2012 10:15:32.123",
								"14/05/2012 10:15:32.123" } },
				{ "[14/01/2012 10:15:32.123]",
						new Object[] { "14/01/2012 10:15:32.123" } },
				{
						"['14/01/2012 10:15:32.123','14/02/2012 10:15:32.123', '14/03/2012 10:15:32.123','14/04/2012 10:15:32.123','14/05/2012 10:15:32.123']",
						new Object[] { "'14/01/2012 10:15:32.123'",
								"'14/02/2012 10:15:32.123'",
								"'14/03/2012 10:15:32.123'",
								"'14/04/2012 10:15:32.123'",
								"'14/05/2012 10:15:32.123'" } },
				{ "['14/01/2012 10:15:32.123']",
						new Object[] { "'14/01/2012 10:15:32.123'" } },
				// Booleans
				{ "[true, false, true, false, false]",
						new Object[] { "true", "false", "true", "false", "false" } },
				{ "[true]", new Object[] { "true" } } });
	}
	
    private static SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static Date getDate(String value) {
    	try {
			return formatter.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	@Parameter(value = 0)
	public String jsn;
	
	@Parameter(value = 1)
	public Object[] value;
	
	
	@Test
	public void testArray() throws JSONException
	{
		JSONParser parser = new JSONParser();
		parser.setTranslators(asList(translatorMock));
    	IJSONFactory factoryMock = mock(IJSONFactory.class);
    	when(factoryMock.createField(anyString(), anyObject())).then(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] arguments = invocation.getArguments();
				JSONField field = new JSONField();
				field.setName((String) arguments[0]);
				JSONValue value = new JSONValue();
				value.setValue(arguments[1]);
				field.setValue(value);
				return field;
			}

		});
    	when(factoryMock.createObject()).then(new Answer<IJSONObject>() {

			@Override
			public IJSONObject answer(InvocationOnMock invocation) throws Throwable {
				return new JSONObject();
			}
		
    	});
    	when(factoryMock.createValue(anyObject())).then(new Answer<IJSONValue>() {

			@Override
			public IJSONValue answer(InvocationOnMock invocation) throws Throwable {
				JSONValue value = new JSONValue();
				value.setValue(invocation.getArguments()[0]);
				return value;
			}
		});
    	parser.setJsonFactory(factoryMock);
    	
        JSONReader reader = new JSONReader();
        reader.read(parser, jsn);
        assertNull(parser.getJson());
        assertNotNull(parser.getArray());
        assertEquals(value.length, parser.getArray().length);
        for(int x = 0; x < value.length; x++)
        {
        	assertEquals(value[x], parser.getArray()[x].getValue());
        }
	}
}
