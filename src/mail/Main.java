package mail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javaConstants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.read.biff.BiffException;
import utility.GenericUtility;
import utility.ReadPropertiesFile;

public class Main {

	public static void getEmpWithBday(List EmpWithBirthDaysList,
			List AllEmails, HttpServletRequest request,HttpServletResponse response) throws BiffException,
			IOException, ClassNotFoundException, SQLException {
		List columnNames = new ArrayList();

		columnNames = GenericUtility.getColumnNames(columnNames);

		EmpWithBirthDaysList = GenericUtility.getEmployeesWithBirthdayToday(
				columnNames, EmpWithBirthDaysList, request, response);

		AllEmails = GenericUtility.getAllEmpEmails(AllEmails);

	}

	public static String PlaceholderReplace(String content,
			HashMap retrievedEmployee, HttpServletRequest request) {
		while (content.indexOf("{") != -1) {

			String result = content.substring(content.indexOf("{") + 2,
					content.indexOf("}"));
			final String reg = "\\{\\{(" + result + ")\\}\\}";
			if (result.equals("imgSrc")) {
				String id = (String) retrievedEmployee.get("Id");
				String value = request.getServletContext().getRealPath("")
						+ "/images/" + id + ".jpg";
				content = StringReplacer(content, reg, value);
				PlaceholderReplace(content, retrievedEmployee, request);
			} else {
				String id = (String) retrievedEmployee.get("Id");
				String value = (String) retrievedEmployee.get(result);
				if (value == "" || value == null) {
					content = StringReplacer(content, reg, result);
				} else {
					content = StringReplacer(content, reg, value);
					PlaceholderReplace(content, retrievedEmployee, request);
				}
			}
		}
		return content;
	}

	public static String StringReplacer(String content, String regex,
			String valueToReplaceWith) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		content = m.replaceAll(valueToReplaceWith);
		return content;
	}

	public static List ReadAllEmpData(int totalNoOfRows, int totalNoOfColumns,
			List columnNames, Sheet sheet) {
		HashMap<Object, String> AllEmpDataHashMap = null;
		List AllEmpDataList = new ArrayList();

		for (int row = 1; row < totalNoOfRows; row++) {
			AllEmpDataHashMap = new HashMap();
			for (int col = 0; col < totalNoOfColumns; col++) {
				AllEmpDataHashMap.put(columnNames.get(col),
						sheet.getCell(col, row).getContents());
			}
			if (AllEmpDataHashMap != null
					&& AllEmpDataHashMap.keySet().size() > 0) {
				AllEmpDataList.add(AllEmpDataHashMap);
			}
		}
		return AllEmpDataList;
	}

	public static String readAndParseRandomTemplateFile(String[] templates,
			HttpServletRequest request) {

		String content = "";
		BufferedReader in = null;
		String folderName = "/templates";
		
		StringBuilder contentBuilder = new StringBuilder();
		
	
		String absolutePath = request.getSession().getServletContext()
				.getRealPath(folderName);
		String folderPath = absolutePath + "/";
		
		if (templates.length == 1) {
			try {
				in = new BufferedReader(new FileReader(folderPath + templates[0]
						+ ".html"));
			} catch (FileNotFoundException e) {
			System.out.println("in first catch");
				e.printStackTrace();
			}
		} else {
			try {
				in = new BufferedReader(new FileReader(folderPath
						+ templates[getRandomInteger(templates.length, 1)]
						+ ".html"));
			} catch (FileNotFoundException e) {
				System.out.println("in 2 catch");
				e.printStackTrace();
			}
		}

		String str = null;
		try {
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		content = contentBuilder.toString();
		return content;

	}

	public static String CreateTemplate(HashMap employeeData,
			HttpServletRequest request) throws Exception {

		ReadPropertiesFile.readConfig();
		String emailBody = "";
		String[] templates = Constants.templates.split(",");

		String templateParsedToString = readAndParseRandomTemplateFile(
				templates, request);
		emailBody = PlaceholderReplace(templateParsedToString, employeeData,
				request);
		return emailBody;
	}

	public static int getRandomInteger(int maximum, int minimum) {
		Random rnd = new Random();
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

	 public static void dataForSendMail(HttpServletRequest request,
			   String UPLOAD_DIR_IMG, HttpServletResponse response)
			   throws Exception {
			  ArrayList EmpWithBirthDaysList = new ArrayList();
			  ArrayList TemplateStrings = new ArrayList();
			  ArrayList<String> AllEmails = new ArrayList<String>();
			  String id = "";

			  ReadPropertiesFile.readConfig();

			  getEmpWithBday(EmpWithBirthDaysList, AllEmails, request, response);

			  for (int i = 0; i < EmpWithBirthDaysList.size(); i++) {
			   HashMap singleEmployee = new HashMap();
			   singleEmployee = (HashMap) EmpWithBirthDaysList.get(i);
			   String emailTo = (String) singleEmployee.get("email");
			   // String emailBody = TemplateStrings.get(i).toString();
			   String emailBody = CreateTemplate(singleEmployee, request);
			   String[] bcc = new String[AllEmails.size()];
			   bcc = AllEmails.toArray(bcc);
			   id = (String) singleEmployee.get("id");
			   System.out.println("Creating template for email!");
			   new SendEmail().sendMail(Constants.setSubject, emailBody,
			     singleEmployee, bcc, request, UPLOAD_DIR_IMG);
			   int status = GenericUtility.insertMailLogs(singleEmployee);
			  }

			 }
}
