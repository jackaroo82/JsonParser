package org.wizfiz.json.translators;

import static java.lang.Integer.valueOf;

public class IntegerTranslator implements IJsonValueTranslator {

    public static final String REGEX = "^-?[0-9]*$";

    @Override
    public Object translate(String value) {
        return valueOf(value);
    }

    @Override
    public boolean isApplicable(String value) {
        return value.matches(REGEX);
    }
}