package net.caprazzi.skimpy.service.response;

import java.io.IOException;
import java.io.OutputStream;

import net.caprazzi.skimpy.ErrorCode;
import net.caprazzi.skimpy.framework.JsonOperationResponse;
import net.caprazzi.skimpy.model.User;

import org.codehaus.jackson.JsonGenerator;

public class CreateUserOperationResponse extends JsonOperationResponse {

	private final User user;
	
	public enum CUErrors {
		noError, userExists
	}

	public static CreateUserOperationResponse success(User newUser) {
		return new CreateUserOperationResponse(ErrorCode.NO_ERROR, newUser);
	}

	public static CreateUserOperationResponse failureUserExists(User newUser) {
		return new CreateUserOperationResponse(ErrorCode.ERR_CONFLICT_USER_EXISTS, newUser);
	}
	
	private CreateUserOperationResponse(boolean success, User newUser) {
		this.user = newUser;
		this.errorCode = ErrorCode.NO_ERROR;
	}
	
	private CreateUserOperationResponse(ErrorCode errorCode, User newUser) {
		this.errorCode = errorCode;
		this.user = newUser;
	}

	@Override
	public int getHttpStatus() {
		if (errorCode != null && errorCode == ErrorCode.ERR_CONFLICT_USER_EXISTS)
			return 409; // conflict, user exists, the only known error so far
		else
			return 200; // created
	}

	@Override
	public String getMessage() {
		if (errorCode == ErrorCode.ERR_CONFLICT_USER_EXISTS)
			return "Sorry, could not create this user because the email address " + user.getEmail() + " is already registered";
		else
			return "Welcome! Your account has been created and you can now log in.";
	}

	@Override
	public void writeBody(OutputStream os) throws IOException {
		JsonGenerator json = jf.createJsonGenerator(os);
		writeOpearationObject(json);
		json.writeObjectFieldStart("user");
		json.writeStringField("email", user.getEmail());
		json.writeBooleanField("confirmed", user.isConfirmed());
		json.writeEndObject();
		json.writeEndObject();
		json.close();
	}
	
	@Override
	public String toString() {
		return "CreateUserOperationResponse{" + user + ", " + isSuccess() + ", " + errorCode + ", " + getHttpStatus() + ", " +  getMessage() + "}";  
	}

}
