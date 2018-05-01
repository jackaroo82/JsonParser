# JsonParser
This JSON Parser is part of a private framework but has been made public for anyone who wishes to use / contribute

## Changing implementations
As an example an alternative field creator implementation has been created. This allows for a fieldName to be substituted to a
value provided in a map.

To enable this creator pass the system property fieldCreatorClass to equal org.wizfiz.json.creators.MappedFieldCreator e.g.

java -jar JsonParser.jar -DfieldCreatorClass=org.wizfiz.json.creators.MappedFieldCreator

Or an alternative implementation which implements org.wizfiz.json.IFieldCreator

((org.wizfiz.json.creators.MappedFieldCreator)org.wizfiz.json.SYSTEM_JSON_FACTORY.getDefaultFieldCreator()).getFieldMappings()

Can then be used to add mappings if required.