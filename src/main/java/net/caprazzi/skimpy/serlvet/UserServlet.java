package net.caprazzi.skimpy.serlvet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.caprazzi.skimpy.framework.ErrorOperationResponse;
import net.caprazzi.skimpy.framework.OperationResponse;
import net.caprazzi.skimpy.framework.OperationResponseServlet;
import net.caprazzi.skimpy.framework.RequestInfo;
import net.caprazzi.skimpy.service.UserService;
import net.caprazzi.skimpy.service.response.LoginOperationResponse;
import net.caprazzi.skimpy.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create/Update/DELETE user: PUT/DELETE /users/<email> Login/Logout user:
 * Login: POST /users/<email>/sessions 
 * Logout: DELETE /users/<email>/sessions/<sessionId> 
 * Confirm email: POST /users/<email>/confirm/<token>
 * 
 * @author matteo.caprari@gmail.com
 */
@SuppressWarnings("serial")
public class UserServlet extends OperationResponseServlet {

	final static Logger log = LoggerFactory.getLogger(UserServlet.class);
	
	private final UserService userService;

	public UserServlet(UserService userService) {
		this.userService = userService;
	}
	
	protected OperationResponse handle(RequestInfo requestInfo, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		log.debug("Handling " + req.getRequestURI());
		
		if (requestInfo.isPut("/users/_")) 
			return createUser(requestInfo, req, resp);
				
		if (requestInfo.isPost("/users/_/sessions"))
			return login(requestInfo, req, resp);
				
		return null;		
	}	

	private OperationResponse login(RequestInfo requestPath,
			HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String email = requestPath.get(1);
		String password = readBodyAsString(req.getInputStream());
		
		if (Utils.isEmpty(email) || Utils.isEmpty(password)) {
			return ErrorOperationResponse.failureInvalidArguments("Please provide username and password");
		}
		
		//TODO: if a session already exists, reuse it directly
		
		// check credentials
		LoginOperationResponse response = userService.loginUser(email, password);

		// create new session if success and bind the user object to it
		if (response.isSuccess()) {				
			HttpSession session = req.getSession(true);
			session.setMaxInactiveInterval(60 * 60 * 24);
			session.setAttribute("user", response.getUser());
		}
		return response;
	}

	private OperationResponse createUser(RequestInfo requestPath,
			HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String email = requestPath.get(1);
		String password = readBodyAsString(req.getInputStream());
		return userService.registerUser(email, password);
	}
	
}
