package org.wizfiz.json.creators;

import org.wizfiz.json.IFieldCreator;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONField;

public class DefaultFieldCreator implements IFieldCreator{
	private IJSONFactory jsonFactory;
	
	@Override
	public IJSONField createField(String name, Object value) throws JSONException {
		IJSONField field = new JSONField();
		field.setName(name);
		field.setValue(getJsonFactory().createValue(value));
		return field;
	}

	public IJSONFactory getJsonFactory() {
		return jsonFactory;
	}

	public void setJsonFactory(IJSONFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
	}

}
