package org.wizfiz.json;

public interface IJSONToObjectParser {
	IJSONObject parseJson(String json) throws JSONException;
	IJSONValue[] parseJsonArray(String json) throws JSONException;
	IJSONFactory getJsonFactory();
	void setParser(IJSONParser parser);
	void setReader(IJSONReader reader);
}
