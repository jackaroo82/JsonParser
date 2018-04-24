package org.wizfiz.json.implementation;

import java.util.ArrayList;
import java.util.List;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONParser;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.translators.IJsonValueTranslator;

public class JSONReaderAdaptor implements IJSONParser{
		private enum ITEM_TYPE
		{
			FIELD, FIELDVALUE, ARRAY, ARRAYITEM, OBJECT
		}
		
		private final List<ITEM_TYPE> paths;
		private final IJSONParser parser;
		
		public JSONReaderAdaptor(IJSONParser parser)
		{
			paths = new ArrayList<ITEM_TYPE>();
			this.parser = parser;
		}
		
		@Override
		public IJSONParser newInstance() {
			return new JSONReaderAdaptor(parser.newInstance());
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
			this.parser.onArray();
		}
		
		@Override
		public void onEndArray() throws JSONException
		{
			this.windBack(ITEM_TYPE.ARRAY);
			this.parser.onEndArray();
		}
		
		@Override
		public void onArrayItem() throws JSONException
		{
			paths.add(ITEM_TYPE.ARRAYITEM);
			this.parser.onArrayItem();
		}
		
		@Override
		public void onEndArrayItem() throws JSONException
		{
			this.windBack(ITEM_TYPE.ARRAYITEM);
			this.parser.onEndArrayItem();
		}
		
		@Override
		public void onNewField() throws JSONException
		{
			paths.add(ITEM_TYPE.FIELD);
			this.parser.onNewField();
		}
		
		@Override
		public void onEndNewField() throws JSONException
		{
			this.windBack(ITEM_TYPE.FIELD);
			this.parser.onEndNewField();
		}
		
		public void onFieldValue() throws JSONException
		{
			paths.add(ITEM_TYPE.FIELDVALUE);
			this.parser.onFieldValue();
		}
		
		public boolean inArray()
		{
			ITEM_TYPE fieldType = ITEM_TYPE.OBJECT;
			for(int x = this.paths.size() - 1; (fieldType.equals(ITEM_TYPE.OBJECT) && x >=0); x--)
			{
				if (this.paths.get(x).equals(ITEM_TYPE.ARRAY) || this.paths.get(x).equals(ITEM_TYPE.FIELD))
				{
					fieldType = this.paths.get(x);
				}
			}
			return ITEM_TYPE.ARRAY.equals(fieldType);
		}
		
		public void onEndFieldValue() throws JSONException
		{
			this.windBack(ITEM_TYPE.FIELDVALUE);
			this.parser.onEndFieldValue();
		}
		
		public void onNewObject() throws JSONException
		{
			paths.add(ITEM_TYPE.OBJECT);
			this.parser.onNewObject();
		}
		
		public void onEndObject() throws JSONException
		{
			this.windBack(ITEM_TYPE.OBJECT);
			this.parser.onEndObject();
		}
		
		@Override
		public IJSONObject getJson() {
			return this.parser.getJson();
		}
		
		@Override
		public void onFieldName(char[] characters, int start, int end) throws JSONException {
			this.parser.onFieldName(characters, start, end);
		}
		
		@Override
		public void onValue(char[] characters, int start, int end) throws JSONException {
			this.parser.onValue(characters, start, end);
		}
		
		@Override
		public IJSONValue[] getArray() {
			return this.parser.getArray();
		}

		@Override
		public IJSONFactory getJsonFactory() {
			return parser.getJsonFactory();
		}

	@Override
	public void setJsonFactory(IJSONFactory ijsonFactory) {

	}

	@Override
	public void setTranslators(List<IJsonValueTranslator> list) {

	}

}