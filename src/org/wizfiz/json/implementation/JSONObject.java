package org.wizfiz.json.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wizfiz.json.IJSONField;
import org.wizfiz.json.IJSONObject;
import org.wizfiz.json.JSONException;
import org.wizfiz.json.ObjectHolder;

public class JSONObject implements IJSONObject {
	
	@Override
	public String toJSON() throws JSONException {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		List<IJSONField> fieldsList = this.getFieldsList();
		for (int x = 0; x < fieldsList.size(); x++) {
			builder.append(fieldsList.get(x).toJSON());
			if (x < fieldsList.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("}");
		return builder.toString();
	}
	
	@Override
	public IJSONObject withField(IJSONField field) throws JSONException {
		addField(field);
		return this;
	}

	@Override
	public IJSONField getField(String fieldName) {
		ObjectHolder holder = new ObjectHolder();
		getFieldsList().stream().forEach((IJSONField field) -> {
			if (field.getName().equals(fieldName)) {
				holder.referenced = field;
			}
		});
		return holder.referenced;
	}

	private List<IJSONField> fields = new ArrayList<IJSONField>();
	private final Set<String> tags = new HashSet<String>();

	@Override
	public IJSONField[] getFields() {
		return getFieldsList().toArray(new IJSONField[this.getFieldsList().size()]);
	}
	
	public List<IJSONField> getFieldsList() {
		return fields;
	}
	
	public void setFields(List<IJSONField> fields) {
		this.fields = fields;
	}

	@Override
	public void addField(IJSONField field) throws JSONException {
		if (null != field && !this.getFieldsList().contains(field)) {
			this.getFieldsList().add(field);
		}
	}
	@Override
	public void removeField(String name) throws JSONException {
		IJSONField field = this.getField(name);
		if (null != field) {
			this.getFieldsList().remove(field);
		}
	}

	@Override
	public void addTag(String tagValue) {
		getTags().add(tagValue);
	}

	public Set<String> getTags() {
		return tags;
	}

}
