package org.wizfiz.json.utilities;

import java.util.Arrays;
import java.util.Map;
import org.junit.Test;
import org.wizfiz.json.IJSONItem;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.utilities.samples.ComplexPojo;
import org.wizfiz.json.utilities.samples.SimplePojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ValueHelperTest {
    ValueHelper testObj = new ValueHelper();

    @Test
    public void jsonIfyObjectForPojo() throws JSONException {
        Date dateValue = new Date();

        SimplePojo pojo = getSimplePojo(dateValue);

        IJSONObject result = testObj.jsonIfyObject(pojo);

        verifySimplePojo(dateValue, result);
    }

    @Test
    public void jsonIfyObjectForComplexPojo() throws JSONException {
        Date dateValue = new Date();
        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("one", "value 1");
        mapValue.put("two", "value 2");
        SimplePojo pojo = getSimplePojo(dateValue);
        ComplexPojo complexPojo = new ComplexPojo();
        complexPojo.setArrayField(new Integer[]{1, 3, 2});
        complexPojo.setPojoField(pojo);
        complexPojo.setMapField(mapValue);
        complexPojo.setListField(Arrays.asList(1, 2, 4));

        IJSONObject result = testObj.jsonIfyObject(complexPojo);

        verifySimplePojo(dateValue, (IJSONObject) result.getField("pojoField").getValue().getValue());
        IJSONItem[] arrayField = (IJSONItem[]) result.getField("arrayField").getValue().getValue();
        assertEquals(3, arrayField.length);
        assertEquals(1, ((IJSONValue)arrayField[0]).getValue());
        assertEquals(3, ((IJSONValue)arrayField[1]).getValue());
        assertEquals(2, ((IJSONValue)arrayField[2]).getValue());

        IJSONItem[] listField = (IJSONItem[]) result.getField("listField").getValue().getValue();
        assertEquals(3, arrayField.length);
        assertEquals(1, ((IJSONValue)listField[0]).getValue());
        assertEquals(2, ((IJSONValue)listField[1]).getValue());
        assertEquals(4, ((IJSONValue)listField[2]).getValue());

        IJSONObject mapField = (IJSONObject) result.getField("mapField").getValue().getValue();
        assertEquals("value 1", mapField.getField("one").getValue().getValue());
        assertEquals("value 2", mapField.getField("two").getValue().getValue());
    }

    private void verifySimplePojo(Date dateValue, IJSONObject result) {
        assertTrue((Boolean) result.getField("booleanValue").getValue().getValue());
        assertEquals(dateValue, result.getField("dateValue").getValue().getValue());
        assertEquals(new BigDecimal("100.32"), result.getField("decimalValue").getValue().getValue());
        assertEquals((Integer)11, result.getField("intValue").getValue().getValue());
        assertEquals("some value", result.getField("stringValue").getValue().getValue());
    }

    private SimplePojo getSimplePojo(Date dateValue) {
        SimplePojo pojo = new SimplePojo();
        pojo.setBooleanValue(true);
        pojo.setDateValue(dateValue);
        pojo.setDecimalValue(new BigDecimal("100.32"));
        pojo.setIntValue(11);
        pojo.setStringValue("some value");
        return pojo;
    }
}