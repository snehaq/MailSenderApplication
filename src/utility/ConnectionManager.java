package utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javaConstants.Constants;

public class ConnectionManager {
	public static Connection con;

	public static Connection getConnection() throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {
		Connection con = null;
		ReadPropertiesFile.readConfig();
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(
				Constants.setDbSchemaLink,Constants.setDbUsername,Constants.setDbPassword);
		return con;
	}

	public static Connection closeConnection() throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {
		Connection con = null;
		ReadPropertiesFile.readConfig();
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(
				Constants.setDbSchemaLink,Constants.setDbUsername,Constants.setDbPassword);
		return con;
	}
}
