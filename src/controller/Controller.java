package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javaConstants.Constants;

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
		if (("sendMail").equals(mode)) {
			try {
				ReadPropertiesFile.readConfig(request, response);
			} catch (FileNotFoundException e) {
				String errMsg = "FileNotFoundException.!!!";
				RedirectToError.errorPage(request, response, errMsg);
				e.printStackTrace();
			} catch (IOException e) {
				String errMsg = "IOException..!!";
				RedirectToError.errorPage(request, response, errMsg);
				e.printStackTrace();
			}
			if (Constants.setStatus.equals("enable")) {
				try {
					Main.dataForSendMail(request, UPLOAD_DIR_IMG, response);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (Constants.setStatus.equals("disable")) {
				System.out.println("Mail functionality disabled");
			}
		} else if (("updateCronJobTimeToRun").equals(mode)) {
			try {
				CronJob.unscheduleCronJob(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (("dashboard").equals(mode)) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				redirectToMainPageWithData(request, response);
			} else {
				response.sendRedirect("./pages/Login.jsp");
			}
		} else if (("checkExpiry").equals(mode)) {
			checkExpiry(request, response);
		} else {
			response.getWriter().append("CronJob Scheduled: ")
					.append(request.getContextPath());
			try {
				CronJob.ScheduleCronJob(request, response);
			} catch (Exception e) {
				e.printStackTrace();
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
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		RequestDispatcher rd = request
				.getRequestDispatcher("pages/ChangePassword.jsp");
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String mode = request.getParameter("mode");
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
		} else if (("loginAuthentication").equals(mode)) {
			loginAuthentication(request, response);
		} else if ("recoveryEmail".equals(mode)) {
			recoveryEmail(request, response);
		} else if ("changePassword".equals(mode)) {
			changePassword(request, response);
		} else if ("logout".equals(mode)) {
			System.out.println("in logout");
			userLogout(request, response);
		}
	}

	private void changePassword(HttpServletRequest request,
			HttpServletResponse response) {
		String conf_pass = request.getParameter("conf_pass");
		try {
			int status = GenericUtility.changePassword(conf_pass, request,
					response);
			response.getWriter().write(new Gson().toJson(status));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void recoveryEmail(HttpServletRequest request,
			HttpServletResponse response) {
		// SMTP server information
		try {
			ReadPropertiesFile.readConfig(request, response);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SecurityManager s = new SecurityManager();
		PasswordRecoveryMail mailer = new PasswordRecoveryMail();
		try {
			String status = "true";

			mailer.sendHtmlEmail(request, response);
			response.getWriter().write(new Gson().toJson(status));
			System.out.println("Email sent.");
		} catch (Exception ex) {
			System.out.println("Failed to sent email.");
			ex.printStackTrace();
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
		} catch (ClassNotFoundException | SQLException
				| NoSuchAlgorithmException | IOException e) {
		}
		try {
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
		} catch (SQLException | IOException e) {

			e.printStackTrace();
		}
	}

	public void userLogout(HttpServletRequest request,
			HttpServletResponse response) {
		// check for an existing session and invalidate it if exists
		HttpSession session = request.getSession(false);

		if (session != null) {

			session.getId();
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
		if (selectedOption.equals("Last Week")) {
			try {
				mailLogs = GenericUtility
						.getPastWeekMailLogs(request, response);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				mailLogs = GenericUtility.getAllMailLogs(request, response);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		try {
			response.getWriter().write(new Gson().toJson(mailLogs));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTemplatesInProperties(HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "";
		String[] selected_value = request.getParameterValues("templateImgs");
		if (selected_value.length == 0) {
			msg = "validation Error";
		} else {
			String[] selected_value_Split = new String[selected_value.length];
			for (int i = 0; i < selected_value.length; i++) {
				selected_value_Split[i] = selected_value[i].split("/")[1];
			}
			String[] final_Templates = new String[selected_value_Split.length];
			for (int i = 0; i < selected_value_Split.length; i++) {
				final_Templates[i] = selected_value_Split[i].split("\\.")[0];
			}
			String templatesSelected = "";
			for (int i = 0; i < final_Templates.length; i++) {
				templatesSelected += final_Templates[i] + ",";
			}
			String path = request.getServletContext().getRealPath(
					File.separator)
					+ "MailSendingApplication.properties";
			FileInputStream in = null;
			try {
				in = new FileInputStream(path);
				Properties props = new Properties();
				props.load(in);
				in.close();
				FileOutputStream out = null;
				out = new FileOutputStream(path);
				props.setProperty("templates", templatesSelected);
				props.store(out, null);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			msg = "success";
		}
		try {
			response.getWriter().write(new Gson().toJson(msg));
		} catch (IOException e) {

			e.printStackTrace();
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
			ReadPropertiesFile.readConfig(request, response);
			String[] templatesFromPropertiesFile = Constants.templates
					.split(",");
			ArrayList<String> templatesAlreadySet = new ArrayList<String>();
			for (int i = 0; i < templatesFromPropertiesFile.length; i++) {
				templatesAlreadySet.add(templatesFromPropertiesFile[i]);
			}
			List<HashMap> pastWeekMailLogs = new ArrayList<HashMap>();
			try {
				pastWeekMailLogs = GenericUtility.getPastWeekMailLogs(request,
						response);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("pastWeekMailLogs", pastWeekMailLogs);
			request.setAttribute("templateImg", imagePath);
			request.setAttribute("templatesSelected", templatesAlreadySet);
			RequestDispatcher rd = request
					.getRequestDispatcher("pages/Index.jsp");
			rd.forward(request, response);
		} catch (NullPointerException e) {
			String errMsg = "Something went wrng..!!!";
			RedirectToError.errorPage(request, response, errMsg);
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void zipUpload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator
				+ UPLOAD_DIR_IMG;
		String fileName = null;
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		try {
			for (Part part : request.getParts()) {
				fileName = GenericUtility.getFileName(part);
				part.write(uploadFilePath + File.separator + fileName);
			}
			if (fileName.split("\\.")[1].equals("zip")) {
				UnzipImages.unzipFile(request, fileName, uploadFilePath);
				File file = new File(uploadFilePath + File.separator + fileName);
				if (file.delete()) {
					System.out.println(file.getName() + " is deleted!");
				} else {
					System.out.println("Delete operation is failed.");
				}
			} else {
				for (Part part : request.getParts()) {
					fileName = GenericUtility.getFileName(part);
					part.write(uploadFilePath + File.separator + fileName);
				}
			}
			String msg = "success";
			response.getWriter().write(new Gson().toJson(msg));
		} catch (IllegalStateException | IOException | ServletException e) {
			String msg = e.getMessage();
			response.getWriter().write(new Gson().toJson(msg));
			e.printStackTrace();
		}
	}

	public void uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
		File fileSaveDir = new File(uploadFilePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}
		String fileName = null;
		try {
			for (Part part : request.getParts()) {
				fileName = GenericUtility.getFileName(part);

				part.write(uploadFilePath + File.separator + fileName);
				AddXlsToDb.insertData(fileName, uploadFilePath, request,
						response);
				String msg = "success";
				response.getWriter().write(new Gson().toJson(msg));
			}
		} catch (IllegalStateException | IOException | ServletException e) {
			String msg = "exception";
			response.getWriter().write(new Gson().toJson(msg));
			e.printStackTrace();
		}
	}

	public void StatusChange(HttpServletRequest request,
			HttpServletResponse response) {
		String status = request.getParameter("status");

		String path = request.getServletContext().getRealPath(File.separator)
				+ "MailSendingApplication.properties";
		FileInputStream in = null;

		try {
			in = new FileInputStream(path);
			Properties props = new Properties();
			props.load(in);
			in.close();
			FileOutputStream out = null;
			out = new FileOutputStream(path);
			props.setProperty("setStatus", status);
			props.store(out, null);
			out.close();
		} catch (IOException e) {
			String errMsg = "IOException..!!!";
			RedirectToError.errorPage(request, response, errMsg);
		}
	}

	private void setCronJobTime(HttpServletRequest request,
			HttpServletResponse response) {
		String hours = request.getParameter("hours");
		String minutes = request.getParameter("minutes");
		String am_pm = request.getParameter("am/pm");
		String timeToRun = hours + ":" + minutes + " " + am_pm;
		String path = request.getServletContext().getRealPath(File.separator)
				+ "MailSendingApplication.properties";
		FileInputStream in = null;
		try {
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			String errMsg = "FileNotFoundException..!!!";

			RedirectToError.errorPage(request, response, errMsg);
			e.printStackTrace();
		}
		Properties props = new Properties();
		try {
			props.load(in);
			in.close();
		} catch (IOException e) {
			String errMsg = "IOException..!!!";
			RedirectToError.errorPage(request, response, errMsg);
			e.printStackTrace();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			String errMsg = "FileNotFoundException..!!!";

			RedirectToError.errorPage(request, response, errMsg);
			e.printStackTrace();
		}
		props.setProperty("timeToRun", timeToRun);
		try {
			props.store(out, null);
			out.close();
			GenericUtility.callupdateCronJobTime(request, response);
		} catch (IOException e) {
			String errMsg = "IOException..!!!";
			RedirectToError.errorPage(request, response, errMsg);
			e.printStackTrace();
		}
	}
}
