package org.wizfiz.json;

import org.wizfiz.json.translators.IJsonValueTranslator;
import java.util.*;

public class SystemParserCreator {
    public static IJSONToObjectParser SYSTEM_PARSER;
    public static IJSONFactory SYSTEM_JSON_FACTORY;
    static {
        try {
            SYSTEM_JSON_FACTORY = createJsonFactory();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            SYSTEM_JSON_FACTORY = null;
        }
        try {
            SYSTEM_PARSER = createObjectParser();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            SYSTEM_PARSER = null;
        }
    }

    private static IJSONToObjectParser createObjectParser() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        IJSONToObjectParser parser = (IJSONToObjectParser)Class.forName(System.getProperty("objectParserClass", "org.wizfiz.json.implementation.JSONToObjectParser")).newInstance();
        IJSONReader reader = (IJSONReader)Class.forName(System.getProperty("jsonReaderClass", "org.wizfiz.json.implementation.JSONReader")).newInstance();
        parser.setParser(createJsonParser());
        parser.setReader(reader);
        return parser;
    }

    private static IJSONParser createJsonParser() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        IJSONParser parser = (IJSONParser)Class.forName(System.getProperty("jsonParserClass", "org.wizfiz.json.implementation.JSONParser")).newInstance();
        parser.setJsonFactory(SYSTEM_JSON_FACTORY);


        parser.setTranslators(getTranslators());
        return parser;
    }

    public static List<IJsonValueTranslator> getTranslators() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] jsonTranslators = System.getProperty("jsonTranslatorsList", "org.wizfiz.json.translators.NullTranslator,org.wizfiz.json.translators.SystemPropertyTranslator,org.wizfiz.json.translators.BooleanTranslator,org.wizfiz.json.translators.DecimalTranslator,org.wizfiz.json.translators.IntegerTranslator,org.wizfiz.json.translators.DateTranslator,org.wizfiz.json.translators.StringTranslator").split(",");
        String[] subTranslators = System.getProperty("classesWithSubTranslators", "org.wizfiz.json.translators.SystemPropertyTranslator").split(",");
        List<IJsonValueTranslator> translators = new ArrayList<>();
        Map<String, IJsonValueTranslator> mappedTranslators = new HashMap<>();
        for(String translator : jsonTranslators) {
            IJsonValueTranslator t = (IJsonValueTranslator)Class.forName(translator).newInstance();
            translators.add(t);
            mappedTranslators.put(translator, t);
        }
        for(String translator : subTranslators) {
            List<IJsonValueTranslator> subList = new ArrayList<>(translators);
            IJsonValueTranslator translatorInstance = mappedTranslators.get(translator);
            subList.remove(translatorInstance);
            translatorInstance.setSubTranslators(subList);
        }
        return translators;
    }

    private static IJSONFactory createJsonFactory() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        IJSONFactory factory = (IJSONFactory)Class.forName(System.getProperty("jsonFactoryClass", "org.wizfiz.json.implementation.JSONFactory")).newInstance();
        IFieldCreator fieldCreator = (IFieldCreator)Class.forName(System.getProperty("fieldCreatorClass", "org.wizfiz.json.creators.DefaultFieldCreator")).newInstance();
        fieldCreator.setJsonFactory(factory);
        IObjectCreator objectCreator = (IObjectCreator) Class.forName(System.getProperty("objectCreatorClass", "org.wizfiz.json.creators.DefaultObjectCreator")).newInstance();
        IValueCreator valueCreator = (IValueCreator) Class.forName(System.getProperty("valueCreatorClass", "org.wizfiz.json.creators.DefaultValueCreator")).newInstance();
        IObjectTagger objectTagger = (IObjectTagger)  Class.forName(System.getProperty("objectTaggerClass", "org.wizfiz.json.creators.DefaultObjectTagger")).newInstance();

        factory.setDefaultFieldCreator(fieldCreator);
        factory.setObjectCreator(objectCreator);
        factory.setDefaultValueCreator(valueCreator);
        factory.setObjectTagger(objectTagger);
        factory.setValueCreators(new HashMap<>());
        factory.setFieldCreators(new HashMap<>());
        return factory;
    }
}
