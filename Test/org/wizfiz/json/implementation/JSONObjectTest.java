package org.wizfiz.json.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.wizfiz.json.IJSONField;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JSONObjectTest {
	private static final String TAG_VALUE = "my tag";
	private JSONObject testObj = new JSONObject();
	
	/**
    * Checks this["item"] returns the field if present
	 * @throws JSONException 
    */
	@Test
    public void defaultFieldName() throws JSONException
    {
        for (int x = 0; x < 10; x++)
        {
        	IJSONField fieldMock = mock(IJSONField.class, RETURNS_DEEP_STUBS);
        	when(fieldMock.getName()).thenReturn("FLD_" + x);
        	when(fieldMock.getValue().getValue()).thenReturn(x);
            testObj.addField(fieldMock);
        }
        IJSONField fv = mock(IJSONField.class, RETURNS_DEEP_STUBS);
    	when(fv.getName()).thenReturn("test");
    	when(fv.getValue().getValue()).thenReturn(new Date());
        testObj.addField(fv);
        assertSame(fv, testObj.getField("test"));
    }
	/**
    * Checks the fields are returned ok
	 * @throws JSONException 
    */
    @Test
    public void getFields() throws JSONException
    {
    	IJSONField f = mock(IJSONField.class, RETURNS_DEEP_STUBS);
        testObj.addField(f);
        assertNotNull(testObj.getFields());
        assertEquals(1, testObj.getFields().length);
        assertSame(f, testObj.getFields()[0]);
    }
	/**
    * Checks if nothing is set an empty array is returned
    */
    @Test
    public void getFields_noneSet()
    {
        assertNotNull(testObj.getFields());
        assertEquals(0, testObj.getFields().length);
    }
	/**
    * Checks the field is added ok
	 * @throws JSONException 
    */
    @Test
    public void addField() throws JSONException
    {
    	IJSONField f = mock(IJSONField.class, RETURNS_DEEP_STUBS);
        testObj.addField(f);
        assertNotNull(testObj.getFields());
        assertEquals(1, testObj.getFields().length);
        assertSame(f, testObj.getFields()[0]);
    }
    /**
    * Checks field not added twice
     * @throws JSONException 
    */
    @Test
    public void addField_alreadyPresent() throws JSONException
    {
    	IJSONField f = mock(IJSONField.class, RETURNS_DEEP_STUBS);
        testObj.addField(f);
        testObj.addField(f);
        testObj.addField(f);
        assertNotNull(testObj.getFields());
        assertEquals(1, testObj.getFields().length);
        assertSame(f, testObj.getFields()[0]);
    }
    /**
    * Checks nothing is added if null passed in - and also no exception raised
     * @throws JSONException 
    */
    @Test
    public void addField_Null() throws JSONException
    {
        testObj.addField(null);
        testObj.addField(null);
        testObj.addField(null);
        assertNotNull(testObj.getFields());
        assertEquals(0, testObj.getFields().length);
    }
    /**
    * Checks field is removed
     * @throws JSONException 
    */
    @Test
    public void removeField() throws JSONException
    {
    	IJSONField f = mock(IJSONField.class, RETURNS_DEEP_STUBS);
    	when(f.getName()).thenReturn("test");
    	when(f.getValue().getValue()).thenReturn(3);
        testObj.addField(f);
        assertEquals(1, testObj.getFields().length);
        
        testObj.removeField("test");
        
        assertEquals(0, testObj.getFields().length);
    }
    /**
    * Checks field no exception raised when field not present
     * @throws JSONException 
    */
    @Test
    public void removeField_whenNotPresent() throws JSONException
    {
        assertEquals(0, testObj.getFields().length);
        
        testObj.removeField("test");
        
        assertEquals(0, testObj.getFields().length);
    }
    /**
    * Checks the JSON is returned ok
     * @throws JSONException 
    */
    @Test
    public void toJSON() throws JSONException
    {
        for (int x = 0; x < 10; x++)
        {
        	IJSONField f = mock(IJSONField.class, RETURNS_DEEP_STUBS);
        	when(f.toJSON()).thenReturn("Mocked" + x);
            testObj.addField(f);
        }
        assertEquals("{Mocked0,Mocked1,Mocked2,Mocked3,Mocked4,Mocked5,Mocked6,Mocked7,Mocked8,Mocked9}", testObj.toJSON());
    }
    /**
    * Checks returned ok if no fields are set
     * @throws JSONException 
    */
    @Test
    public void toJSON_noFields() throws JSONException
    {
        assertEquals("{}", testObj.toJSON());
    }
    
    @Test
    public void addTagAddsTagToSet() {
    	testObj.addTag(TAG_VALUE);
    	
    	assertTrue(testObj.getTags().contains(TAG_VALUE));
    }
}
