package net.caprazzi.skimpy.service.response;

import java.io.IOException;
import java.io.OutputStream;

import org.codehaus.jackson.JsonGenerator;

import net.caprazzi.skimpy.ErrorCode;
import net.caprazzi.skimpy.framework.JsonOperationResponse;
import net.caprazzi.skimpy.model.User;

public class LoginOperationResponse extends JsonOperationResponse {

	private final User user;
	private final String message;

	public static LoginOperationResponse makeSuccess(User user) {
		return new LoginOperationResponse(user, ErrorCode.NO_ERROR,
				"Login succesful. Welcome back!");
	}

	public static LoginOperationResponse makeFailureUserNotFound() {
		return new LoginOperationResponse(null,
				ErrorCode.ERR_USER_WRONG_CREDENTIALS,
				"Sorry, could not authenticate this user with this password.");
	}

	public static LoginOperationResponse makeFailureUserNotConfirmed(User user) {
		return new LoginOperationResponse(user,
				ErrorCode.ERR_USER_NOT_CONFIRMED,
				"Sorry, this account is disabled");
	}

	private LoginOperationResponse(User user, ErrorCode errorCode,
			String message) {
		this.user = user;
		this.errorCode = errorCode;
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int getHttpStatus() {
		if (this.errorCode == ErrorCode.NO_ERROR)
			return 200;
		return 401;
	}

	@Override
	public String getMessage() {
		return message;
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
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String toString() {
		return String.format(
				"LoginOperationResponse{error:%s, message:%s, user:%s}",
				errorCode, message, user);
	}

}
