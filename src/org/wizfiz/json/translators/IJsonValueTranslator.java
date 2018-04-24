package org.wizfiz.json.translators;

import java.util.List;

public interface IJsonValueTranslator {
    Object translate(String var1);
    default void setSubTranslators(List<IJsonValueTranslator> subTranslators){

    };

    boolean isApplicable(String var1);
}
