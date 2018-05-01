package org.wizfiz.json;

import java.util.Map;


public interface IJSONFactory {

	void tagObject(IJSONObject object, String tagValue)  throws JSONException;

	IJSONObject createObject()  throws JSONException;

	IJSONField createField(String name, Object value) throws JSONException;

	IJSONValue createValue(Object value) throws JSONException;

	void setDefaultFieldCreator(IFieldCreator defaultFieldCreator);

	IFieldCreator getDefaultFieldCreator();

	void setObjectCreator(IObjectCreator objectCreator);

	IObjectCreator getObjectCreator();

	void setFieldCreators(Map<Class<?>, IFieldCreator> fieldCreators);

	void setValueCreators(Map<Class<?>, IValueCreator> valueCreators);

	void setDefaultValueCreator(IValueCreator defaultValueCreator);

	IValueCreator getDefaultValueCreator();

	void setObjectTagger(IObjectTagger objectTagger);

	IObjectTagger getObjectTagger();
	
}
