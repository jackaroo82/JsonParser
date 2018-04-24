package org.wizfiz.json;
/**
 * Represents a value in a JSON item
 * @author Paul Jackson
 * @version 1
 */
public interface IJSONValue extends IJSONItem {
	/**
	 * Returns the Object representing the value
	 * @return The value
	 */
	Object getValue();

	void setValue(Object value);
}
