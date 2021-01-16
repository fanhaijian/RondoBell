package org.rondobell.racailum.base.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	public static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		mapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
	}

	public static <T> String toJsonString(T t) {
		try {
			return mapper.writeValueAsString(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T toObject(String str, Class<T> c) {
		T response = null;
		try {
			response = (T)mapper.readValue(str, c);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
