package org.wizfiz.json;

public class JSONException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_MESSAGE = "JSON Parser Exception";
	public JSONException()
	{
		super(DEFAULT_MESSAGE);
	}
	public JSONException(String message)
	{
		super(message);
	}
	public JSONException(String message, Throwable e)
	{
		super(message, e);
	}
}
