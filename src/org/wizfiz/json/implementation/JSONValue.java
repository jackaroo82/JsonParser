package org.wizfiz.json.implementation;

import static org.wizfiz.json.implementation.JSONParser.DATE_TIME_FORMAT;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.wizfiz.json.IJSONItem;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;

public class JSONValue implements IJSONValue {

	protected Object value;

	@Override
	public String toJSON() throws JSONException {
		String output = "";
        Object value2 = getValue();
		if (null == value2)
        {
            output = "null";
        }
        else if (getValue() instanceof Date)
        {
        	SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date itm = ((Date)getValue());
            output = "\"" + formatter.format(itm) + "\"";
        }
        else if (getValue() instanceof Integer)
        {
            output = ((Integer)getValue()).toString();
        }
        else if (getValue() instanceof Byte) {
        	output = ((Byte)getValue()).toString();
        }
        else if (getValue() instanceof String)
        {
            output = "\"" + ((String)getValue()).replace("\"", "\\\"") + "\"";
        }
        else if (getValue().getClass().isArray())
        {
            Object[] cast = (Object[])getValue();
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            for (int x = 0; x < cast.length; x++)
            {
                builder.append(((IJSONItem)cast[x]).toJSON());
                if (x < cast.length - 1)
                {
                    builder.append(",");
                }
            }
            builder.append("]");
            output = builder.toString();
        }
        else if (getValue() instanceof IJSONObject)
        {
            output = ((IJSONObject)getValue()).toJSON();
        }
        else if (getValue() instanceof BigDecimal)
        {
            output = ((BigDecimal)getValue()).toString();
        }
        else if (getValue() instanceof Boolean)
        {
            output = getValue().toString().toLowerCase();
        }
        return output;
	}

	@Override
	public Object getValue() {
		return value;
	}

	public void setValue(Object val) {
		this.value = val;
	}

}
