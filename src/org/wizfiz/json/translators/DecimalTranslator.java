package org.wizfiz.json.translators;

import java.math.BigDecimal;

public class DecimalTranslator implements IJsonValueTranslator {

    public static final String REGEX = "^[0-9]*\\.[0-9]*$";

    @Override
    public Object translate(String value) {
        return new BigDecimal(value);
    }

    @Override
    public boolean isApplicable(String value) {
        return value.matches(REGEX);
    }
}
