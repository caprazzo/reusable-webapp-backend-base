package net.caprazzi.skimpy.framework;


import java.io.IOException;

import net.caprazzi.skimpy.ErrorCode;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

/**
 * Utility implementation for json representations
 * @author matteo.caprari@gmail.com
 */
public abstract class JsonOperationResponse implements OperationResponse {

	protected static final JsonFactory jf = new JsonFactory();
	protected ErrorCode errorCode;

	@Override
	public String getHttpContentType() {
		return "application/json";
	}		
	
	@Override
	public boolean isSuccess() {
		return errorCode == ErrorCode.NO_ERROR;
	}
	
	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}	
	
	protected final void writeOpearationObject(JsonGenerator json) throws JsonGenerationException, IOException {
		json.writeStartObject();
		json.writeObjectFieldStart("operation");
		json.writeStringField("type", this.getClass().getSimpleName());
		json.writeBooleanField("success", isSuccess());
		json.writeStringField("errorCode", getErrorCode().toString());
		json.writeStringField("message", getMessage());
		json.writeEndObject();		
	}

}
