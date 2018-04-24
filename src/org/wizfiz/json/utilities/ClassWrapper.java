package org.wizfiz.json.utilities;

import static java.lang.reflect.Array.newInstance;
import static java.util.Arrays.asList;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassWrapper {
	private static final String EXPOSED_TYPE_FIELD = "TYPE";
	private final Class<?> javaClass;

	public ClassWrapper(Class<?> javaClass) {
		this.javaClass = javaClass;
	}

	public Class<?>[] getAllInterfaces() {
		Set<Class<?>> interfaces = getInterfaces(getJavaClass());
		if (getJavaClass().isInterface()) {
			interfaces.add(getJavaClass());
		}
		return interfaces.toArray(new Class<?>[interfaces.size()]);
	}
	
	private Set<Class<?>> getInterfaces(Class<?> targetClass) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for(Class<?> interfaceClass : targetClass.getInterfaces()) {
			classes.add(interfaceClass);
			classes.addAll(getInterfaces(interfaceClass));
		}
		if (null != targetClass.getSuperclass()) {
			classes.addAll(getInterfaces(targetClass.getSuperclass()));
		}
		return classes;
	}
	
	public Class<?> getJavaClass() {
		return javaClass;
	}
	
	public Method getMethod(String methodName, Object[] arguments) {
		List<Method> matchingMethodsByName = getMethodsMatchingName(methodName);
		if (matchingMethodsByName.size() == 1) {
			return matchingMethodsByName.get(0);
		} else if (matchingMethodsByName.size() > 0) {
			filterMethodsBasedOnNumberOfArguments(arguments, matchingMethodsByName);
			if (matchingMethodsByName.size() == 1) {
				return matchingMethodsByName.get(0);
			} else {
				filterMethodsWithDifferentArgumentTypes(arguments, matchingMethodsByName);
				if (matchingMethodsByName.size() == 1) {
					return matchingMethodsByName.get(0);
				}
			}
		}
		return null;
	}
	
	private List<Method> getMethodsMatchingName(String methodName) {
		List<Method> matchingMethodsByName = new ArrayList<Method>();
		for(Method m : getJavaClass().getMethods()) {
			if (m.getName().equals(methodName)) {
				matchingMethodsByName.add(m);
			}
		}
		return matchingMethodsByName;
	}
	
	private void filterMethodsBasedOnNumberOfArguments(Object[] arguments,
			List<Method> matchingMethodsByName) {
		for(int x = 0; x < matchingMethodsByName.size(); x++) {
			if (matchingMethodsByName.get(x).getParameterTypes().length != arguments.length) {
				matchingMethodsByName.remove(x);
				x--;
			}
		}
	}
	
	private void filterMethodsWithDifferentArgumentTypes(Object[] arguments, List<Method> matchingMethodsByName) {
		for (int x = 0; x < matchingMethodsByName.size(); x++) {
			List<Class<?>> methodArguments = mapClassList(matchingMethodsByName.get(x).getParameterTypes());
			for(Object arg : arguments) {
				if (!listContainsClassOrSubClassOfItem(methodArguments, arg)) {
					matchingMethodsByName.remove(matchingMethodsByName.get(x));
					x--;
					break;
				}
			}
		}
	}
	
	private List<Class<?>> mapClassList(Class<?>[] toMap) {
		List<Class<?>> mapped = new ArrayList<Class<?>>();
		for(Class<?> classToMap: toMap) {
			mapped.add(mapClass(classToMap));
		}
		return mapped;
	}
	
	private boolean listContainsClassOrSubClassOfItem(List<Class<?>> classes, Object item) {
		List<Class<?>> classesClone = new ArrayList<Class<?>>(classes);
		List<Class<?>> classesToMatch = new ArrayList<Class<?>>();
		if (item.getClass().isArray() && Array.getLength(item) > 0) {
			Class<?> classToMatch = mapClass(Array.get(item, 0).getClass());
			for(Class<?> subType : new ClassWrapper(classToMatch).getAllInterfaces()) {
				classesToMatch.add(newInstance(subType, 0).getClass());
			}
			classesToMatch.add(item.getClass());
		} else {
			classesToMatch.addAll(asList(new ClassWrapper(item.getClass()).getAllInterfaces()));
			classesToMatch.add(item.getClass());
			classesToMatch.add(mapClass(item.getClass()));
		}
		classesClone.retainAll(classesToMatch);
		return classesClone.size() > 0;
	}
	
	private Class<?> mapClass(Class<?> classToConvert) {
		if (Number.class.equals(classToConvert.getSuperclass()) || Boolean.class.equals(classToConvert) || Character.class.equals(classToConvert)) {
			try {
				return (Class<?>) classToConvert.getField(EXPOSED_TYPE_FIELD).get(null);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return classToConvert;
			}
		} else {
			return classToConvert;
		}
	}
}
