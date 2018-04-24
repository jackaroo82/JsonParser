package org.wizfiz.json.utilities;

import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.JSONException;

public interface IValueHelper {
	boolean isJsonType(Class<?> classToEvaluate);
	IJSONObject jsonIfyObject(Object o) throws JSONException;
	Object getJsonValue(Object o) throws JSONException;
}
