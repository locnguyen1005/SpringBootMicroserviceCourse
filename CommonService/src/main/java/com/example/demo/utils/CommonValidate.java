package com.example.demo.utils;

import java.io.InputStream;
import java.net.http.HttpClient.Version;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.http.HttpStatus;

import com.example.demo.Common.ValidateException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class CommonValidate {
	
	@SneakyThrows
	public static void jsonValidate(String json, InputStream inputStream) {
		JsonSchema jsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(inputStream);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(json);
		Set<ValidationMessage> errorsMessages = jsonSchema.validate(jsonNode);
		Map<String,String> stringSetMap = new HashMap();
		for(ValidationMessage error : errorsMessages) {
			if(stringSetMap.containsKey(formatStringValidate(error.getPath()))) {
				String message = stringSetMap.get(formatStringValidate(error.getPath()));
				stringSetMap.put(formatStringValidate(error.getPath()),message + ", "+formatStringValidate(error.getMessage()));
			}
			else {
				 stringSetMap.put(formatStringValidate(error.getPath()),formatStringValidate(error.getMessage()));
			}
		
		}
		if(!errorsMessages.isEmpty()){

			throw new ValidateException("RQ01",stringSetMap, HttpStatus.BAD_REQUEST);
	    }
	}
	public static String formatStringValidate(String message){
        return message.replaceAll("\\$.","");
    }
}
