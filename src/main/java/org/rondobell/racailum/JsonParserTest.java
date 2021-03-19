package org.rondobell.racailum;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;

import java.io.File;
import java.io.IOException;

public class JsonParserTest {
	public static void main(String[] args) throws IOException {
		JsonFactory jsonFactory = new MappingJsonFactory();
		File file = new File("D:\\test.json");
		JsonParser jp = jsonFactory.createJsonParser(file);
		JsonToken current;
		while (jp.nextToken()!=JsonToken.END_OBJECT) {
			if(jp.getCurrentToken()==JsonToken.START_ARRAY&&"con".equals(jp.getCurrentName())){
				//con:[
				while (jp.nextToken()!=null) {
					if(jp.getCurrentToken()==JsonToken.START_ARRAY&&"programs".equals(jp.getCurrentName())){
						//programs:[
						while (jp.nextToken()!=null) {
							//读碎片属性
						}
					}
				}
			}
		}
	}
}
