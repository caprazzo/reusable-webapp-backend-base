package net.caprazzi.skimpy.framework;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.caprazzi.skimpy.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility servlet implementation. @see OperationResponse.Handle for more details. 
 * @author matteo.caprari@gmail.com 
 */
@SuppressWarnings("serial")
public abstract class OperationResponseServlet extends HttpServlet {
	
	protected final static Logger log = LoggerFactory.getLogger(OperationResponseServlet.class);

	/**
	 * Implement this method to provide further functionality. In the implementation you can
	 * use requestInfo to route the request correctly. Return null to mean "404 not found". Example:<pre>
	 * 
	 * protected OperationResponse handle(RequestInfo requestInfo, HttpServletRequest req, HttpServletResponse resp) throws Exception {
	 *		if (requestInfo.isPut("/users/_"))
	 * 			return createNewUser(requestInfo, req, resp)
	 * 
	 * 		return null;
	 * }
	 *
	 * </pre>
	 * 
	 * @param requestInfo information about the current request
	 * @param req
	 * @param resp
	 * @return an OperationREsponse object. If it is null, a notFound error will be returned
	 * @throws Exception
	 */
	protected abstract OperationResponse handle(RequestInfo requestInfo, HttpServletRequest req, HttpServletResponse resp)  throws Exception;

	@Override
	protected final void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
												
		try {
			currentUser.set(getUserFromSession(req));
			OperationResponse result = handle(RequestInfo.fromRequest(req), req, resp);
			if (result == null)
				result = ErrorOperationResponse.makeNotFound();
			sendResult(result, resp);
		} catch (Exception ex) {
			log.error("Unhandled exception", ex);
			sendResult(ErrorOperationResponse.fromException(ex), resp);
		}
	}		
			
	private final void sendResult(OperationResponse result, HttpServletResponse resp)
			throws IOException {
		log.debug("Sending result {}", result);

		resp.setContentType(result.getHttpContentType());
		resp.setCharacterEncoding("UTF-8");

		resp.setStatus(result.getHttpStatus());
		result.writeBody(new BufferedOutputStream(resp.getOutputStream()));
	}
	
	protected final String readBodyAsString(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return new String(buffer.toByteArray(), "UTF-8");
	}
	
	private static final ThreadLocal<User> currentUser = new ThreadLocal<User>();
	
	protected User getCurrentUser() {
		return currentUser.get();
	}
	
	private User getUserFromSession(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session == null)
			return null;
				
		User user = (User) session.getAttribute("user");
		if (user != null)
			return user;
		
		return null;		
	}

}
