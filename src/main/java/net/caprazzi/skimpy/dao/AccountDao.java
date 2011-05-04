package net.caprazzi.skimpy.dao;

import java.util.List;
import java.util.UUID;

import net.caprazzi.skimpy.model.Account;
import net.caprazzi.skimpy.util.Ensure;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class AccountDao extends Db4oDao {

	public AccountDao(ObjectContainer db) {
		super(db);
	}

	@SuppressWarnings("serial")
	public void createAccount(final Account newAccount) {
		Ensure.notEmpty(newAccount);				
		queryForZero(new Predicate<Account>() {
			@Override
			public boolean match(Account account) {				
				return newAccount.getId().equals(account.getId());
			}
		});
		db.store(newAccount);
	}

	@SuppressWarnings("serial")
	public List<Account> findByUserId(final UUID userId) {
		System.err.println("user,id:" + userId);
		Ensure.notEmpty(userId);
		return db.query(new Predicate<Account>() {
			@Override
			public boolean match(Account account) {
				System.err.println("account.user.id:" + account.getUserId());
				System.err.println(userId.equals(account.getUserId()));
				System.err.println(account.getUserId().compareTo(userId));
				return true; //account.getUserId().equals(userId);
			}
		});		
	}

}
