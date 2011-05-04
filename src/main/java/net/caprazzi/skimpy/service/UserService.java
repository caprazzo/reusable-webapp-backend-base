package net.caprazzi.skimpy.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.caprazzi.skimpy.dao.UserDao;
import net.caprazzi.skimpy.framework.OperationResponse;
import net.caprazzi.skimpy.model.User;
import net.caprazzi.skimpy.service.response.CreateUserOperationResponse;
import net.caprazzi.skimpy.service.response.LoginOperationResponse;
import net.caprazzi.skimpy.util.Ensure;

public class UserService {
	
	private static final String salt = "0wxPECLq4a";
	private final UserDao userDao;
	
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public CreateUserOperationResponse registerUser(String email, String password) {
						
		User newUser = User.newRegistration(email, hash(password));
		
		if (userDao.createUser(newUser)) {
			// TODO: queue confirmation email
			return CreateUserOperationResponse.success(newUser);
		}
		
		return CreateUserOperationResponse.failureUserExists(newUser);				
	}
	
	public OperationResponse confirmEmail(String token) {
		return null;
	}
	
	public LoginOperationResponse loginUser(String email, String password) {
		Ensure.notEmpty(email, password);
		User user = userDao.findWithPassword(email, hash(password));
		
		if (user == null)
			return LoginOperationResponse.makeFailureUserNotFound();
		
		if (!user.isConfirmed())
			return LoginOperationResponse.makeFailureUserNotConfirmed(user);
		
		return LoginOperationResponse.makeSuccess(user);			
	}
	
	private String hash(String password) {		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest((salt + password).getBytes("UTF-8"));
			return new String(digest, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public User getByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
}
