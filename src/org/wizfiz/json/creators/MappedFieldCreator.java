package org.wizfiz.json.creators;

import org.wizfiz.json.IFieldCreator;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.implementation.JSONField;

import java.util.HashMap;
import java.util.Map;

public class MappedFieldCreator implements IFieldCreator {
    private IJSONFactory jsonFactory;
    private Map<String, String> fieldMappings = new HashMap<>();

    @Override
    public IJSONField createField(String name, Object value) throws JSONException {
        IJSONField field = new JSONField();
        if (getFieldMappings().containsKey(name)) {
            name = getFieldMappings().get(field);
        }
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

    public Map<String, String> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(Map<String, String> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }
}
