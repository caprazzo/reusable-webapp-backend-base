package net.caprazzi.skimpy.dao;

import net.caprazzi.skimpy.model.User;
import net.caprazzi.skimpy.util.Ensure;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

public class UserDao extends Db4oDao {

	public UserDao(ObjectContainer db) {
		super(db);
	}

	/**
	 * Store new user
	 * @param newUser
	 * @return true if the user was created, false if the email is already registered
	 */
	public boolean createUser(User newUser) {		
		Ensure.notEmpty(newUser);
		
		if (findByEmail(newUser.getEmail()) != null)
			return false;
		
		db.store(newUser);
		return true;
	}
	
	/**
	 * Find a user with the given email and hash.
	 * @param email
	 * @param passwordHash
	 * @return the user if found, null otherwise
	 */
	public User findWithPassword(final String email, final String passwordHash) {
		Ensure.notEmpty(email, passwordHash);		
		
		return queryForOne(new Predicate<User>() {
			@Override
			public boolean match(User user) {
				return user.getEmail().equals(email) 
					&& user.getPasswordHash().equals(passwordHash);
			}
		});		
	}

	/**
	 * Find a user with the given email.
	 * @param email
	 * @return
	 */
	public User findByEmail(final String email) {		
		Ensure.notEmpty(email);
		
		return queryForOne(new Predicate<User>() {
			@Override
			public boolean match(User user) {
				return email.equals(user.getEmail());
			}
		});
	}

}
