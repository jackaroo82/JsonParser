package org.wizfiz.json.translators;

public class NullTranslator implements IJsonValueTranslator {

    public static final String NULL_VALUE = "null";

    @Override
    public Object translate(String value) {
        return null;
    }

    @Override
    public boolean isApplicable(String value) {
        return null == value || NULL_VALUE.equals(value);
    }
}
