package org.wizfiz.json;
/**
 * Objects implementing this interface represent
 * a json field - the value can be of any IJSON type
 * @author Paul Jackson
 * @version 1
 */
public interface IJSONField extends IJSONItem {
	/**
	 * Gets the name of the field
	 * @return The field name
	 */
	String getName();
	/**
	 * Sets the fields name
	 * @param value The name of the field
	 * @throws JSONException 
	 */
	void setName(String value) throws JSONException;
	/**
	 * Gets the value associated with the field
	 * which can represent any type e.g. object, string, bool
	 * @return The value
	 */
    IJSONValue getValue();
    /**
     * Sets the value of the field, which can be of any type
     * e.g. a JSON object, string, date etc
     * @param value The value to set
     */
    void setValue(IJSONValue value);
}
