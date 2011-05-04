package net.caprazzi.skimpy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import net.caprazzi.skimpy.util.Ensure;

public class User implements Serializable {

	private static final long serialVersionUID = -4183609823800728533L;
	private final UUID id;
	private final String email;
	private final String passwordHash;
	private final Date created;
	private boolean confirmed;
	
	private User(UUID id, String email, String passwordHash, Date created, boolean confirmed) {		
		Ensure.notEmpty(id, email, passwordHash, created);
		this.id = id;
		this.email = email;
		this.passwordHash = passwordHash;
		this.created = created;
		this.confirmed = confirmed;
	}

	public static User newRegistration(String email, String password) {
		return new User(UUID.randomUUID(), email, password, new Date(), true);
	}

	public String getEmail() {
		return email;
	}
	
	public boolean isConfirmed() {
		return confirmed;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
	
	public Date created() {
		return created;
	}
	
	public UUID getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("User{email:%s, confirmed:%s}", email, confirmed);
	}

}
