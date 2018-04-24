package org.wizfiz.json.implementation;

import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONParser;
import org.wizfiz.json.IJSONReader;
import org.wizfiz.json.IJSONToObjectParser;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;

public class JSONToObjectParser implements IJSONToObjectParser{

	private IJSONParser parser;
	private IJSONReader reader;
	
	@Override
	public IJSONObject parseJson(String json) throws JSONException {
		IJSONReader reader = getReader().newInstance();
		IJSONParser parser = getParser().newInstance();
		
		reader.read(parser, json);
		return parser.getJson();
	}

	@Override
	public IJSONValue[] parseJsonArray(String json) throws JSONException {
		IJSONReader reader = getReader().newInstance();
		IJSONParser parser = getParser().newInstance();
		reader.read(parser, json);
		return parser.getArray();
	}
	
	public IJSONParser getParser() {
		return parser;
	}

	@Override
	public void setParser(IJSONParser parser) {
		this.parser = parser;
	}

	public IJSONReader getReader() {
		return reader;
	}

	@Override
	public void setReader(IJSONReader reader) {
		this.reader = reader;
	}

	@Override
	public IJSONFactory getJsonFactory() {
		return getParser().getJsonFactory();
	}
}
