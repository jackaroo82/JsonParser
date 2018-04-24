package org.wizfiz.json.implementation;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.wizfiz.json.*;
import org.wizfiz.json.implementation.JSONField;
import org.wizfiz.json.implementation.JSONObject;
import org.wizfiz.json.implementation.JSONParser;
import org.wizfiz.json.implementation.JSONReader;
import org.wizfiz.json.implementation.JSONValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.wizfiz.json.translators.IJsonValueTranslator;

@RunWith(Parameterized.class)
public class JSONParserParamatisedBasicValueTest {
	public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss.SSS";
	public static final String[] FIVE_FIELDS = new String[]{"one", "two", "three", "four", "five"};
	public static final String[] ONE_FIELD = new String[]{"one"};

	@Parameters(name="{index} : {0}")
    public static Collection<Object[]> data() {
        return asList(new Object[][] {  
        		//no fields
       		 	{
              	 	"{}",  
              	 	new String[]{}, 
              	 	new Object[] {} 
                },
        		 //booleans
        		 {
               	 	"{one : true}",  
               	 ONE_FIELD, 
               	 	new Object[] { true }
                 },
                 {
               	 	"{\"one\" : false}",  
               	 	ONE_FIELD, 
               	 	new Object[] {false }
                 },
                 {
               	 	"{'one' : true}",  
               	 	ONE_FIELD, 
               	 	new Object[] { true }
                 },
                 {
                	 "{one : true, two : false, three : false, four : true, five : true}",  
                	 FIVE_FIELDS, 
                	 new Object[] { true, false, false, true, true }
                 },
                 {
                	 "{\"one\" : true, \"two\" : false, \"three\" : false, \"four\" : true, \"five\" : true}",  
                	 FIVE_FIELDS, 
                	 new Object[] { true, false, false, true, true }
                 },
                 {
                	 "{'one' : true, 'two' : false, 'three' : false, 'four' : true, 'five' : true}",  
                	 FIVE_FIELDS, 
                	 new Object[] { true, false, false, true, true }
                 },
                 //Strings
                 {
                	 "{one : \"val 1\"}",
                	 ONE_FIELD,
                	 new String []{"val 1"}
                 },
                 {
                	 "{\"one\" : \"val 1\"}",
                	 ONE_FIELD,
                	 new Object []{"val 1"}
                 },
                 {
                	 "{'one' : 'val 1'}",
                	 ONE_FIELD,
                	 new Object []{"val 1"}
                 },
                 {
                	 "{'one' : \"val ' 1\"}",
                	 ONE_FIELD,
                	 new Object []{"val ' 1"}
                 },
                 {
                	 "{'one' : 'val \" 1'}",
                	 ONE_FIELD,
                	 new Object []{"val \" 1"}
                 },
                 {
                	 "{one : \"val 1\", two : \"val 2\", three : \"val 3\", four : \"val 4\", five : \"val 5\"}",
                	 FIVE_FIELDS,
                	 new String []{"val 1", "val 2", "val 3", "val 4", "val 5"}
                 },
                 {
                	 "{\"one\" : \"val 1\", \"two\" : \"val 2\", \"three\" : \"val 3\", \"four\" : \"val 4\", \"five\" : \"val 5\"}",
                	 FIVE_FIELDS,
                	 new String []{"val 1", "val 2", "val 3", "val 4", "val 5"}
                 },
                 {
                	 "{'one' : 'val 1', 'two' : 'val 2', 'three' : 'val 3', 'four' : 'val 4', 'five' : 'val 5'}",
                	 FIVE_FIELDS,
                	 new String []{"val 1", "val 2", "val 3", "val 4", "val 5"}
                 },
                 {
                	 "{\"one\" : \", two : [1], three : {}\"}",
                	 ONE_FIELD,
                	 new String[]{", two : [1], three : {}"}
                 },
                 //Dates
                 {
                	 "{one : 14/04/2012 10:15:32.123}",
                	 ONE_FIELD,
                	 new Object[]{getDate("14/04/2012 10:15:32.123")}
                 },
                 {
                	 "{\"one\" : \"14/04/2012 10:15:32.123\"}",
                	 ONE_FIELD,
                	 new Object[]{getDate("14/04/2012 10:15:32.123")}
                 },
                 {
                	 "{'one' : '14/04/2012 10:15:32.123'}",
                	 ONE_FIELD,
                	 new Object[]{getDate("14/04/2012 10:15:32.123")}
                 },
                 {
                	 "{one : 14/01/2012 10:15:32.123, two : 14/02/2012 10:15:32.123, three : 14/03/2012 10:15:32.123, four : 14/04/2012 10:15:32.123, five : 14/05/2012 10:15:32.123}",
                	 FIVE_FIELDS,
                	 new Object[]{getDate("14/01/2012 10:15:32.123"),
							 getDate("14/02/2012 10:15:32.123"),
							 getDate("14/03/2012 10:15:32.123"),
							 getDate("14/04/2012 10:15:32.123"),
							 getDate("14/05/2012 10:15:32.123")}
                 },
                 {
                	 "{\"one\" : \"14/01/2012 10:15:32.123\", \"two\" : \"14/02/2012 10:15:32.123\", \"three\" : \"14/03/2012 10:15:32.123\", \"four\" : \"14/04/2012 10:15:32.123\", \"five\" : \"14/05/2012 10:15:32.123\"}",
                	 FIVE_FIELDS,
                	 new Object[]{
							 getDate("14/01/2012 10:15:32.123"),
							 getDate("14/02/2012 10:15:32.123"),
							 getDate("14/03/2012 10:15:32.123"),
							 getDate("14/04/2012 10:15:32.123"),
							 getDate("14/05/2012 10:15:32.123")}
                 },
                 {
                	 "{'one' : '14/01/2012 10:15:32.123', 'two' : '14/02/2012 10:15:32.123', 'three' : '14/03/2012 10:15:32.123', 'four' : '14/04/2012 10:15:32.123', 'five' : '14/05/2012 10:15:32.123'}",
                	 FIVE_FIELDS,
                	 new Object[]{getDate("14/01/2012 10:15:32.123"),
							 getDate("14/02/2012 10:15:32.123"),
							 getDate("14/03/2012 10:15:32.123"),
							 getDate("14/04/2012 10:15:32.123"),
							 getDate("14/05/2012 10:15:32.123")}
                 },
                 //Ints
                 {
                	 "{one : 1}",
                	 ONE_FIELD,
                	 new Object[]{1}
                 },
                 {
                	 "{\"one\" : 1}",
                	 ONE_FIELD,
                	 new Object[]{1}
                 },
                 {
                	 "{'one' : 1}",
                	 ONE_FIELD,
                	 new Object[]{1}
                 },
                 {
                	 "{one : 1, two : 2, three : 3, four : 4, five : 5}",
                	 FIVE_FIELDS,
                	 new Object[]{1, 2, 3, 4, 5}
                 },
                 {
                	 "{\"one\" : 1, \"two\" : 2, \"three\" : 3, \"four\" : 4, \"five\" : 5}",
                	 FIVE_FIELDS,
                	 new Object[]{1, 2, 3, 4, 5}
                 },
                 {
                	 "{'one' : 1, 'two' : 2, 'three' : 3, 'four' : 4, 'five' : 5}",
                	 FIVE_FIELDS,
                	 new Object[]{1, 2, 3, 4, 5}
                 },
                 //Decimals
                 {
                	 "{one : 1.1}",
                	 ONE_FIELD,
                	 new Object[]{new BigDecimal("1.1")}
                 },
                 {
                	 "{\"one\" : 1.20}",
                	 ONE_FIELD,
                	 new Object[]{new BigDecimal("1.20")}
                 },
                 {
                	 "{'one' : 122.33}",
                	 ONE_FIELD,
                	 new Object[]{new BigDecimal("122.33")}
                 },
                 {
                	 "{one : 1.1, two : 22.2, three : 33.33, four : 444.44, five : 55555.555}",
                	 FIVE_FIELDS,
                	 new Object[]{new BigDecimal("1.1"), new BigDecimal("22.2"), new BigDecimal("33.33"),
							 new BigDecimal("444.44"), new BigDecimal("55555.555")}
                 },
                 {
                	 "{one : 1.1, two : 22.2, three : 33.33, four : 444.44, five : 55555.555}",
                	 FIVE_FIELDS,
                	 new Object[]{new BigDecimal("1.1"), new BigDecimal("22.2"), new BigDecimal("33.33"), new BigDecimal("444.44")
							 , new BigDecimal("55555.555")}
                 },
                 {
                	 "{one : 1.1, two : 22.2, three : 33.33, four : 444.44, five : 55555.555}",
                	 FIVE_FIELDS,
                	 new Object[]{new BigDecimal("1.1"), new BigDecimal("22.2"), new BigDecimal("33.33"),
							 new BigDecimal("444.44"), new BigDecimal("55555.555")}
                 }
           });
    }
    
    private static SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
    private static Date getDate(String value) {
    	try {
			return formatter.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    @Parameter(value = 0)
    public String valueToParse;
    @Parameter(value = 1)
    public String[] keys;
    @Parameter(value = 2)
    public Object[] vals;
    
    @Test
    public void testParsing() throws Exception {
    	JSONParser parser = new JSONParser();
		parser.setTranslators(SystemParserCreator.getTranslators());
    	parser.setJsonFactory(SystemParserCreator.SYSTEM_JSON_FACTORY);
    	
    	new JSONReader().read(parser, valueToParse);
    	
    	assertNotNull(parser.getJson());
        assertTrue(parser.getJson() instanceof IJSONObject);
        assertEquals(keys.length, parser.getJson().getFields().length);

        for (int x = 0; x < keys.length; x++)
        {
            assertTrue(parser.getJson().getFields()[x] instanceof IJSONField);
            IJSONField field = (IJSONField)parser.getJson().getFields()[x];
            assertEquals(keys[x], field.getName());
            assertEquals(vals[x], field.getValue().getValue());
        }
    }
}
