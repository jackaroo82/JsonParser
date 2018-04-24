package org.wizfiz.json;

public interface IValueCreator {
	IJSONValue createValue(Object value) throws JSONException;
}
