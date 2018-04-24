package org.wizfiz.json.implementation;

import java.util.ArrayList;
import java.util.List;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONItem;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONParser;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.translators.IJsonValueTranslator;

public class JSONParser implements IJSONParser{

	private enum ITEM_TYPE
	{
		FIELD, FIELDVALUE, ARRAY, ARRAYITEM, OBJECT
	}
	
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
	
	private List<IJSONItem> arrayRoot;
	private IJSONObject root;
	private final List<IJSONValue> values;
	private final List<IJSONObject> children;
	private final List<List<IJSONItem>> arrays;
	private final List<IJSONField> fields;
	private final List<ITEM_TYPE> paths;
	private IJSONFactory jsonFactory;
	private List<IJsonValueTranslator> translators;
	
	public JSONParser()
	{
		paths = new ArrayList<ITEM_TYPE>();
		this.children = new ArrayList<IJSONObject>();
		this.arrays = new ArrayList<List<IJSONItem>>();
		this.values = new ArrayList<IJSONValue>();
		this.fields = new ArrayList<IJSONField>();
	}
	
	@Override
	public IJSONParser newInstance() {
		JSONParser newParser = new JSONParser();
		newParser.setJsonFactory(getJsonFactory());
		newParser.setTranslators(translators);
		return newParser;
	}
	
	private void windBack(ITEM_TYPE toItem) throws JSONException
	{
		//Ensures we wind back to the required depth
		while (!paths.get(paths.size() - 1).equals(toItem))
		{
			switch(paths.get(paths.size() - 1))
			{
				case FIELD:
					this.onEndNewField();
				break;
				case FIELDVALUE:
					this.onEndFieldValue();
				break;
				case OBJECT:
					this.onEndObject();
				break;
				case ARRAYITEM:
					this.onEndArrayItem();
					break;
				case ARRAY:
					this.onEndArray();
					break;
				default:
					
			}
		}
		this.paths.remove(this.paths.size() - 1);
	}
	
	@Override
	public void onArray()
	{
		paths.add(ITEM_TYPE.ARRAY);
		//Add a new array
		List<IJSONItem> newArray = new ArrayList<IJSONItem>();

		if (null == root && null == arrayRoot)
		{
			arrayRoot = newArray;
		}
		this.arrays.add(newArray);
	}
	
	@Override
	public void onEndArray() throws JSONException
	{
		this.windBack(ITEM_TYPE.ARRAY);
		//Remove the last array and add it to the last value
		List<IJSONItem> array = this.arrays.remove(this.arrays.size() - 1);
		if (!array.equals(this.arrayRoot))
		{
			this.values.set(this.values.size() - 1, getJsonFactory().createValue(array.toArray(new IJSONItem[array.size()])));
		}
	}
	
	@Override
	public void onArrayItem() throws JSONException
	{
		paths.add(ITEM_TYPE.ARRAYITEM);
		//Add a new value to store the array value into
		this.values.add(getJsonFactory().createValue(false));
	}
	
	@Override
	public void onEndArrayItem() throws JSONException
	{
		this.windBack(ITEM_TYPE.ARRAYITEM);
		//Remove the last value and set it onto the last array
		IJSONValue val = this.values.remove(this.values.size() - 1);
		this.arrays.get(this.arrays.size() - 1).add(val);
	}
	
	@Override
	public void onNewField() throws JSONException
	{
		paths.add(ITEM_TYPE.FIELD);
		//Add the field
		this.fields.add(getJsonFactory().createField("TEMP", (String)null));
	}
	
	@Override
	public void onEndNewField() throws JSONException
	{
		this.windBack(ITEM_TYPE.FIELD);
		//Remove the last field
		IJSONField f = this.fields.remove(this.fields.size() - 1);
		//Add it to the last object
		this.children.get(this.children.size() - 1).addField(f);
	}
	
	@Override
	public void onFieldValue() throws JSONException
	{
		paths.add(ITEM_TYPE.FIELDVALUE);
		//Add a new value
		this.values.add(getJsonFactory().createValue((String)null));
	}
	
	@Override
	public void onEndFieldValue() throws JSONException
	{
		this.windBack(ITEM_TYPE.FIELDVALUE);
		//Remove the last value
		IJSONValue val = this.values.remove(this.values.size() - 1);
		//Add it to the last field
		this.fields.get(this.fields.size() - 1).setValue(val);
	}
	
	@Override
	public void onNewObject() throws JSONException
	{
		paths.add(ITEM_TYPE.OBJECT);
		//Create a new object
		IJSONObject o = getJsonFactory().createObject();
		if (null == root && null == arrayRoot)
		{
			//If root is not set this is the root
			root = o;
		}
		//Add to the children
		children.add(o);
	}
	
	@Override
	public void onEndObject() throws JSONException
	{
		this.windBack(ITEM_TYPE.OBJECT);
		//Remove the last object
		IJSONObject o = this.children.remove(this.children.size() - 1);
		//If this isn't the root
		if (!o.equals(root))
		{
			//Set as the value of the last value
			this.values.set(this.values.size() - 1, getJsonFactory().createValue(o));
		}
	}
	
	@Override
	public IJSONObject getJson() {
		if (null != arrayRoot)
		{
			return null;
		}
		return this.root;
	}
	
	@Override
	public void onFieldName(char[] characters, int start, int end) throws JSONException {
		//Set the name into a string
		String name = new String(characters, start, (end - (start - 1))).trim();
		//Set the name of the last field
		this.fields.get(this.fields.size() - 1).setName(name);
	}
	
	@Override
	public void onValue(char[] characters, int start, int end) throws JSONException {
		//Translate the value into the correct type
		Object val = this.translateValue(new String(characters, start, (end - start) + 1).trim());
		//Set onto the last value
		this.values.set(this.values.size() - 1, getJsonFactory().createValue(val));
	}
	
	/**
	 * Takes the input value and evaluates if this is a string, date, integer
	 * decimal or function
	 * @param value The value to translate
	 * @return The translated value
	 */
	Object translateValue(String value)
	{
		for(IJsonValueTranslator translator : getTranslators()) {
			if (translator.isApplicable(value)) {
				return translator.translate(value);
			}
		}
		return value;
	}

	public List<IJsonValueTranslator> getTranslators() {
		return translators;
	}

	public void setTranslators(List<IJsonValueTranslator> translators) {
		this.translators = translators;
	}
	
	@Override
	public IJSONValue[] getArray() {
		return (null != this.arrayRoot ? this.arrayRoot.toArray(new IJSONValue[this.arrayRoot.size()]) : null);
	}

	public IJSONFactory getJsonFactory() {
		return jsonFactory;
	}

	public void setJsonFactory(IJSONFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
	}
}
