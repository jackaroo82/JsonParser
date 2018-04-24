package org.wizfiz.json.translators;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemPropertyTranslator implements IJsonValueTranslator {
    private static final Logger LOGGER = Logger.getLogger("SystemPropertyTranslator");
    private List<IJsonValueTranslator> subTranslators;
    @Override
    public Object translate(String value) {
        String key = value.substring(1);
        String rawValue = System.getProperty(key);
        if (null == rawValue) {
            rawValue = System.getenv(key);
        }

        LOGGER.log(Level.FINE, String.format("Setting property %s to %s", key, rawValue));

        for(IJsonValueTranslator translator: subTranslators) {
            if (translator.isApplicable(rawValue)) {
                return translator.translate(rawValue);
            }
        }
        return rawValue;
    }

    @Override
    public boolean isApplicable(String value) {
        return value.startsWith("@");
    }

    public List<IJsonValueTranslator> getSubTranslators() {
        return subTranslators;
    }

    public void setSubTranslators(List<IJsonValueTranslator> subTranslators) {
        this.subTranslators = subTranslators;
    }
}
