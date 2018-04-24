package org.wizfiz.json.creators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.creators.DefaultValueCreator;
import org.wizfiz.json.implementation.JSONValue;

import org.junit.Test;

public class DefaultValueCreatorTest{
	private static final String VALUE = "test";
	DefaultValueCreator testObj = new DefaultValueCreator();
	
	@Test
	public void createValueReturnsNewValue() {
		IJSONValue value = testObj.createValue(VALUE);
		
		assertNotNull(value);
		assertTrue(value instanceof JSONValue);
		assertEquals(VALUE, value.getValue());
	}
}
