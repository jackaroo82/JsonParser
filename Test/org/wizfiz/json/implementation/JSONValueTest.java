package org.wizfiz.json.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wizfiz.json.IJSONItem;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONValue;

import org.junit.Test;

public class JSONValueTest{
	/**
    * Checks the value set matches that returned
    */
	@Test
    public void value()
    {
        JSONValue vl = new JSONValue();
        vl.setValue("one");
        assertEquals("one", vl.getValue());
    }
	/**
    * Checks can be set to null
    */
    @Test
    public void value_Null()
    {

        JSONValue vl = new JSONValue();
        vl.setValue(null);
        assertNull(vl.getValue());
    }
    /**
    * Checks JSON formatted ok if item is null
     * @throws JSONException 
    */
    @Test
    public void toJSON_null() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue(null);
        
        assertEquals("null", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is date
     * @throws JSONException 
    */
    @Test
    public void toJSON_date() throws JSONException
    {
        Date dt = new Date();
        JSONValue vl = new JSONValue();
        vl.setValue(dt);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        assertEquals("\"" + formatter.format(dt) + "\"", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is int
     * @throws JSONException 
    */
    @Test
    public void toJSON_int() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue(3);

        assertEquals("3", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is decimal
     * @throws JSONException 
    */
    @Test
    public void toJSON_decimal() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue(new BigDecimal("3.321"));
        
        assertEquals("3.321", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is bool (true)
     * @throws JSONException 
    */
    @Test
    public void toJSON_bool_true() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue(true);

        assertEquals("true", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is bool (false)
     * @throws JSONException 
    */
    @Test
    public void toJSON_bool_false() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue(false);
        
        assertEquals("false", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is string
     * @throws JSONException 
    */
    @Test
    public void toJSON_string() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue("3");
        
        assertEquals("\"3\"", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is string
    * and escapes "
     * @throws JSONException 
    */
    @Test
    public void toJSON_stringEscaped() throws JSONException
    {
        JSONValue vl = new JSONValue();
        vl.setValue("3 \" 4");
        
        assertEquals("\"3 \\\" 4\"", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is IJSONItem[] as int
     * @throws JSONException 
    */
    @Test
    public void toJSON_Array_int() throws JSONException
    {
        List<IJSONItem> items = new ArrayList<IJSONItem>();
        for (int x = 0; x < 10; x++)
        {
            JSONValue v = new JSONValue();
            v.setValue(x);
            items.add(v);
        }
        JSONValue vl = new JSONValue();
        vl.setValue(items.toArray(new IJSONItem[items.size()]));
        assertEquals("[0,1,2,3,4,5,6,7,8,9]", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is IJSONItem[] as string
     * @throws JSONException 
    */
    @Test
    public void toJSON_Array_string() throws JSONException
    {
        List<IJSONItem> items = new ArrayList<IJSONItem>();
        for (int x = 0; x < 10; x++)
        {
            JSONValue v = new JSONValue();
            v.setValue(Integer.valueOf(x) + "a");
            items.add(v);
        }
        JSONValue vl = new JSONValue();
        vl.setValue(items.toArray(new IJSONItem[items.size()]));
        assertEquals("[\"0a\",\"1a\",\"2a\",\"3a\",\"4a\",\"5a\",\"6a\",\"7a\",\"8a\",\"9a\"]", vl.toJSON());
    }
    /**
    * Checks JSON formatted ok if item is JSONobject
     * @throws JSONException 
    */
    @Test
    public void toJSON_JSONobject() throws JSONException
    {
        IJSONObject o = mock(IJSONObject.class, RETURNS_DEEP_STUBS);
        when(o.toJSON()).thenReturn("MockedJson");
        
        JSONValue vl = new JSONValue();
        vl.setValue(o);
        assertEquals("MockedJson", vl.toJSON());
    }
}
