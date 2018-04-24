package org.wizfiz.json.creators;

import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IObjectCreator;
import org.wizfiz.json.implementation.JSONObject;

public class DefaultObjectCreator implements IObjectCreator{

	@Override
	public IJSONObject createObject() {
		return new JSONObject();
	}
	
}
