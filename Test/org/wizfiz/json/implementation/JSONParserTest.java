package org.wizfiz.json.implementation;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONItem;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONParser;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONField;
import org.wizfiz.json.implementation.JSONObject;
import org.wizfiz.json.implementation.JSONParser;
import org.wizfiz.json.implementation.JSONReader;
import org.wizfiz.json.implementation.JSONValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.wizfiz.json.translators.IJsonValueTranslator;

@RunWith(MockitoJUnitRunner.class)
public class JSONParserTest {

	@Mock
	IJsonValueTranslator translatorMock;

	JSONParser parser;
	IJSONFactory jsonFactoryMock;

	@Before
	public void setup() throws JSONException
	{
		parser = new JSONParser();
		parser.setTranslators(asList(translatorMock));
    	jsonFactoryMock = mock(IJSONFactory.class);
    	when(jsonFactoryMock.createField(anyString(), anyObject())).then(new Answer<Object>() {

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
    	when(jsonFactoryMock.createObject()).then(new Answer<IJSONObject>() {

			@Override
			public IJSONObject answer(InvocationOnMock invocation) throws Throwable {
				return new JSONObject();
			}
		
    	});
    	when(jsonFactoryMock.createValue(anyObject())).then(new Answer<IJSONValue>() {

			@Override
			public IJSONValue answer(InvocationOnMock invocation) throws Throwable {
				JSONValue value = new JSONValue();
				value.setValue(invocation.getArguments()[0]);
				return value;
			}
		});
    	parser.setJsonFactory(jsonFactoryMock);
	}
	
	/**
	 * Checks an array of objects parses ok
	 * 
	 * @throws JSONException
	 */
	@Test
	public void Array_Objects() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				return Integer.valueOf(value);
			}
		});
		String jsn = "[{\"f\":1}, {\"f\":2}, {\"f\":3}, {\"f\":4}, {\"f\":5}]";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);
		assertNull(parser.getJson());
		assertNotNull(parser.getArray());
		assertEquals(5, parser.getArray().length);
		for (int x = 1; x < 6; x++) {
			assertTrue(parser.getArray()[x - 1].getValue() instanceof IJSONObject);
			assertEquals(x, ((IJSONObject) parser.getArray()[x - 1].getValue())
					.getField("f").getValue().getValue());
		}
	}

	/**
	 * Checks parses ok with a \t \n \r in the JSON
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_TabLineReturn() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				if (value.startsWith("\"")) {
					return value.substring(1, value.length()-1);
				}
				return Integer.valueOf(value);
			}
		});
		String jsn = "{\"field1\"\t\n\r : \t\n\r[\t\n\r{\"fielda\"\t\n\r : \t\n\r[\t\n\r{\t\n\r\"fielda_a\"\t\n\r :\t\n\r 10 \t\n\r}\t\n\r]\t\n\r}\t\n\r]\t\n\r}";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);

		// fail("Parser is sending \" in field name if a \t\r\n follow");

		IJSONObject inEval = (IJSONObject) ((IJSONValue) ((IJSONItem[]) parser
				.getJson().getField("field1").getValue().getValue())[0])
				.getValue();
		assertEquals(1, inEval.getFields().length);

		inEval = (IJSONObject) ((IJSONValue) ((IJSONItem[]) inEval
				.getField("fielda").getValue().getValue())[0]).getValue();
		assertEquals(1, inEval.getFields().length);

		IJSONValue val = inEval.getField("fielda_a").getValue();
		assertEquals(10, val.getValue());
	}

	/**
	 * Checks parses ok if an array is within an array
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_ArrayWithinArray() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return Integer.valueOf((String)invocation.getArguments()[0]);
			}
		});
		String jsn = "{\"field1\" : [{\"fielda\" : [{\"fielda_a\" : 10}]}]}";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);

		IJSONObject inEval = (IJSONObject) ((IJSONValue) ((IJSONItem[]) parser
				.getJson().getField("field1").getValue().getValue())[0])
				.getValue();
		assertEquals(1, inEval.getFields().length);

		inEval = (IJSONObject) ((IJSONValue) ((IJSONItem[]) inEval
				.getField("fielda").getValue().getValue())[0]).getValue();
		assertEquals(1, inEval.getFields().length);

		IJSONValue val = inEval.getField("fielda_a").getValue();
		assertEquals(10, val.getValue());
	}
	
	/**
	 * Checks parses ok if field has null value
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_nullValue() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
		});
		String jsn = "{\"field1\" : null}";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);

		Object value = parser.getJson().getField("field1").getValue().getValue();
		assertNull(value);
	}
	
	/**
	 * Checks parses ok if array has null value
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_nullValueInArray() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				if (value.equals("null")) {
					return null;
				}
				return Integer.valueOf(value);
			}
		});

		String jsn = "{\"field1\" : [null]}";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);

		IJSONItem[] value = ((IJSONItem[])parser.getJson().getField("field1").getValue().getValue());
		assertEquals(1, value.length);
		assertNull(((IJSONValue)value[0]).getValue());
	}
	
	/**
	 * Checks parses ok if array has null value
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_nullValueInArrayOfValues() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				if (value.equals("null")) {
					return null;
				}
				return Integer.valueOf(value);
			}
		});
		String jsn = "{\"field1\" : [1, null, 2]}";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);

		IJSONItem[] value = ((IJSONItem[])parser.getJson().getField("field1").getValue().getValue());
		assertEquals(3, value.length);
		assertEquals(1, ((IJSONValue)value[0]).getValue());
		assertNull(((IJSONValue)value[1]).getValue());
		assertEquals(2, ((IJSONValue)value[2]).getValue());
	}

	/**
	 * Checks parses ok with 1 sub object field (with or without " around
	 * getName())
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_Object1Field() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				if (value.startsWith("\"")) {
					return value.substring(1, value.length()-1);
				}
				return Integer.valueOf(value);
			}
		});
		new JSONReader().read(parser,
				"{one : {two : 2, three : \"four\"}}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		assertTrue(parser.getJson().getFields()[0] instanceof IJSONField);
		IJSONField field = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field.getName());
		assertEquals(2,
				((IJSONObject) field.getValue().getValue()).getFields().length);
		assertTrue(((IJSONObject) field.getValue().getValue()).getFields()[0] instanceof IJSONField);
		assertEquals("two",
				((IJSONObject) field.getValue().getValue()).getFields()[0]
						.getName());
		assertEquals(2,
				((IJSONObject) field.getValue().getValue()).getFields()[0]
						.getValue().getValue());
		assertTrue(((IJSONObject) field.getValue().getValue()).getFields()[1] instanceof IJSONField);
		assertEquals("three",
				((IJSONObject) field.getValue().getValue()).getFields()[1]
						.getName());
		assertEquals("four",
				((IJSONObject) field.getValue().getValue()).getFields()[1]
						.getValue().getValue());

		setup();
		
		new JSONReader().read(parser,
				"{\"one\" : {\"two\" : 2, \"three\" : \"four\"}}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		assertTrue(parser.getJson().getFields()[0] instanceof IJSONField);
		IJSONField field2 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field2.getName());
		assertEquals(2,
				((IJSONObject) field2.getValue().getValue()).getFields().length);
		assertTrue(((IJSONObject) field2.getValue().getValue()).getFields()[0] instanceof IJSONField);
		assertEquals("two",
				((IJSONObject) field2.getValue().getValue()).getFields()[0]
						.getName());
		assertEquals(2,
				((IJSONObject) field2.getValue().getValue()).getFields()[0]
						.getValue().getValue());
		assertTrue(((IJSONObject) field2.getValue().getValue()).getFields()[1] instanceof IJSONField);
		assertEquals("three",
				((IJSONObject) field2.getValue().getValue()).getFields()[1]
						.getName());
		assertEquals("four",
				((IJSONObject) field2.getValue().getValue()).getFields()[1]
						.getValue().getValue());
	}

	/**
	 * Checks parses ok with 5 sub object fields (with or without " around
	 * getName())
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_Object5Fields() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				if (value.startsWith("\"")) {
					return value.substring(1, value.length()-1);
				}
				return Integer.valueOf(value);
			}
		});
		String[] basenames = new String[] { "one", "two", "three", "four",
				"five" };
		List<String[]> names = new ArrayList<String[]>();
		List<Object[]> values = new ArrayList<Object[]>();
		names.add(new String[] { "two", "three" });
		names.add(new String[] { "four", "five" });
		names.add(new String[] { "six", "seven" });
		names.add(new String[] { "eight", "nine" });
		names.add(new String[] { "ten", "evelen" });

		values.add(new Object[] { 2, "four" });
		values.add(new Object[] { 3, "five" });
		values.add(new Object[] { 4, "six" });
		values.add(new Object[] { 5, "seven" });
		values.add(new Object[] { 6, "eight" });

		setup();
		
		new JSONReader()
				.read(parser,
						"{one : {two : 2, three : \"four\"}, two : {four : 3, five : \"five\"}, three : {six : 4, seven : \"six\"}, four : {eight : 5, nine : \"seven\"}, five : {ten : 6, evelen : \"eight\"}}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(5, parser.getJson().getFields().length);

		for (int x = 0; x < parser.getJson().getFields().length; x++) {
			assertTrue(parser.getJson().getFields()[x] instanceof IJSONField);
			IJSONField field = (IJSONField) parser.getJson()
					.getFields()[x];
			assertEquals(basenames[x], field.getName());
			assertEquals(
					2,
					((IJSONObject) field.getValue().getValue()).getFields().length);
			assertTrue(((IJSONObject) field.getValue().getValue()).getFields()[0] instanceof IJSONField);
			assertEquals(names.get(x)[0], ((IJSONObject) field.getValue()
					.getValue()).getFields()[0].getName());
			assertEquals(values.get(x)[0], ((IJSONObject) field.getValue()
					.getValue()).getFields()[0].getValue().getValue());
			assertTrue(((IJSONObject) field.getValue().getValue()).getFields()[1] instanceof IJSONField);
			assertEquals(names.get(x)[1], ((IJSONObject) field.getValue()
					.getValue()).getFields()[1].getName());
			assertEquals(values.get(x)[1], ((IJSONObject) field.getValue()
					.getValue()).getFields()[1].getValue().getValue());
		}

		setup();
		
		new JSONReader()
				.read(parser,
						"{\"one\" : {\"two\" : 2, \"three\" : \"four\"}, \"two\" : {\"four\" : 3, \"five\" : \"five\"}, \"three\" : {\"six\" : 4, \"seven\" : \"six\"}, \"four\" : {\"eight\" : 5, \"nine\" : \"seven\"}, \"five\" : {\"ten\" : 6, \"evelen\" : \"eight\"}}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(5, parser.getJson().getFields().length);
		for (int x = 0; x < parser.getJson().getFields().length; x++) {
			assertTrue(parser.getJson().getFields()[x] instanceof IJSONField);
			IJSONField field = (IJSONField) parser.getJson()
					.getFields()[x];
			assertEquals(basenames[x], field.getName());
			assertEquals(
					2,
					((IJSONObject) field.getValue().getValue()).getFields().length);
			assertTrue(((IJSONObject) field.getValue().getValue()).getFields()[0] instanceof IJSONField);
			assertEquals(names.get(x)[0], ((IJSONObject) field.getValue()
					.getValue()).getFields()[0].getName());
			assertEquals(values.get(x)[0], ((IJSONObject) field.getValue()
					.getValue()).getFields()[0].getValue().getValue());
			assertTrue(((IJSONObject) field.getValue().getValue()).getFields()[1] instanceof IJSONField);
			assertEquals(names.get(x)[1], ((IJSONObject) field.getValue()
					.getValue()).getFields()[1].getName());
			assertEquals(values.get(x)[1], ((IJSONObject) field.getValue()
					.getValue()).getFields()[1].getValue().getValue());
		}
	}

	/**
	 * Checks parses ok with 1 Array field with array getValue()s (with or
	 * without " around getName())
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_Array1FieldArray() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				return Integer.valueOf(value);
			}
		});
		new JSONReader().read(parser, "{one : [[1]]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field1 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field1.getName());
		assertEquals(1, ((IJSONItem[]) field1.getValue().getValue()).length);
		assertEquals(1, ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field1
				.getValue().getValue())[0]).getValue()).length);
		assertEquals(
				1,
				((IJSONValue) ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field1
						.getValue().getValue())[0]).getValue())[0]).getValue());

		setup();
		
		new JSONReader().read(parser, "{\"one\" : [[2]]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field2 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field2.getName());
		assertEquals(1, ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field2
				.getValue().getValue())[0]).getValue()).length);
		assertEquals(
				2,
				((IJSONValue) ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field2
						.getValue().getValue())[0]).getValue())[0]).getValue());
	}

	/**
	 * Checks parses ok with 5 Array field with array getValue()s (with or
	 * without " around getName())
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_Array5FieldsArray() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return Integer.valueOf((String)invocation.getArguments()[0]);
			}
		});
		new JSONReader().read(parser,
				"{one : [[1], [2, 3], [4,5,6],[7,8,9,10], [11,12 ,13,14,15]]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field1 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field1.getName());
		assertEquals(5, ((IJSONItem[]) field1.getValue().getValue()).length);
		int vl = 1;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field1
					.getValue().getValue())[x]).getValue()).length; y++) {
				assertEquals(
						vl,
						((IJSONValue) ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field1
								.getValue().getValue())[x]).getValue())[y])
								.getValue());
				vl++;
			}
		}

		setup();
		
		new JSONReader()
				.read(parser,
						"{\"one\" : [[1], [2, 3], [4,5,6],[7,8,9,10], [11,12 ,13,14,15]]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field2 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field2.getName());
		assertEquals(5, ((IJSONItem[]) field2.getValue().getValue()).length);
		vl = 1;
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field2
					.getValue().getValue())[x]).getValue()).length; y++) {
				assertEquals(
						vl,
						((IJSONValue) ((IJSONItem[]) ((IJSONValue) ((IJSONItem[]) field2
								.getValue().getValue())[x]).getValue())[y])
								.getValue());
				vl++;
			}
		}
	}

	/**
	 * Checks parses ok with 1 Array field with object getValue()s (with or
	 * without " around getName())
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_Array1FieldObject() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				return Integer.valueOf(value);
			}
		});
		new JSONReader().read(parser, "{one : [{two : 3}]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field1 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field1.getName());
		assertEquals(1, ((IJSONItem[]) field1.getValue().getValue()).length);

		for (int x = 0; x < ((IJSONItem[]) field1.getValue().getValue()).length; x++) {
			IJSONItem[] items = (IJSONItem[]) field1.getValue().getValue();
			IJSONObject o = (IJSONObject) ((IJSONValue) items[x]).getValue();

			assertEquals(1, o.getFields().length);
			assertEquals("two", o.getFields()[0].getName());
			assertEquals(3, o.getFields()[0].getValue().getValue());
		}

		setup();
		
		new JSONReader().read(parser,
				"{\"one\" : [{\"three\" : 4}]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field2 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field2.getName());
		assertEquals(1, ((IJSONItem[]) field2.getValue().getValue()).length);

		for (int x = 0; x < ((IJSONItem[]) field2.getValue().getValue()).length; x++) {
			IJSONItem[] items = (IJSONItem[]) field2.getValue().getValue();
			IJSONObject o = (IJSONObject) ((IJSONValue) items[x]).getValue();

			assertEquals(1, o.getFields().length);
			assertEquals("three", o.getFields()[0].getName());
			assertEquals(4, o.getFields()[0].getValue().getValue());
		}
	}

	/**
	 * Checks parses ok with 5 Array field with object getValue()s (with or
	 * without " around getName())
	 * 
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void test_Array5FieldsObject() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				return Integer.valueOf(value);
			}
		});
		String[] fields = new String[] { "two", "three", "five", "seven",
				"nine" };
		int[] values = new int[] { 3, 4, 6, 8, 10 };
		new JSONReader()
				.read(parser,
						"{one : [{two : 3}, {three : 4}, {five : 6}, {seven : 8}, {nine : 10}]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field1 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field1.getName());
		assertEquals(5, ((IJSONItem[]) field1.getValue().getValue()).length);

		for (int x = 0; x < ((IJSONItem[]) field1.getValue().getValue()).length; x++) {
			IJSONItem[] items = (IJSONItem[]) field1.getValue().getValue();
			IJSONObject o = (IJSONObject) ((IJSONValue) items[x]).getValue();
			assertEquals(1, o.getFields().length);
			assertEquals(fields[x], o.getFields()[0].getName());
			assertEquals(values[x], o.getFields()[0].getValue().getValue());
		}

		setup();
		
		new JSONReader()
				.read(parser,
						"{\"one\" : [{\"two\" : 3}, {\"three\" : 4}, {\"five\" : 6}, {\"seven\" : 8}, {\"nine\" : 10}]}");
		assertNotNull(parser.getJson());
		assertTrue(parser.getJson() instanceof IJSONObject);
		assertEquals(1, parser.getJson().getFields().length);
		IJSONField field2 = (IJSONField) parser.getJson()
				.getFields()[0];
		assertEquals("one", field2.getName());
		assertEquals(5, ((IJSONItem[]) field2.getValue().getValue()).length);

		for (int x = 0; x < ((IJSONItem[]) field2.getValue().getValue()).length; x++) {
			IJSONItem[] items = (IJSONItem[]) field2.getValue().getValue();
			IJSONObject o = (IJSONObject) ((IJSONValue) items[x]).getValue();
			assertEquals(1, o.getFields().length);
			assertEquals(fields[x], o.getFields()[0].getName());
			assertEquals(values[x], o.getFields()[0].getValue().getValue());
		}
	}
	
	@Test
	public void createNewCreatesNewInstanceAndCopiesFactory() {
		IJSONParser newInstance = parser.newInstance();
		
		assertNotSame(parser, newInstance);
		assertSame(jsonFactoryMock, ((JSONParser)newInstance).getJsonFactory());
	}

	/**
	 * Checks parses ok with a \t \n \r in the JSON
	 *
	 * @throws JSONException
	 * @throws ServiceNotAvailable
	 */
	@Test
	public void canParseWithColonsInFieldName() throws JSONException {
		when(translatorMock.isApplicable(anyString())).thenReturn(true);
		when(translatorMock.translate(anyString())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String value = (String) invocation.getArguments()[0];
				if (value.startsWith("\"")) {
					return value.substring(1, value.length()-1);
				}
				return Integer.valueOf(value);
			}
		});
		String jsn = "{\"some::field\" : \"some::value\"}";
		JSONReader reader = new JSONReader();
		reader.read(parser, jsn);

		IJSONObject returned = parser.getJson();

		assertEquals("some::value", returned.getField("some::field").getValue().getValue());
	}
}
