package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class ReadPropertiesFile {
	public static void readConfig(HttpServletRequest request)
			throws FileNotFoundException, IOException {

		Properties pro = new Properties();
		String path = request.getServletContext().getRealPath(File.separator)
				+ "MailSendingApplication.properties";
		pro.load(new FileInputStream(path));
		HashMap<String, String> hmProperties = new HashMap<String, String>();
		hmProperties.put("templates", pro.getProperty("templates"));
		hmProperties.put("timeToRun", pro.getProperty("timeToRun"));
		hmProperties.put("setStatus", pro.getProperty("setStatus"));
		hmProperties.put("setFrom", pro.getProperty("setFrom"));
		hmProperties.put("setPassword", pro.getProperty("setPassword"));
		hmProperties.put("setSubject", pro.getProperty("setSubject"));
		hmProperties.put("setDbSchemaLink", pro.getProperty("setDbSchemaLink"));
		hmProperties.put("setDbUsername", pro.getProperty("setDbUsername"));
		hmProperties.put("setDbPassword", pro.getProperty("setDbPassword"));
		request.setAttribute("Properties", hmProperties);
	}

}