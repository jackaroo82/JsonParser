package org.wizfiz.json.translators;

import static java.lang.Boolean.valueOf;

public class BooleanTranslator implements IJsonValueTranslator {
    @Override
    public Object translate(String value) {
        return valueOf(value);
    }

    @Override
    public boolean isApplicable(String value) {
        return valueOf(value).toString().equals(value);
    }
}
