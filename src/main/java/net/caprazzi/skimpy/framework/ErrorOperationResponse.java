package net.caprazzi.skimpy.framework;

import java.io.IOException;
import java.io.OutputStream;

import net.caprazzi.skimpy.ErrorCode;

import org.codehaus.jackson.JsonGenerator;

public class ErrorOperationResponse extends JsonOperationResponse {

	private final String message;
	private final ErrorCode errorCode;

	public static OperationResponse serverError(String message) {
		return new ErrorOperationResponse(ErrorCode.ERR_SERVER_ERROR, message);
	}

	public static OperationResponse fromException(Exception ex) {
		return new ErrorOperationResponse(ErrorCode.ERR_SERVER_ERROR, ex.getMessage());
	}
	
	public static OperationResponse failureInvalidArguments(String message) {
		return new ErrorOperationResponse(ErrorCode.ERR_INVALID_ARGUMENTS, message);
	}
	
	public static OperationResponse makeNotAuthorised() {
		return new ErrorOperationResponse(ErrorCode.ERR_NOT_AUTORIZED, "Please login");
	}

	private ErrorOperationResponse(ErrorCode errorCode, String string) {
		this.errorCode = errorCode;
		message = string;
	}

	public static OperationResponse makeNotFound() {
		return new ErrorOperationResponse(ErrorCode.ERR_NOT_FOUND, "Not found");
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public int getHttpStatus() {
		if (errorCode == ErrorCode.ERR_NOT_FOUND)
			return 404;
		if (errorCode == ErrorCode.ERR_SERVER_ERROR)
			return 500;
		if (errorCode == ErrorCode.ERR_INVALID_ARGUMENTS)
			return 400;
		if (errorCode == ErrorCode.ERR_NOT_AUTORIZED)
			return 401;
		throw new RuntimeException("Error not handled " + errorCode);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void writeBody(OutputStream os) throws IOException {
		JsonGenerator json = jf.createJsonGenerator(os);
		writeOpearationObject(json);
		json.close();
	}

	@Override
	public String toString() {
		return String.format(
				"ErrorOperationResponse{errorCode:%s, message:%s}", errorCode,
				message);
	}

}
