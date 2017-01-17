package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javaConstants.Constants;

import javax.servlet.http.HttpServletRequest;

public class ConnectionManager {
	public static Connection con;

	public static Connection getConnection(HttpServletRequest request)
			throws ClassNotFoundException, SQLException, FileNotFoundException,
			IOException {
		Connection con = null;
		ReadPropertiesFile.readConfig(request);
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(Constants.setDbSchemaLink,
				Constants.setDbUsername, Constants.setDbPassword);
		return con;
	}

	public static Connection closeConnection(HttpServletRequest request)
			throws ClassNotFoundException, SQLException, FileNotFoundException,
			IOException {
		Connection con = null;
		ReadPropertiesFile.readConfig(request);
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(Constants.setDbSchemaLink,
				Constants.setDbUsername, Constants.setDbPassword);
		return con;
	}
}
