package org.wizfiz.json.utilities.samples;

import java.util.List;
import java.util.Map;

public class ComplexPojo {
    private SimplePojo pojoField;
    private Integer[] arrayField;
    private Map<String, String> mapField;
    private List<Integer> listField;

    public SimplePojo getPojoField() {
        return pojoField;
    }

    public void setPojoField(SimplePojo pojoField) {
        this.pojoField = pojoField;
    }

    public Integer[] getArrayField() {
        return arrayField;
    }

    public void setArrayField(Integer[] arrayField) {
        this.arrayField = arrayField;
    }

    public Map<String, String> getMapField() {
        return mapField;
    }

    public void setMapField(Map<String, String> mapField) {
        this.mapField = mapField;
    }

    public List<Integer> getListField() {
        return listField;
    }

    public void setListField(List<Integer> listField) {
        this.listField = listField;
    }
}
