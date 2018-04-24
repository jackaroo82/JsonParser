package org.wizfiz.json;

import java.util.Set;


/**
 * Represents a complete JSON object
 * which may be included within another
 * JSON object
 * @author Paul Jackson
 * @version 1
 */
public interface IJSONObject extends IJSONItem {
	/**
	 * Returns a field with the given name
	 * @param fieldName The name of the field
	 * @return The field or null if not present
	 */
    IJSONField getField(String fieldName);
    /**
     * Gets all fields associated with this object
     * @return The fields
     */
    IJSONField[] getFields();
    /**
     * Adds a field to this object
     * @param field The field to add
     * @throws JSONException 
     */
    void addField(IJSONField field) throws JSONException;
    /**
     * Removes the field with the given name
     * @param name The field to remove
     * @throws JSONException 
     */
	void removeField(String name) throws JSONException;
	/**
	 * Tags the object to make it queryable
	 * @param tagValue
	 */
	void addTag(String tagValue);
	/**
	 * Returns the tags for this object
	 * @return Tags
	 */
	Set<String> getTags();
	IJSONObject withField(IJSONField createField) throws JSONException;
}
