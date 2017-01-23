package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import mail.Main;
import utility.AddXlsToDb;
import utility.CronJob;
import utility.GenericUtility;
import utility.PasswordRecoveryMail;
import utility.ReadPropertiesFile;
import utility.RedirectToError;
import utility.UnzipImages;

import com.google.gson.Gson;

@WebServlet("/controller")
@MultipartConfig
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIR = "uploads";
	private static final String UPLOAD_DIR_IMG = "images";

	public Controller() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String mode = request.getParameter("mode");

		HttpSession session1 = request.getSession(false);
		if (session1 != null) {
			if (("updateCronJobTimeToRun").equals(mode)) {
				try {
					CronJob.unscheduleCronJob(request, response);
				} catch (Exception e) {
					request.setAttribute("errorMessage", e.getMessage());
					GenericUtility.errorForwader(request,response,e);
				}
			} else if (("dashboard").equals(mode)) {
				redirectToMainPageWithData(request, response);

			} else {
				response.getWriter().append("CronJob Scheduled: ")
				.append(request.getContextPath());
				try {
					CronJob.ScheduleCronJob(request, response);
				} catch (Exception e) {
					request.setAttribute("errorMessage", e.getMessage());
					GenericUtility.errorForwader(request,response,e);
				}
			}
		} else {
			if (("sendMail").equals(mode)) {
				try {
					ReadPropertiesFile.readConfig(request);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					request.setAttribute("errorMessage", e.getMessage());
					GenericUtility.errorForwader(request,response,e);
				} 
				if (((HashMap<String, String>) request
						.getAttribute("Properties")).get("setStatus").equals(
								"enable")) {
					try {
						Main.dataForSendMail(request, UPLOAD_DIR_IMG, response);
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute("errorMessage", e.getMessage());
						GenericUtility.errorForwader(request,response,e);
					}
				} else if (((HashMap<String, String>) request
						.getAttribute("Properties")).get("setStatus").equals(
								"disable")) {
					System.out.println("Mail functionality disabled");
				}
			} else if (("checkExpiry").equals(mode)) {
				checkExpiry(request, response);
			} else
				response.sendRedirect("./pages/Login.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)  {
		String mode = request.getParameter("mode");
		HttpSession session = request.getSession(false);

		if (session == null) {
			if (("loginAuthentication").equals(mode)) {
				loginAuthentication(request, response);
			} else if ("recoveryEmail".equals(mode)) {
				recoveryEmail(request, response);
			} else if ("changePassword".equals(mode)) {
				changePassword(request, response);
			} else {
				try {
					response.sendRedirect("./pages/Login.jsp");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (("statusChange").equals(mode)) {
				StatusChange(request, response);
			} else if (("xlsUpload").equals(mode)) {
				uploadFile(request, response);
			} else if (("imageUpload").equals(mode)) {
				zipUpload(request, response);
			} else if (("setCronJobTime").equals(mode)) {
				setCronJobTime(request, response);
			} else if (("setTemplatesInProperties").equals(mode)) {
				setTemplatesInProperties(request, response);
			} else if (("mailLogSelectChange").equals(mode)) {
				mailLogSelectChange(request, response);
			} else if ("logout".equals(mode)) {
				userLogout(request, response);
			}
		}
	}
	private void checkExpiry(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getParameter("token");
		ResultSet rs = null;
		String msg = null;
		try {
			rs = GenericUtility.findLink(token, request, response);
			if (rs.next()) {
				long linkTime = Long.parseLong(token);
				long currentTime = new Date().getTime();
				long timeDifference = currentTime - linkTime;

				if (timeDifference > 86400000) {
					msg = "The link has expired!";
					request.setAttribute("msg", msg);
				} else {
					msg = null;
				}
			} else {
				msg = "The link has expired!";
				request.setAttribute("msg", msg);
			}
			
				RequestDispatcher rd = request
						.getRequestDispatcher("pages/ChangePassword.jsp");
				rd.forward(request, response);
			 
		} catch (SQLException | ClassNotFoundException | IOException | ServletException e1) {
			e1.printStackTrace();
			msg=e1.getMessage();
			request.setAttribute("msg", msg);
			RequestDispatcher rd = request
					.getRequestDispatcher("pages/ChangePassword.jsp");
			try {
				rd.forward(request, response);
			} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	private void changePassword(HttpServletRequest request,
			HttpServletResponse response)  {
		String conf_pass = request.getParameter("conf_pass");
		try {
			int status = GenericUtility.changePassword(conf_pass, request,
					response);
			response.getWriter().write(new Gson().toJson(status));
		} catch (IOException e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e.printStackTrace();
			}
		}
	}

	public void recoveryEmail(HttpServletRequest request,
			HttpServletResponse response) {
		String mailTo = request.getParameter("email");
		String username = request.getParameter("username");
		String userEmail = null;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		String status = "false";

		if (!mailTo.matches(EMAIL_PATTERN)) {
			status = "emailInvalid";

		} else {
			try {
				userEmail = GenericUtility.checkEmailValid(username, request,response);

				if (userEmail.equals(mailTo)) {

					ReadPropertiesFile.readConfig(request);

					SecurityManager s = new SecurityManager();
					PasswordRecoveryMail mailer = new PasswordRecoveryMail();

					mailer.sendHtmlEmail(request, response);
					status = "true";


				} else {
					status = "emailInvalid";
				}
			}
			catch (SQLException | ClassNotFoundException | IOException | MessagingException e1) {
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e1.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			response.getWriter().write(new Gson().toJson(status));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loginAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("key");
		ResultSet user = null;
		String status = "";

		try {
			user = GenericUtility.authenticateUser(username, password, request,
					response);

			if (user.next()) {
				HttpSession session = request.getSession();
				session.setAttribute("name", username);
				String sessionStatus = "true";
				status = "true";
				response.getWriter().write(new Gson().toJson(status));
			} else {
				status = "false";
				response.getWriter().write(new Gson().toJson(status));
			}
		} catch (ClassNotFoundException | NoSuchAlgorithmException | SQLException | IOException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		} 
	}

	public void userLogout(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
			try {
				response.sendRedirect("./pages/Login.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void mailLogSelectChange(HttpServletRequest request,
			HttpServletResponse response) {
		
		String selectedOption = request.getParameter("selectedOption");
		List<HashMap> mailLogs = new ArrayList<HashMap>();
		try {
		if (selectedOption.equals("Last Week")) {
				mailLogs = GenericUtility
						.getPastWeekMailLogs(request, response);
			
		} else {
				mailLogs = GenericUtility.getAllMailLogs(request, response);
		}
			response.getWriter().write(new Gson().toJson(mailLogs));
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		}
	}

	public void setTemplatesInProperties(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ReadPropertiesFile.readConfig(request);
		

		String[] selected_value = request.getParameterValues("templateImgs");

		String relativeWebPath = "/templates";
		String absoluteDiskPath = getServletContext().getRealPath(
				relativeWebPath);
		File templateHtmlFolder = new File(absoluteDiskPath);
		File[] listOfTemplateFiles = templateHtmlFolder.listFiles();
		List templatesList = new ArrayList();
			for (int i = 0; i < listOfTemplateFiles.length; i++) {
				templatesList
				.add(listOfTemplateFiles[i].getName().split("\\.")[0]);
			}

		String[] selected_value_Split = new String[selected_value.length];
		for (int i = 0; i < selected_value.length; i++) {
			selected_value_Split[i] = selected_value[i].split("/")[1];
		}
		String[] final_Templates = new String[selected_value_Split.length];
		for (int i = 0; i < selected_value_Split.length; i++) {
			final_Templates[i] = selected_value_Split[i].split("\\.")[0];
		}

		ArrayList templatesSelected = new ArrayList();
		String templateSelectedString = "";
		ArrayList templatesDiscarded = new ArrayList();

		for (int i = 0; i < final_Templates.length; i++) {
			if (templatesList.contains(final_Templates[i])) {
				templateSelectedString += final_Templates[i] + ",";
				templatesSelected.add(final_Templates[i]);

			} else {
				System.out.println("template name " + final_Templates[i]
						+ " invalid");
				System.out.println("dicarding " + final_Templates[i]);
				templatesDiscarded.add(final_Templates[i]);
			}
		}
		if (templateSelectedString == "") {

			templateSelectedString = ((HashMap<String, String>) request
					.getAttribute("Properties")).get("templates");

		}
		String path = request.getServletContext().getRealPath(File.separator)
				+ "MailSendingApplication.properties";
		FileInputStream in = null;
			in = new FileInputStream(path);
			Properties props = new Properties();
			props.load(in);
			in.close();
			FileOutputStream out = null;
			out = new FileOutputStream(path);

			props.setProperty("templates", templateSelectedString);
			props.store(out, null);
			out.close();
		ArrayList jsonData = new ArrayList();
		jsonData.add(templatesSelected);
		jsonData.add(templatesDiscarded);

		response.getWriter().write(new Gson().toJson(jsonData));
		} catch (IOException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		}
	}

	private void redirectToMainPageWithData(HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException {
		List<String> imagePath = null;
		String relativeWebPath = "/templateImgs";
		String absoluteDiskPath = getServletContext().getRealPath(
				relativeWebPath);
		String relativeWebPath1 = "/templates";
		String absoluteDiskPath1 = getServletContext().getRealPath(
				relativeWebPath1);
		File templateImageFolder = new File(absoluteDiskPath);
		File templateHtmlFolder = new File(absoluteDiskPath1);

		File[] listOfFiles = templateImageFolder.listFiles();
		File[] listOfTemplateFiles = templateHtmlFolder.listFiles();
		List templatesList = new ArrayList();
		try {
			for (int i = 0; i < listOfTemplateFiles.length; i++) {
				templatesList
				.add(listOfTemplateFiles[i].getName().split("\\.")[0]);
			}
			imagePath = new ArrayList<String>();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					if (templatesList.contains(listOfFiles[i].getName().split(
							"\\.")[0])) {
						imagePath.add("templateImgs" + "/"
								+ listOfFiles[i].getName());
					}
				} else if (listOfFiles[i].isDirectory()) {
				}
			}
			ReadPropertiesFile.readConfig(request);
			String[] templatesFromPropertiesFile = ((HashMap<String, String>) request
					.getAttribute("Properties")).get("templates").split(",");
			ArrayList<String> templatesAlreadySet = new ArrayList<String>();
			for (int i = 0; i < templatesFromPropertiesFile.length; i++) {
				templatesAlreadySet.add(templatesFromPropertiesFile[i]);
			}
			List<HashMap> pastWeekMailLogs = new ArrayList<HashMap>();
			
				pastWeekMailLogs = GenericUtility.getPastWeekMailLogs(request,
						response);
			request.setAttribute("pastWeekMailLogs", pastWeekMailLogs);
			request.setAttribute("templateImg", imagePath);
			request.setAttribute("templatesSelected", templatesAlreadySet);
			RequestDispatcher rd = request
					.getRequestDispatcher("pages/Index.jsp");
			rd.forward(request, response);
		} catch (NullPointerException | ServletException | IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			request.setAttribute("errorMessage",e.getMessage());
			RequestDispatcher rd=request.getRequestDispatcher("pages/Error.jsp");
			try {
				rd.forward(request, response);
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			} 
		} 
	}
	private void zipUpload(HttpServletRequest request,
			HttpServletResponse response)  {
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator
				+ UPLOAD_DIR_IMG;
		String fileName = null;
		String msg = "";
		boolean status = false;
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		try {
			for (Part part : request.getParts()) {
				status = GenericUtility.checkZipExtension(part);
			}

			if (status) {
				for (Part part : request.getParts()) {
					fileName = GenericUtility.getFileName(part);
					if (fileName.split("\\.")[1].equals("zip")) {
						part.write(uploadFilePath + File.separator + fileName);
						UnzipImages
						.unzipFile(request, fileName, uploadFilePath);
						File file = new File(uploadFilePath + File.separator
								+ fileName);
						if (file.delete()) {
							msg = "success";
							System.out.println(file.getName() + " is deleted!");
						} else {
							System.out.println("Delete operation is failed.");
						}
					} else {
						if (fileName.split("\\.")[0].matches("\\d{4}")) {
							part.write(uploadFilePath + File.separator
									+ fileName);
							msg = "success";
						} else {
							msg = "formatError";
						}
					}
				}
			} else {
				msg = "extensionError";
			}

			response.getWriter().write(new Gson().toJson(msg));
		} catch (IllegalStateException | IOException | ServletException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		}


	}

	public void uploadFile(HttpServletRequest request,
			HttpServletResponse response)  {
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		boolean status = false;
		String msg = "";
		try {
			File fileSaveDir = new File(uploadFilePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdirs();
			}
			String fileName = null;

			for (Part part : request.getParts()) {
				status = GenericUtility.checkExtension(part);
			}
			if (status) {
				for (Part part : request.getParts()) {
					fileName = GenericUtility.getFileName(part);
					part.write(uploadFilePath + File.separator + fileName);
					AddXlsToDb.insertData(fileName, uploadFilePath, request,
							response);
					msg = "success";
				}
			} else {
				msg = "error";
			}
			response.getWriter().write(new Gson().toJson(msg));
		} catch (IOException | IllegalStateException | ServletException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		}
	}

	public void StatusChange(HttpServletRequest request,
			HttpServletResponse response) {
		String status = request.getParameter("status");
		try {
			if (status.equals("enable") || status.equals("disable")) {

				String path = request.getServletContext().getRealPath(
						File.separator)
						+ "MailSendingApplication.properties";
				FileInputStream in = null;


				in = new FileInputStream(path);
				Properties props = new Properties();
				props.load(in);
				in.close();
				FileOutputStream out = null;
				out = new FileOutputStream(path);
				props.setProperty("setStatus", status);
				props.store(out, null);
				out.close();
			} else {
				status = "invalid";
			}
			response.getWriter().write(new Gson().toJson(status));
		} catch (IOException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		}
	}

	private void setCronJobTime(HttpServletRequest request,
			HttpServletResponse response) {
		String hours = request.getParameter("hours");
		String minutes = request.getParameter("minutes");
		String am_pm = request.getParameter("am/pm");
		try{
			boolean validationCheck = GenericUtility.validateTimeToRun(hours,
					minutes, am_pm);
			if (!validationCheck) {
				String status = "invalid";
				response.getWriter().write(new Gson().toJson(status));
			} else {
				String timeToRun = hours + ":" + minutes + " " + am_pm;
				String path = request.getServletContext().getRealPath(
						File.separator)
						+ "MailSendingApplication.properties";
				FileInputStream in = null;
				in = new FileInputStream(path);
				Properties props = new Properties();
				props.load(in);
				in.close();
				FileOutputStream out = null;
				out = new FileOutputStream(path);
				props.setProperty("timeToRun", timeToRun);
				HttpSession session = request.getSession(false);
				props.store(out, null);
				out.close();
				GenericUtility.callupdateCronJobTime(request, response);
				response.getWriter().write(new Gson().toJson(timeToRun));
			} 
		}catch (IOException e) {
			e.printStackTrace();
			GenericUtility.redirectError(response, e);
		}
	}
	
}




