package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javaConstants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadPropertiesFile {
	public static void readConfig(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			IOException {

		Properties pro = new Properties();
		try {
			// String path = System.getProperty("user.home")
			// + "/Desktop/MailSendingApplication.properties";
			String path = request.getServletContext().getRealPath(
					File.separator)
					+ "MailSendingApplication.properties";
			pro.load(new FileInputStream(path));
		} catch (IOException e) {
			System.out.println("FileNotFoundException in readP");
			String errMsg = "FileNotFoundException.!!!";

			e.printStackTrace();
		}

		Constants.setFrom = pro.getProperty("setFrom");
		Constants.setPassword = pro.getProperty("setPassword");
		Constants.setSubject = pro.getProperty("setSubject");
		Constants.setStatus = pro.getProperty("setStatus");
		Constants.templates = pro.getProperty("templates");
		Constants.timeToRun = pro.getProperty("timeToRun");
		Constants.setDbSchemaLink = pro.getProperty("setDbSchemaLink");
		Constants.setDbUsername = pro.getProperty("setDbUsername");
		Constants.setDbPassword = pro.getProperty("setDbPassword");

	}

}