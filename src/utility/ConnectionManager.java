package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class ConnectionManager {
	public static Connection con;

	public static Connection getConnection(HttpServletRequest req){
		Connection con = null;
		try {
			ReadPropertiesFile.readConfig(req);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(((HashMap<String,String>) req.getAttribute("Properties")).get("setDbSchemaLink"),
					((HashMap<String,String>) req.getAttribute("Properties")).get("setDbUsername"),((HashMap<String,String>) req.getAttribute("Properties")).get("setDbPassword"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	public static Connection closeConnection(HttpServletRequest req){
		Connection con = null;
		try {
			ReadPropertiesFile.readConfig(req);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(((HashMap<String,String>) req.getAttribute("Properties")).get("setDbSchemaLink"),
					((HashMap<String,String>) req.getAttribute("Properties")).get("setDbUsername"),((HashMap<String,String>) req.getAttribute("Properties")).get("setDbPassword"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
}
