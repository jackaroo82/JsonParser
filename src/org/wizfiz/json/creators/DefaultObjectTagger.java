package org.wizfiz.json.creators;

import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IObjectTagger;

public class DefaultObjectTagger implements IObjectTagger {

	@Override
	public void tagObject(IJSONObject object, String tagValue) {
		object.addTag(tagValue);
	}
}
