import java.io.File;
import java.util.UUID;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;

import net.caprazzi.skimpy.Main;
import net.caprazzi.skimpy.model.User;

public class TestUUIDEquality
{

	public static class Account {
		private final UUID id;
		private final UUID userId;
		
		public Account(UUID id, UUID userId) {
			this.id = id;
			this.userId = userId;
		}
		
		public UUID getId() {
			return id;
		}
		public UUID getUserId() {
			return userId;
		}
	}
	
	public static class User {
		private final UUID id;		

		public User(UUID id) {
			this.id = id;
		}

		public UUID getId() {
			return id;
		}
	}
	
	public static void main(String[] args) {
		
		new File("uuid.db4o").delete();
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "uuid.db4o");
		
		final UUID id = UUID.randomUUID();
		
		final User user = new User(id);
				
		Account account1 = new Account(UUID.randomUUID(), user.getId());
		Account account2 = new Account(UUID.randomUUID(), user.getId());
		
		db.store(account1);
		db.store(account2);
		
		db.query(new Predicate<Account>() {
			@Override
			public boolean match(Account acct) {
				System.out.println("A:" + acct.getUserId());				
				System.out.println("U:" + user.getId());
				System.out.println(acct.getUserId().equals(user.getId()));
				return acct.getUserId().equals(user.getId());
			}
			
		});
		
		// reopen the database and try again 
		db.close();
		
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "uuid.db4o");
		
		db.query(new Predicate<Account>() {

			@Override
			public boolean match(Account acct) {
				System.out.println("A:" + acct.getUserId());				
				System.out.println("U:" + user.getId());
				System.out.println(acct.getUserId().equals(user.getId()));
				return acct.getUserId().equals(user.getId());
			}
			
		});
		
	}
	
}
