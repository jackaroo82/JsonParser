package org.wizfiz.json.implementation;

import java.util.Map;

import org.wizfiz.json.IFieldCreator;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.IObjectCreator;
import org.wizfiz.json.IObjectTagger;
import org.wizfiz.json.IValueCreator;
import org.wizfiz.json.JSONException;

public class JSONFactory implements IJSONFactory {
	private IObjectCreator objectCreator;
	private Map<Class<?>, IFieldCreator> fieldCreators;
	private IFieldCreator defaultFieldCreator;
	private Map<Class<?>, IValueCreator> valueCreators;
	private IValueCreator defaultValueCreator;
	private IObjectTagger objectTagger;
	
	@Override
	public void tagObject(IJSONObject object, String tagValue)  throws JSONException {
		getObjectTagger().tagObject(object, tagValue);
		object.addTag(tagValue);
	}

	@Override
	public IJSONObject createObject() throws JSONException {
		return getObjectCreator().createObject();
	}

	@Override
	public IJSONField createField(String name, Object value) throws JSONException {
		IFieldCreator creator;
		if (null != value && getFieldCreators().containsKey(value.getClass())) {
			creator = getFieldCreators().get(value.getClass());
		} else {
			creator = getDefaultFieldCreator();
		}
		return creator.createField(name, value);
	}

	@Override
	public IJSONValue createValue(Object value)  throws JSONException {
		IValueCreator creator;
		Class<? extends Object> key = null == value ? null : value.getClass();
		if (getValueCreators().containsKey(key)) {
			creator = getValueCreators().get(key);	
		} else {
			creator = getDefaultValueCreator();
		}
		return creator.createValue(value);
	}

	public IObjectCreator getObjectCreator() {
		return objectCreator;
	}

	@Override
	public void setObjectCreator(IObjectCreator objectCreator) {
		this.objectCreator = objectCreator;
	}

	public Map<Class<?>, IFieldCreator> getFieldCreators() {
		return fieldCreators;
	}

	@Override
	public void setFieldCreators(Map<Class<?>, IFieldCreator> fieldCreators) {
		this.fieldCreators = fieldCreators;
	}

	public IFieldCreator getDefaultFieldCreator() {
		return defaultFieldCreator;
	}

	@Override
	public void setDefaultFieldCreator(IFieldCreator defaultFieldCreator) {
		this.defaultFieldCreator = defaultFieldCreator;
	}

	public Map<Class<?>, IValueCreator> getValueCreators() {
		return valueCreators;
	}

	@Override
	public void setValueCreators(Map<Class<?>, IValueCreator> valueCreators) {
		this.valueCreators = valueCreators;
	}

	public IValueCreator getDefaultValueCreator() {
		return defaultValueCreator;
	}

	@Override
	public void setDefaultValueCreator(IValueCreator defaultValueCreator) {
		this.defaultValueCreator = defaultValueCreator;
	}

	public IObjectTagger getObjectTagger() {
		return objectTagger;
	}

	@Override
	public void setObjectTagger(IObjectTagger objectTagger) {
		this.objectTagger = objectTagger;
	}

}
