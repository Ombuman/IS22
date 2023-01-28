package librarium;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;

import com.librarium.database.AuthenticationManager;
import com.librarium.database.CatalogManager;
import com.librarium.database.PrestitiManager;
import com.librarium.database.UsersManager;

public class TestDB {
	
	@Before
	public void initTest() {		
		try {
			UsersManager.getInstance().setAutoCommit(false);
			AuthenticationManager.getInstance().setAutoCommit(false);
			CatalogManager.getInstance().setAutoCommit(false);
			PrestitiManager.getInstance().setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	@AfterClass
	public static void endTest() {
		try {
			UsersManager.getInstance().rollback();
			UsersManager.getInstance().setAutoCommit(true);
			
			AuthenticationManager.getInstance().rollback();
			AuthenticationManager.getInstance().setAutoCommit(true);
			
			CatalogManager.getInstance().rollback();
			CatalogManager.getInstance().setAutoCommit(true);
			
			PrestitiManager.getInstance().rollback();
			PrestitiManager.getInstance().setAutoCommit(true);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
