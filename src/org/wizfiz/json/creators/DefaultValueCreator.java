package org.wizfiz.json.creators;

import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.IValueCreator;
import org.wizfiz.json.implementation.JSONValue;

public class DefaultValueCreator implements IValueCreator{

	@Override
	public IJSONValue createValue(Object val) {
		JSONValue value = new JSONValue();
		value.setValue(val);
		return value;
	}
}
