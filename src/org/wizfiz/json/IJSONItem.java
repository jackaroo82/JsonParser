package org.wizfiz.json;
/**
 * Represents a JSON item
 * @author Paul Jackson
 * @version 1
 */
public interface IJSONItem {
	/**
	 * Returns the item as a string confirming to JSON standards
	 * @return The String value
	 * @throws JSONException 
	 */
	String toJSON() throws JSONException;
}
