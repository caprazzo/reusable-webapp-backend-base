package net.caprazzi.skimpy;

import java.io.File;

import net.caprazzi.skimpy.dao.AccountDao;
import net.caprazzi.skimpy.dao.UserDao;
import net.caprazzi.skimpy.serlvet.AccountServlet;
import net.caprazzi.skimpy.serlvet.UserServlet;
import net.caprazzi.skimpy.service.AccountService;
import net.caprazzi.skimpy.service.UserService;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class Main<T> {
	
	final static String DB4OFILENAME = System.getProperty("user.home") + "/skimpy.db4o";
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 1) {
			System.err.println("Please specify the port number");
			return;
		}
		Integer port;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException ex) {
            System.err.println("port_number must be a number");
            return;
        
        }        
        
		Server server = new Server(port);
		 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        HashSessionManager sm = new HashSessionManager();
        sm.setStoreDirectory(new File("sessions"));
        sm.setSavePeriod(10);
        sm.setMaxInactiveInterval(60 * 60 * 24);
		context.setSessionHandler(new SessionHandler(sm));
        context.setContextPath("/");
        server.setHandler(context);
                
        // configure the default servlet to serve static files from "htdocs" in the classpath   
        context.addServlet(new ServletHolder(new ClasspathFilesServlet("/htdocs")),"/");
                        
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4OFILENAME);
        
		UserDao userDao = new UserDao(db);
		UserService userService = new UserService(userDao);
		context.addServlet(new ServletHolder(new UserServlet(userService)), "/users/*");
		
		AccountDao accountDao = new AccountDao(db);
		AccountService accountService = new AccountService(accountDao);
        context.addServlet(new ServletHolder(new AccountServlet(accountService)), "/accounts/*");
        
        
        // use /uuid to get a fresh id
       // context.addServlet(new ServletHolder(new UUIDServlet()), "/uuid");
        
        // the actual key/value store
       // context.addServlet(new ServletHolder(new KeyValueServlet()), "/store/*");
        
        // bind a publishServlet to /quotes
       // final PublishServlet publishServlet = new PublishServlet();
       // context.addServlet(new ServletHolder(publishServlet), "/quotes");
        
        // setup the quote service
       // InputStream stream = Main.class.getClassLoader().getResourceAsStream("hitchhiker_guide_to_the_galaxy_quotes.txt");        
       // final QuoteService guideQuoteService = QuoteService.fromInputStream(stream);
        
        // send out a new quote every 3 to 10 seconds
     //   new RandomTimer(3, 10) {			
	//		@Override
		//	public void tick() {
	//			publishServlet.publish(guideQuoteService.getRandomQuote());				
//			}
//		}; 		
        
		// start the server
        server.start();
        server.join();		
	}
	
	public T get() {
		return null;
	}
}
