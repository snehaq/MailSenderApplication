package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javaConstants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConnectionManager {
	public static Connection con;

	public static Connection getConnection(HttpServletRequest request,
			HttpServletResponse response) throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {
		Connection con = null;
		ReadPropertiesFile.readConfig(request, response);
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(Constants.setDbSchemaLink,
				Constants.setDbUsername, Constants.setDbPassword);
		return con;
	}

	public static Connection closeConnection(HttpServletRequest request,
			HttpServletResponse response) throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {
		Connection con = null;
		ReadPropertiesFile.readConfig(request, response);
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(Constants.setDbSchemaLink,
				Constants.setDbUsername, Constants.setDbPassword);
		return con;
	}
}
