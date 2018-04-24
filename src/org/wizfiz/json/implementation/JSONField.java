package org.wizfiz.json.implementation;

import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;

public class JSONField implements IJSONField {
	public static final String NULL_FIELD_EX = "Field Name cannot be null";
	private final static String FIELD_NOT_SET_EX = "Field name must be set";
    private String name;
    private IJSONValue value;
 
    public String toJSON() throws JSONException
    {
        if (null == this.getName())
        {
            throw new JSONException(FIELD_NOT_SET_EX);
        }
        return "\"" + this.getName() + "\":" + this.getValue().toJSON();
    }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String value) throws JSONException {
		if (null == value || "".equals(value))
        {
            throw new JSONException(NULL_FIELD_EX);
        }
		this.name = value;
	}

	@Override
	public IJSONValue getValue() {
		return (null == this.value ? new JSONValue() : this.value);
	}

	@Override
	public void setValue(IJSONValue value) {
		this.value = value;
	}
}
