package org.wizfiz.json;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Objects implementing this interface provide a service which
 * enables the parsing and creation of JSON objects
 * @author Paul Jackson
 *
 */
public interface IJSONCreator {
	/**
	 * Creates a new empty JSON object
	 * @return an empty JSON obect
	 */
    IJSONObject createObject();
    /**
     * Parses a JSON string and returns it as an
     * object
     * @param JSON A JSON string object in the form {}
     * @return A parsed JSON object
     * @throws JSONException
     */
    IJSONObject parseJSON(String JSON) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The String value to set
     * @return A string field
     * @throws JSONException 
     */
    IJSONField createField(String name, String value) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The decimal value to set
     * @return A decimal field
     * @throws JSONException 
     */
    IJSONField createField(String name, BigDecimal value) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The boolean value to set
     * @return A boolean field
     * @throws JSONException 
     */
    IJSONField createField(String name, Boolean value) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The int value to set
     * @return An int field
     * @throws JSONException 
     */
    IJSONField createField(String name, Integer value) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The Date value to set
     * @return A Date field
     * @throws JSONException 
     */
    IJSONField createField(String name, Date value) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The IJSONItem array value to set
     * @return A IJSONItem array field
     * @throws JSONException 
     */
    IJSONField createField(String name, IJSONItem[] values) throws JSONException;
    /**
     * Creates a new JSON field
     * @param name The name of the field
     * @param value The JSON object value to set
     * @return A JSON object field
     * @throws JSONException 
     */
    IJSONField createField(String name, IJSONObject value) throws JSONException;
    /**
     * Creates a JSON value
     * @param value the value of any type to set
     * @return An IJSONValue wrapping the correct type
     * @param val
     * @return
     */
    IJSONField createField(String name, Object value) throws JSONException;
    /**
     * Creates a JSON value
     * @param value The String value
     * @return A String wrapped value
     */
    IJSONValue createValue(String value);
    /**
     * Creates a JSON value
     * @param value The BigDecimal value
     * @return A BigDecimal wrapped value
     */
    IJSONValue createValue(BigDecimal value);
    /**
     * Creates a JSON value
     * @param value The boolean value
     * @return A boolean wrapped value
     */
    IJSONValue createValue(boolean value);
    /**
     * Creates a JSON value
     * @param value The int value
     * @return An int wrapped value
     */
    IJSONValue createValue(int value);
    /**
     * Creates a JSON value
     * @param value The Date value
     * @return A Date wrapped value
     */
    IJSONValue createValue(Date value);
    /**
     * Creates a JSON value
     * @param value The IJSONItem array value
     * @return A IJSONItem array wrapped value
     */
    IJSONValue createValue(IJSONItem[] values);
    /**
     * Creates a JSON value
     * @param value The IJSONObject object value
     * @return A IJSONObject object wrapped value
     */
    IJSONValue createValue(IJSONObject value);
    /**
     * Creates a JSON value
     * @param value the value of any type to set
     * @return An IJSONValue wrapping the correct type
     * @param val
     * @return
     */
    IJSONValue createValue(Object value);
}
