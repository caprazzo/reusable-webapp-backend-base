package net.caprazzi.skimpy.service;

import java.util.List;
import java.util.UUID;

import net.caprazzi.skimpy.dao.AccountDao;
import net.caprazzi.skimpy.framework.OperationResponse;
import net.caprazzi.skimpy.model.Account;
import net.caprazzi.skimpy.service.response.AccountCreationOperationResponse;
import net.caprazzi.skimpy.service.response.AccountListOperationResponse;

public class AccountService {

	private final AccountDao accountDao;

	public AccountService(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public AccountCreationOperationResponse createAccount(Account account) {						
		accountDao.createAccount(account);
		return AccountCreationOperationResponse.success(account);
	}

	public OperationResponse findAccountsForUser(UUID userId) {
		List<Account> accounts = accountDao.findByUserId(userId);
		return AccountListOperationResponse.success(accounts);
	}
	
}
