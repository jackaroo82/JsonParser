package org.wizfiz.json.creators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;
import org.wizfiz.json.IJSONFactory;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.creators.DefaultFieldCreator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
@RunWith(MockitoJUnitRunner.class)
public class DefaultFieldCreatorTest {
	private static final String VALUE = "myvalue";
	private static final String FIELD_NAME = "myfield";
	@InjectMocks DefaultFieldCreator testObj = new DefaultFieldCreator();
	@Mock IJSONFactory jsonFactoryMock;
	@Mock IJSONValue valueMock;
	
	@Test
	public void returnsNewField() throws Exception {
		when(jsonFactoryMock.createValue(VALUE)).thenReturn(valueMock);
		
		IJSONField field = testObj.createField(FIELD_NAME, VALUE);
		
		assertNotNull(field);
		assertEquals(FIELD_NAME, field.getName());
		assertSame(valueMock, field.getValue());	
	}
}
