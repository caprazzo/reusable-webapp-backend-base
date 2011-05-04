package net.caprazzi.skimpy.service.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.caprazzi.skimpy.ErrorCode;
import net.caprazzi.skimpy.framework.JsonOperationResponse;
import net.caprazzi.skimpy.model.Account;

import org.codehaus.jackson.JsonGenerator;

public class AccountListOperationResponse extends JsonOperationResponse {

	private final List<Account> accounts;

	public static AccountListOperationResponse success(List<Account> accounts) {
		return new AccountListOperationResponse(ErrorCode.NO_ERROR, accounts);
	}

	private AccountListOperationResponse(ErrorCode errorCode, List<Account> accounts) {
		this.errorCode = errorCode;
		this.accounts = accounts;
	}
	
	@Override
	public String getMessage() {
		return "Your accounts list";
	}

	@Override
	public int getHttpStatus() {
		if (isSuccess())
			return 200;
		return 0;
	}

	@Override
	public void writeBody(OutputStream os) throws IOException {
		JsonGenerator json = jf.createJsonGenerator(os);
		writeOpearationObject(json);
		json.writeArrayFieldStart("accounts");
		for(Account account : accounts) {
			json.writeStartObject();
			json.writeStringField("id", account.getId().toString());
			json.writeStringField("userId", account.getUserId().toString());
			json.writeStringField("name", account.getName());
			json.writeEndObject();
		}
		json.writeEndArray();
		json.writeEndObject();
		json.close();
	}

}
