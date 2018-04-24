package org.wizfiz.json.translators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTranslator implements IJsonValueTranslator {
    private static final Logger LOGGER = Logger.getLogger("DateTranslator");
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
    private static final String REGEX = "^[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,4} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}(.[0-9]{1,3})?$";
    private final SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);

    @Override
    public Object translate(String value) {
        try {
            return formatter.parse(trimQuotes(value));
        } catch (ParseException e) {
            LOGGER.log(Level.WARNING, "Error formatting date");
        }
        return null;
    }

    @Override
    public boolean isApplicable(String value) {
        return trimQuotes(value).matches(REGEX);
    }

    private String trimQuotes(String value) {
        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'") )) {
            value = value.substring(1, value.length() -1);
        }
        return value;
    }
}
