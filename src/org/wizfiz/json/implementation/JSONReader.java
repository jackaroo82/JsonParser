package org.wizfiz.json.implementation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.wizfiz.json.IJSONParser;
import org.wizfiz.json.IJSONReader;
import org.wizfiz.json.JSONException;

public class JSONReader implements IJSONReader{
	private static final String FUNCTION_KEYWORD = "function";
	private static final Character NEW_LINE_CHARACTER = '\n';
	private static final Character RETURN_LINE_CHARACTER = '\r';
	private static final Character TAB_CHARACTER = '\t';
	private static final Character SPACE_CHARACTER = ' ';
	private static final Collection<Character> PADDING_CHARACTERS;
	static
	{
		Set<Character> temp = new HashSet<Character>();
		temp.add(NEW_LINE_CHARACTER);
		temp.add(RETURN_LINE_CHARACTER);
		temp.add(TAB_CHARACTER);
		temp.add(SPACE_CHARACTER);
		PADDING_CHARACTERS = Collections.unmodifiableCollection(temp);
	}
	private static final Collection<Character> PROCESSABLE_CHARACTERS;
	static {
		Set<Character> temp = new HashSet<Character>();
		temp.add('{');
		temp.add('}');
		temp.add('[');
		temp.add(']');
		temp.add(',');
		temp.add(':');
		temp.add('\\');
		temp.add('\"');
		temp.add('\'');
		temp.add('/');
		PROCESSABLE_CHARACTERS = Collections.unmodifiableCollection(temp);
	}
	private int stringStart = 0;
	private boolean escapeCharacter = false;
	private boolean inString = false;
	private JSONReaderAdaptor adaptor;
	private char[] characters;
	private boolean inDate = false;
	int functionDepth = 0;
	
	public IJSONReader newInstance() {
		return new JSONReader();
	}
	
	private boolean lastCharacterMatches(int index, char ch, char[] characters)
	{
		Boolean m = null;
		while(null == m)
		{
			index--;
			if (characters[index] == ch)
			{
				m = true;
			}
			else if (!PADDING_CHARACTERS.contains(characters[index]))
			{
				m = false;
			}
		}
		return m;
	}
	
	public void read(IJSONParser parser, String json) throws JSONException
	{
		char stringDelimiter = '\"';
		this.adaptor = new JSONReaderAdaptor(parser);
		this.characters = json.toCharArray();
		for(int x = 0; x < characters.length; x++)
		{
			if (PROCESSABLE_CHARACTERS.contains(characters[x])) {
				if (characters[x] == '{' && !escapeCharacter && !inString)
				{
					this.triggerStartObject(x);
				}
				else if (characters[x] == '}' && !escapeCharacter && !inString)
				{
					this.triggerEndObject(x);
				}
				else if (characters[x] == '[' && !escapeCharacter && !inString && functionDepth == 0)
				{
					this.triggerStartArray(x);
				}
				else if (characters[x] == ']' && !escapeCharacter && !inString && functionDepth == 0)
				{
					this.triggerEndArray(x);
				}
				else if (characters[x] == ',' && !escapeCharacter && !inString && functionDepth == 0)
				{
					this.triggerFieldValue(x);
				}
				else if (characters[x] == ':' && !escapeCharacter && !inString && functionDepth == 0 && !inDate)
				{
					this.triggerFieldName(x);
				}
				else if (characters[x] == '\\' && !escapeCharacter) //Always escape in a string
				{
					escapeCharacter = true;
				}
				//The delimiter has to match either ' or " and not be escaped, and if were in a string match the set delimiter
				else if ((characters[x] == '\"' && !escapeCharacter && ((inString && '\"' == stringDelimiter) || !inString))) //Allow both ' and " delimiters
				{
					inString = !inString;
					stringDelimiter = characters[x]; //Set a delimiter so we can allow the other delimiter in a string
				}
				else if ((characters[x] == '\'' && ((!escapeCharacter && '\'' == stringDelimiter && inString) || !inString))) //Allow both ' and " delimiters
				{
					inString = !inString;
					stringDelimiter = characters[x]; //Set a delimiter so we can allow the other delimiter in a string
				}
				else if (characters[x] == '/' && !inString)
				{
					inDate = true;
				}
				else
				{
					escapeCharacter = false;
				}
			} else {
				escapeCharacter = false;
			}
		}
	}
	
