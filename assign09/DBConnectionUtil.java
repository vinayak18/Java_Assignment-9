package assign09;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
	static Connection cn = null;
	
	private DBConnectionUtil() {
		
	}
	
	static String url="jdbc:oracle:thin:@//localhost:1521/XE";
	static String username = "db_user";
	static String pass = "db_user";
	
	public static Connection getConnection() {
		if(cn == null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				cn = DriverManager.getConnection(url,username,pass);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return cn;
	}
	
	public static void closeConnection(){
		try {
			cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}	
