package org.wizfiz.json;

import org.wizfiz.json.translators.IJsonValueTranslator;

import java.util.List;

/**
 * Classes implementing this interface will receive
 * callbacks when JSON items have been reached, and will
 * be responsible for parsing the values into a JSON
 * object
 * @author Paul Jackson
 *
 */
public interface IJSONParser {
	/**
	 * Returns the parsed JSON object
	 * @return The parsed object
	 */
	IJSONObject getJson();
	
	/**
	 * Instantiates a new instance
	 * @return A new instance
	 */
	IJSONParser newInstance();
	
	/**
	 * Indicates the start of a new object
	 * @throws JSONException 
	 */
	void onNewObject() throws JSONException;
	
	/**
	 * Indicates the end of an object has been reached
	 * @throws JSONException 
	 */
	void onEndObject() throws JSONException;
	
	/**
	 * Indicates the start of a field
	 * @throws JSONException 
	 */
	void onNewField() throws JSONException;
	
	/**
	 * Indicates the end of a field
	 * @throws JSONException 
	 */
	void onEndNewField() throws JSONException;
	
	/**
	 * Indicates the start of a field value - seperate calls will be made to
	 * indicate when a value, array, object etc is found, which may form
	 * part of this value
	 * @throws JSONException 
	 */
	void onFieldValue() throws JSONException;
	
	/**
	 * Indicates the end of a field value
	 * @throws JSONException 
	 */
	void onEndFieldValue() throws JSONException;
	
	/**
	 * Indicates an array has been matched.
	 */
	void onArray();
	
	/**
	 * Indicates the end of processing an array has been reached
	 * @throws JSONException 
	 */
	void onEndArray() throws JSONException;
	
	/**
	 * Indicates an array item has been matched. This may be a value,
	 * object, another array etc so seperate calls will be made to
	 * indicate when a value etc has been matched
	 * @throws JSONException 
	 */
	void onArrayItem() throws JSONException;
	
	/**
	 * Indicates the end of processing an array element has been reached
	 * @throws JSONException 
	 */
	void onEndArrayItem() throws JSONException;
	
	/**
	 * Indicates the name of the field
	 * @param characters The characters from the JSON message
	 * @param start The start character for the field name
	 * @param end The end character for the field name
	 * @throws JSONException 
	 */
	void onFieldName(char[] characters, int start, int end) throws JSONException;
	
	/**
	 * Indicates a value has been matched
	 * @param characters characters The characters from the JSON message
	 * @param start The start character for the value
	 * @param end The end character for the value
	 * @throws JSONException 
	 */
	void onValue(char[] characters, int start, int end) throws JSONException;
	/**
	 * If the JSON data base was an array returns the array
	 * @return JSON Array
	 */
	IJSONValue[] getArray();

	IJSONFactory getJsonFactory();

	void setJsonFactory(IJSONFactory factory);

	void setTranslators(List<IJsonValueTranslator> translators);
}
