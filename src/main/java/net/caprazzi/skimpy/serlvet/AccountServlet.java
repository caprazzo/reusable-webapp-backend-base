package net.caprazzi.skimpy.serlvet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.caprazzi.skimpy.framework.ErrorOperationResponse;
import net.caprazzi.skimpy.framework.OperationResponse;
import net.caprazzi.skimpy.framework.OperationResponseServlet;
import net.caprazzi.skimpy.framework.RequestInfo;
import net.caprazzi.skimpy.model.Account;
import net.caprazzi.skimpy.model.User;
import net.caprazzi.skimpy.service.AccountService;
import net.caprazzi.skimpy.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create account: POST /accounts/<email>
 * upload report: POST /accounts/<email>/<accountId>/upload
 * 
 * @author matteo.caprari@gmail.com
 */
@SuppressWarnings("serial")
public class AccountServlet extends OperationResponseServlet {

	final static Logger log = LoggerFactory.getLogger(AccountServlet.class);
	private final AccountService accountService;

	public AccountServlet(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Override
	protected OperationResponse handle(RequestInfo requestInfo,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		log.debug("Handling " + req.getRequestURI());
		
		User user = getCurrentUser();
		if (user == null)
			return ErrorOperationResponse.makeNotAuthorised();		
		
		if (requestInfo.isPost("/accounts")) {
			return createAccount(requestInfo, req, resp);
		}
		
		if (requestInfo.isGet("/accounts")) {
			return listAccounts(requestInfo, req, resp);
		}
		
		return null;
	}

	private OperationResponse listAccounts(RequestInfo requestInfo,
			HttpServletRequest req, HttpServletResponse resp) {
		
		User user = getCurrentUser();
		return accountService.findAccountsForUser(user.getId());
	}

	private OperationResponse createAccount(RequestInfo requestInfo,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String accountName = readBodyAsString(req.getInputStream());
		
		if (Utils.isEmpty(accountName))
			return ErrorOperationResponse.failureInvalidArguments("Please provide a name for this account");
		
		Account newAccount = Account.newAccount(getCurrentUser().getId(), accountName);
		
		return accountService.createAccount(newAccount);
	}

}
