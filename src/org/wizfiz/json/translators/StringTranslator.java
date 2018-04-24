package org.wizfiz.json.translators;

public class StringTranslator implements IJsonValueTranslator {

    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "\"";

    @Override
    public Object translate(String value) {
        String returnV = value.substring(1, value.length() -1);
        if (returnV.contains("\\'")){
            returnV = returnV.replaceAll("\\\\'", "'");
        }
        if (returnV.contains("\\\"")){
            returnV = returnV.replaceAll("\\\\\"", "\"");
        }
        return returnV;
    }

    @Override
    public boolean isApplicable(String value) {
        return value.startsWith(SINGLE_QUOTE) && value.endsWith(SINGLE_QUOTE) || value.startsWith(DOUBLE_QUOTE) && value.endsWith(DOUBLE_QUOTE);
    }
}
