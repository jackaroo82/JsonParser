package org.wizfiz.json.creators;

import static org.mockito.Mockito.verify;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.creators.DefaultObjectTagger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultObjectTaggerTest {
	private static final String TAG = "tagval";

	DefaultObjectTagger testObj = new DefaultObjectTagger();
	
	@Mock IJSONObject objectMock;
	
	@Test
	public void testAddsTagToObject() {
		testObj.tagObject(objectMock, TAG);
		
		verify(objectMock).addTag(TAG);
	}
}
