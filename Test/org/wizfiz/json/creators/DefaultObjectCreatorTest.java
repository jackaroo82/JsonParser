package org.wizfiz.json.creators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.creators.DefaultObjectCreator;

import org.junit.Test;

public class DefaultObjectCreatorTest {
	DefaultObjectCreator testObj = new DefaultObjectCreator();
	
	@Test
	public void returnsNewObject() {
		IJSONObject object = testObj.createObject();
		
		assertNotNull(object);
		assertTrue(object instanceof org.wizfiz.json.implementation.JSONObject);
	}
}
