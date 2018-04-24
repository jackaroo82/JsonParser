package org.wizfiz.json;

public interface IJSONReader {

	void read(IJSONParser parser, String json) throws JSONException;

	IJSONReader newInstance();
}
