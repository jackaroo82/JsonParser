package org.wizfiz.json.implementation;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.SystemParserCreator;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONToObjectParserTest {
	private static final String JSON_STRING = "[1, 2, 'a', 'b', 'c']";

	@InjectMocks JSONToObjectParser testObj = (JSONToObjectParser)SystemParserCreator.SYSTEM_PARSER;
	
	@Test
	public void parsesArrayCorectly() throws Exception {
		IJSONValue[] returned = testObj.parseJsonArray(JSON_STRING);
		
		assertEquals(5, returned.length);
		assertEquals(1, returned[0].getValue());
		assertEquals(2, returned[1].getValue());
		assertEquals("a", returned[2].getValue());
		assertEquals("b", returned[3].getValue());
		assertEquals("c", returned[4].getValue());
	}

	@Test
	public void parseWillCorrectlyParseDates() throws JSONException {
		String content = "{\"return\":{\"owner\":\"test\",\"intent\":\"addlast\",\"action\":\"testaction\",\"lastActioned\":\"10/03/2018 10:10:10.000\",\"id\":\"7e80f98e-10f8-4c1e-8c01-4885ed377f18\",\"privateIp\":\"10.0.218.147\",\"port\":1024}}";

		"10/03/2018 10:10:10.000".matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}(.[0-9]{1,3})?$");

		IJSONObject response = SystemParserCreator.SYSTEM_PARSER.parseJson(content);

		assertTrue(((IJSONObject)response.getField("return").getValue().getValue()).getField("lastActioned").getValue().getValue() instanceof Date);
	}
}
