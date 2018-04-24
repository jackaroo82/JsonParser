package org.wizfiz.json.implementation;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.wizfiz.json.IFieldCreator;
import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.IJSONValue;
import org.wizfiz.json.IObjectCreator;
import org.wizfiz.json.IObjectTagger;
import org.wizfiz.json.IValueCreator;
import org.wizfiz.json.implementation.JSONFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JSONFactoryTest {
	
	private static final String TAG_VALUE = "mytag";
	private static final String UNMAPPED_VALUE = "unmapped value";
	private static final Integer MAPPED_VALUE = 10;
	private static final String FIELD_NAME = "myfield";
	@InjectMocks JSONFactory testObj = new JSONFactory();
	@Mock IObjectCreator objectCreatorMock;
	@Mock IFieldCreator fieldCreatorMock;
	@Mock(name="defaultFieldCreator") IFieldCreator defaultFieldCreatorMock;
	@Mock IValueCreator valueCreatorMock;
	@Mock IValueCreator nullValueCreatorMock;
	@Mock(name="defaultValueCreator") IValueCreator defaultValueCreatorMock;
	@Mock IObjectTagger objectTaggerMock;
	@Mock IJSONObject objectMock;
	@Mock IJSONField fieldMock;
	@Mock IJSONField defaultFieldMock;
	@Mock IJSONValue valueMock;
	@Mock IJSONValue defaultValueMock;
	
	@Before
	public void setup() {
		Map<Class<?>, IFieldCreator> fieldCreators = new HashMap<Class<?>, IFieldCreator>();
		fieldCreators.put(Integer.class, fieldCreatorMock);
		testObj.setFieldCreators(fieldCreators);
		
		Map<Class<?>, IValueCreator> valueCreators = new HashMap<Class<?>, IValueCreator>();
		valueCreators.put(Integer.class, valueCreatorMock);
		valueCreators.put(null, nullValueCreatorMock);
		testObj.setValueCreators(valueCreators);
	}
	
	@Test
	public void createObject_callsCreator() throws Exception {
		when(objectCreatorMock.createObject()).thenReturn(objectMock);
		
		IJSONObject returned = testObj.createObject();
		
		assertSame(objectMock, returned);
	}
	
	@Test
	public void createField_callsFieldCreatorBasedOnValueClass() throws Exception {
		when(fieldCreatorMock.createField(FIELD_NAME, MAPPED_VALUE)).thenReturn(fieldMock);
		
		IJSONField returned = testObj.createField(FIELD_NAME, 10);
		
		assertSame(fieldMock, returned);
	}
	
	@Test
	public void createField_usesDefaultCreatorIfNoMappingPresent() throws Exception {
		when(defaultFieldCreatorMock.createField(FIELD_NAME, UNMAPPED_VALUE)).thenReturn(defaultFieldMock);
		
		IJSONField returned = testObj.createField(FIELD_NAME, UNMAPPED_VALUE);
		
		assertSame(defaultFieldMock, returned);
	}
	
	@Test
	public void createField_usesDefaultCreatorIfNoMappingPresentForNullValue() throws Exception {
		when(defaultFieldCreatorMock.createField(FIELD_NAME, null)).thenReturn(defaultFieldMock);
		
		IJSONField returned = testObj.createField(FIELD_NAME, null);
		
		assertSame(defaultFieldMock, returned);
	}
	
	@Test
	public void createValue_usesValueCreatorBasedOnValueClass() throws Exception {
		when(valueCreatorMock.createValue(MAPPED_VALUE)).thenReturn(valueMock);
		
		IJSONValue returned = testObj.createValue(MAPPED_VALUE);
		
		assertSame(valueMock, returned);
	}
	
	@Test
	public void createValue_ManagesNullCreation() throws Exception {
		when(nullValueCreatorMock.createValue(null)).thenReturn(valueMock);
		
		IJSONValue returned = testObj.createValue(null);
		
		assertSame(valueMock, returned);
	}
	
	@Test
	public void createValue_usesDefaultCreatorIfNoMappingPresent() throws Exception {
		when(defaultValueCreatorMock.createValue(UNMAPPED_VALUE)).thenReturn(defaultValueMock);
		
		IJSONValue returned = testObj.createValue(UNMAPPED_VALUE);
		
		assertSame(defaultValueMock, returned);
	}

	@Test
	public void tagObject_usesObjectTagger() throws Exception {
		testObj.tagObject(objectMock, TAG_VALUE);
		
		InOrder inOrder = inOrder(objectTaggerMock, objectMock);
		inOrder.verify(objectTaggerMock).tagObject(objectMock, TAG_VALUE);
		inOrder.verify(objectMock).addTag(TAG_VALUE);
		inOrder.verifyNoMoreInteractions();
	}
}
