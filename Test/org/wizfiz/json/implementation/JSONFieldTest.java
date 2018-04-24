package org.wizfiz.json.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONField;
import org.wizfiz.json.implementation.JSONValue;

import org.junit.Test;

public class JSONFieldTest {
	private static final String NULL_FIELD_EX = "Field Name cannot be null";
	/**
    * Checks the value set matches that returned
	 * @throws JSONException 
    */
	@Test
    public void name() throws JSONException
    {
		JSONField field = new JSONField();
        field.setName("one");
        assertEquals("one", field.getName());
    }
	/**
    * Checks an exception is raised if set to null
    */
    @Test
    public void name_null()
    {
    	JSONField field = new JSONField();
        try
        {
            field.setName(null);
            fail("Should not get here");
        }
        catch (JSONException ex)
        {
        	assertEquals(ex.getMessage(), NULL_FIELD_EX);
        }
    }
    /**
    * Checks an exception is raised if set to empty
    */
    @Test
    public void name_empty()
    {
    	JSONField field = new JSONField();
        try
        {
            field.setName("");
            fail("Should not get here");
        }
        catch (JSONException ex)
        {
        	assertEquals(ex.getMessage(), NULL_FIELD_EX);
        }
    }
    /**
    * Checks the value set matches that returned
    */
    @Test
    public void value()
    {
        JSONValue vl = new JSONValue();
        JSONField field = new JSONField();
        field.setValue(vl);
        assertEquals(vl, field.getValue());
    }
	/**
    * Checks the value can be set to null
    */
    @Test
    public void value_Null()
    {
    	JSONField field = new JSONField();
        field.setValue(null);
        assertNotNull(field.getValue());
    }
    /**
    * Checks the string is returned ok
     * @throws JSONException 
    */
    @Test
    public void toJSON() throws JSONException
    {
    	JSONField field = new JSONField();
        field.setName("one");
        JSONValue vl = new JSONValue();
        vl.setValue(4);
        field.setValue(vl);
        assertEquals("\"one\":4", field.toJSON());
    }
	/**
    * Checks returns ok if no value has been set
     * @throws JSONException 
    */
    @Test
    public void toJSON_NoValue() throws JSONException
    {
    	JSONField field = new JSONField();
        field.setName("one");
        assertEquals("\"one\":null", field.toJSON());
    }
    /**
    * Checks an exception is raised if the field has not been set
    */
    @Test
    public void toJSON_noField()
    {
    	JSONField field = new JSONField();
        JSONValue vl = new JSONValue();
        vl.setValue(4);
        field.setValue(vl);
        try
        {
            field.toJSON();
            fail("Should not get here");
        }
        catch (JSONException ex)
        {
        	assertEquals("Field name must be set", ex.getMessage());
        }
    }
}
