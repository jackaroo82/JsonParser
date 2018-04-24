package org.wizfiz.json.utilities;

import org.wizfiz.json.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;

public class ValueHelper implements IValueHelper {
	private static final Logger LOGGER = Logger.getLogger("ValueHelper");
	private List<String> jsonTypes;
	private IJSONFactory jsonFactory = SystemParserCreator.SYSTEM_JSON_FACTORY;

	public ValueHelper() {
		setJsonTypes(asList(
				"java.lang.String",
				"java.lang.Integer",
				"java.lang.Boolean",
				"java.lang.Character",
				"java.lang.Double",
				"java.lang.Float",
				"java.lang.Byte",
				"java.lang.Long",
				"java.lang.Short",
				"java.util.Date",
				"java.math.BigDecimal"
		));
	}

	@Override
	public boolean isJsonType(Class<?> classToEvaluate) {
		return classToEvaluate.isPrimitive() || classToEvaluate.equals(Object.class) || getJsonTypes().contains(classToEvaluate.getName());
	}

	public List<String> getJsonTypes() {
		return jsonTypes;
	}

	public void setJsonTypes(List<String> jsonTypes) {
		this.jsonTypes = jsonTypes;
	}

	public IJSONFactory getJsonFactory() {
		return jsonFactory;
	}

	public void setJsonFactory(IJSONFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
	}

	@Override
	public Object getJsonValue(Object o) throws JSONException {
		if (null == o) {
			return o;
		}
		Object returnVal = null;
		Class<?> classToIntrospect = o.getClass();
		if (classToIntrospect.isArray()) {
			List<Object> list = new ArrayList<>();
			Object[] arrayCast = (Object[])o;
			for(int x = 0; x < arrayCast.length; x++) {
				list.add(arrayCast[x]);
			}
			o = list;
			classToIntrospect = o.getClass();
		}
		if (classToIntrospect.equals(Class.class)) {
			returnVal = ((Class<?>)o).getName();
		}
		else if (isJsonType(classToIntrospect)) {
			returnVal = o;
		} else if (asList(new ClassWrapper(classToIntrospect).getAllInterfaces()).contains(Collection.class)) {
			Collection<?> collection = (Collection<?>)o;
			IJSONItem[] returnArray = new IJSONItem[collection.size()];
			Object[] collectionArray = collection.toArray();
			for(int x = 0; x < collection.size(); x++) {
				returnArray[x] = getJsonFactory().createValue(getJsonValue(collectionArray[x]));
			}
			returnVal = returnArray;
		}else if (asList(new ClassWrapper(classToIntrospect).getAllInterfaces()).contains(Map.class)) {
			Map<?, ?> map = (Map<?, ?>)o;
			IJSONObject mapped = getJsonFactory().createObject();
			for(Object key : map.keySet()) {
				mapped.addField(getJsonFactory().createField(key.toString(), getJsonValue(map.get(key))));
			};
			returnVal = mapped;
		} else {
			returnVal = jsonIfyObject(o);
		}
		return returnVal;
	}

	@Override
	public IJSONObject jsonIfyObject(Object o) throws JSONException {
		if (o instanceof IJSONObject) {
			return (IJSONObject) o;
		}
		IJSONObject data = getJsonFactory().createObject();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(o.getClass());
		} catch (IntrospectionException e) {
			throw new JSONException("Error introspecting bean", e);
		}
		List<PropertyDescriptor> propertyDescriptors = asList(info.getPropertyDescriptors());
		sort(propertyDescriptors, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
		for(PropertyDescriptor descriptor : propertyDescriptors) {
			try {
				if (null == o) {
					LOGGER.log(Level.INFO, "Object is null");
				}
				if (null == descriptor) {
					LOGGER.log(Level.INFO, "Descriptor is null");
				}
				if (null == descriptor.getReadMethod()) {
					LOGGER.log(Level.INFO, format("Descriptor read method is null for %s on class %s", descriptor.getName(), o.getClass().getName()));
				}
				Object value = getJsonValue(descriptor.getReadMethod().invoke(o));
				IJSONField field = getJsonFactory().createField(descriptor.getName(), value);
				data.addField(field);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new JSONException("Error getting bean value", e);
			}
		}
		return data;
	}

}
