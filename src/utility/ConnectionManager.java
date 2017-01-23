package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConnectionManager {
	public static Connection con;
	
	public static Connection getConnection(HttpServletRequest req,HttpServletResponse res) throws ClassNotFoundException, FileNotFoundException, IOException, SQLException{
		Connection con = null;
		
			ReadPropertiesFile.readConfig(req);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(((HashMap<String,String>) req.getAttribute("Properties")).get("setDbSchemaLink"),
					((HashMap<String,String>) req.getAttribute("Properties")).get("setDbUsername"),((HashMap<String,String>) req.getAttribute("Properties")).get("setDbPassword"));
		
		return con;
	}

	public static Connection closeConnection(HttpServletRequest req){
		Connection con = null;
		try {
			ReadPropertiesFile.readConfig(req);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(((HashMap<String,String>) req.getAttribute("Properties")).get("setDbSchemaLink"),
					((HashMap<String,String>) req.getAttribute("Properties")).get("setDbUsername"),((HashMap<String,String>) req.getAttribute("Properties")).get("setDbPassword"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
