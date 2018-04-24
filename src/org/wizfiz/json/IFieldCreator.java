package org.wizfiz.json;

public interface IFieldCreator {
	IJSONField createField(String name, Object value) throws JSONException;
	void setJsonFactory(IJSONFactory factory);
}
