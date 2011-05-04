package net.caprazzi.skimpy.model;

import java.util.UUID;

public class Account {

	private final UUID userId;
	private final String accountName;
	private final UUID id;

	private Account(UUID id, UUID userId, String accountName) {
		this.id = id;
		this.userId = userId;
		this.accountName = accountName;
	}

	public static Account newAccount(UUID userId, String accountName) {
		UUID id = UUID.randomUUID();
		return new Account(id, userId, accountName);
	}

	public String getName() {
		return accountName;
	}
	
	public UUID getId() {
		return id;
	}
	
	public UUID getUserId() {
		return userId;
	}
	
	@Override
	public String toString() {
		return String.format("Account{name:%s, user:%s, id:%s}", accountName, userId, id);
	}

}
