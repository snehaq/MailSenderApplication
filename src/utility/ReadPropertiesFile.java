package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javaConstants.Constants;

public class ReadPropertiesFile {
	public static void readConfig()throws FileNotFoundException,IOException{
	
			Properties pro = new Properties();
			try {
				String path = System.getProperty("user.home")
						+ "/Desktop/MailSendingApplication.properties";
				pro.load(new FileInputStream(path));
			} catch (IOException e) {
				System.out.println("FileNotFoundException in readP");
				String errMsg="FileNotFoundException.!!!";
				
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