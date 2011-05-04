package net.caprazzi.skimpy.service.response;

import java.io.IOException;
import java.io.OutputStream;

import net.caprazzi.skimpy.ErrorCode;
import net.caprazzi.skimpy.framework.JsonOperationResponse;
import net.caprazzi.skimpy.model.Account;

import org.codehaus.jackson.JsonGenerator;

public class AccountCreationOperationResponse extends JsonOperationResponse {
	
	private final Account account;

	private AccountCreationOperationResponse(ErrorCode errorCode, Account account) {
		this.errorCode = errorCode;
		this.account = account;
	}

	public static AccountCreationOperationResponse success(Account account) {
		return new AccountCreationOperationResponse(ErrorCode.NO_ERROR, account);
	}

	@Override
	public int getHttpStatus() {
		if (isSuccess())
			return 200;
		throw new RuntimeException("Error not handled " + errorCode);
	}

	@Override
	public String getMessage() {
		if (isSuccess())
			return "Account created";
		return "Error account not created";
	}

	@Override
	public void writeBody(OutputStream os) throws IOException {
		JsonGenerator json = jf.createJsonGenerator(os);
		writeOpearationObject(json);
		json.writeObjectFieldStart("account");
		json.writeStringField("id", account.getId().toString());
		json.writeStringField("name", account.getName());
		json.writeStringField("userId", account.getUserId().toString());
		json.writeEndObject();
		json.writeEndObject();
		json.close();
	}
	
	@Override
	public String toString() {
		return String.format("AccountCreationOperationResponse{errorCode:%s, account:%s}", errorCode, account);
	}

}