	private void triggerStartObject(int x) throws JSONException
	{
		inDate = false;
		//Check the previous content is not the word function
		if (functionDepth == 0 && !(x >= FUNCTION_KEYWORD.length() + 2 && new String(characters, (x - (FUNCTION_KEYWORD.length() + 2)), FUNCTION_KEYWORD.length() + 2).contains(FUNCTION_KEYWORD)))
		{
			this.adaptor.onNewObject(); //Indicate an object has been found
			//Indicate the start of a string being a field name or value as field name and value
			//may not be surrounded by a "
			stringStart = x + 1;
		}
		else
		{
			functionDepth++;;
		}
	}
	
	private void triggerEndObject(int x) throws JSONException
	{
		inDate = false;
		if (functionDepth > 0)
		{
			functionDepth--;
		}
		else
		{
			if (!this.lastCharacterMatches(x, '}', characters) && !this.lastCharacterMatches(x, '{', characters) && !this.lastCharacterMatches(x, ']', characters)) //Indicates not an empty object
			{
				this.adaptor.onValue(characters, stringStart, x - 1); //Indicate the end of the final field
			}
			this.adaptor.onEndObject(); //Indicate the end of the object
		}
	}
	
	private void triggerStartArray(int x) throws JSONException
	{
		stringStart = x + 1;
		inDate = false;
		this.adaptor.onArray(); //Indicate the start of an array
		if (!isEmptyArray(x)) {
			this.adaptor.onArrayItem(); //Indicate the start of an array item
		}
	}
	
	private boolean isEmptyArray(int x) {
		boolean empty = true;
		int index = x;
		while(empty == true && index < this.characters.length) {
			index++;
			if (']' == this.characters[index]){
				return empty;
			}
			else if (' ' != this.characters[index]) {
				empty = false;
			}
		}
		return empty;
	}
	
	private void triggerEndArray(int x) throws JSONException
	{
		inDate = false;
		if (!this.lastCharacterMatches(x, '}', characters) && !this.lastCharacterMatches(x, ']', characters) && stringStart != x) //May end an object in the array or an array in an array
		{
			this.adaptor.onValue(characters, stringStart, x - 1);
		}
		this.adaptor.onEndArray(); //Indicate the end of an array
	}
	
	private void triggerFieldValue(int x) throws JSONException
	{
		inDate = false;
		if (!this.lastCharacterMatches(x, '}', characters) && !this.lastCharacterMatches(x, ']', characters))
		{
			this.adaptor.onValue(characters, stringStart, x - 1); //Indicate the field value
		}
		if (this.adaptor.inArray())
		{
			this.adaptor.onEndArrayItem();
			this.adaptor.onArrayItem();
		}
		else
		{
			this.adaptor.onEndFieldValue(); //Indicate the end of a field value which will be an object
			this.adaptor.onEndNewField();
		}
		stringStart = x + 1;
	}
	
	private void triggerFieldName(int x) throws JSONException
	{
		//Check for a space before the name
		while(PADDING_CHARACTERS.contains(characters[stringStart]))
		{
			stringStart++;
		}
		this.adaptor.onNewField();
		int deduct = (':' == characters[x] ? 1 : 0); //Deduct 1 if we triggered by a :
		char delimiter = 'z';
		if ('\"' == characters[stringStart] && !escapeCharacter || PADDING_CHARACTERS.contains(characters[stringStart])) //Assume end character is also a "
		{
			stringStart++;
			delimiter = '\"';
		}
		if ('\'' == characters[stringStart] && !escapeCharacter || PADDING_CHARACTERS.contains(characters[stringStart])) //Assume end character is also a '
		{
			stringStart++;
			delimiter = '\'';
		}
		
		while (PADDING_CHARACTERS.contains(characters[x - deduct]) || ('\"' == characters[x - deduct] && '\"' == delimiter) || ('\'' == characters[x - deduct] && '\'' == delimiter))
		{
			deduct++;
		}
		this.adaptor.onFieldName(characters, stringStart, x - deduct); //Set the field name as a : is only used after a field name
		this.adaptor.onFieldValue(); //Indicate the start of a fields value
		//Indicate the start of a string being a field name or value as field name and value
		//may not be surrounded by a "
		stringStart = x + 1;
	}
}
