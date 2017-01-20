package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class GenericUtility {
	public static List getEmployeesWithBirthdayToday(List columnNames,
			List EmpWithBirthDaysList, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException,
			IOException {
		ResultSet rs = null;
		int counter = 1;
		Connection con = null;
		String date = new SimpleDateFormat("dd/MM/").format(new Date());

		int year = Integer.parseInt(new SimpleDateFormat("YYYY")
				.format(new Date()));

		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		try {
			con = ConnectionManager.getConnection(request);
			ps = con.prepareStatement(SqlQueries.getEmpOnBirthDay);
			ps.setString(counter++, date + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				HashMap EmpWithBirthDaysHashMap = new HashMap();

				EmpWithBirthDaysHashMap.put((columnNames.get(0)),
						rs.getString("id"));
				EmpWithBirthDaysHashMap.put(columnNames.get(1),
						rs.getString("name"));
				EmpWithBirthDaysHashMap.put(columnNames.get(2),
						rs.getString("dob"));
				EmpWithBirthDaysHashMap.put(columnNames.get(3),
						rs.getString("month"));
				EmpWithBirthDaysHashMap.put(columnNames.get(4),
						rs.getString("location"));
				EmpWithBirthDaysHashMap.put(columnNames.get(5),
						rs.getString("email"));

				EmpWithBirthDaysList.add(EmpWithBirthDaysHashMap);

			}

			counter = 1;
			boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
			if (!isLeapYear) {
				if (date.equals("01/03/")) {
					ps1 = con.prepareStatement(SqlQueries.getEmpOnBirthDay);
					ps1.setString(counter++, "29/02/%");
					rs1 = ps1.executeQuery();
					while (rs1.next()) {
						HashMap EmpWithBirthDaysHashMap = new HashMap();

						EmpWithBirthDaysHashMap.put((columnNames.get(0)),
								rs1.getString("id"));
						EmpWithBirthDaysHashMap.put(columnNames.get(1),
								rs1.getString("name"));
						EmpWithBirthDaysHashMap.put(columnNames.get(2),
								rs1.getString("dob"));
						EmpWithBirthDaysHashMap.put(columnNames.get(3),
								rs1.getString("month"));
						EmpWithBirthDaysHashMap.put(columnNames.get(4),
								rs1.getString("location"));
						EmpWithBirthDaysHashMap.put(columnNames.get(5),
								rs1.getString("email"));

						EmpWithBirthDaysList.add(EmpWithBirthDaysHashMap);
					}
				}

			}
		} catch (SQLException e) {
			String errMsg = "Something went wrng..!!!";
			RedirectToError.errorPage(request, response, errMsg);
		}

		connectionClose(con, rs, ps);

		return EmpWithBirthDaysList;
	}

	public static int insertMailLogs(HashMap singleEmployee,
			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, FileNotFoundException,
			IOException {
		ResultSet rs = null;
		int counter = 1;
		Connection con = null;
		PreparedStatement ps = null;
		con = ConnectionManager.getConnection(request);

		Date currentDate = new Date();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		String dateOfDelivery = dt1.format(currentDate);
		ps = con.prepareStatement(SqlQueries.insertMail_Logs);
		String recepientsName = "", email = "";
		for (int i = 0; i < singleEmployee.size(); i++) {

			recepientsName = (String) singleEmployee.get("name");
			email = (String) singleEmployee.get("email");
		}
		ps.setString(counter++, recepientsName);
		ps.setString(counter++, email);
		ps.setString(counter++, dateOfDelivery);
		int rows = ps.executeUpdate();
		connectionClose(con, rs, ps);
		return rows;
	}

	public static String getFileName(Part part) {

		String contentDisp = part.getHeader("content-disposition");
		String[] tokens = contentDisp.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2,
						token.length() - 1);
			}
		}
		return "";
	}

	public static List getColumnNames(List columnNames,
			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, FileNotFoundException,
			IOException {
		ResultSet rs = null;
		int counter = 1;
		Connection con = null;
		PreparedStatement ps = null;
		con = ConnectionManager.getConnection(request);
		ps = con.prepareStatement(SqlQueries.getColumnNames);
		rs = ps.executeQuery();
		while (rs.next()) {
			columnNames.add(rs.getString("COLUMN_NAME"));
		}
		connectionClose(con, rs, ps);
		return columnNames;

	}

	public static List getAllEmpEmails(List AllEmails,
			HttpServletRequest request, HttpServletResponse response)
			throws ClassNotFoundException, SQLException, FileNotFoundException,
			IOException {
		ResultSet rs = null;
		int counter = 1;
		Connection con = null;
		PreparedStatement ps = null;
		con = ConnectionManager.getConnection(request);
		ps = con.prepareStatement(SqlQueries.getAllEmpEmails);
		rs = ps.executeQuery();
		while (rs.next()) {
			AllEmails.add(rs.getString("email"));
		}
		connectionClose(con, rs, ps);
		return AllEmails;

	}

	public static List getAllMailLogs(HttpServletRequest request,
			HttpServletResponse response) throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {

		ResultSet rs = null;
		int counter = 1;
		Connection con = null;
		PreparedStatement ps = null;
		con = ConnectionManager.getConnection(request);
		ps = con.prepareStatement(SqlQueries.getAllMailLogs);
		rs = ps.executeQuery();
		List<HashMap> mailLogs = new ArrayList<HashMap>();
		while (rs.next()) {
			HashMap singleLog = new HashMap();
			singleLog.put("id", rs.getInt("id"));
			singleLog.put("name", rs.getString("name"));
			singleLog.put("email", rs.getString("email"));
			singleLog.put("dateofdelivery", rs.getString("dateofdelivery"));
			mailLogs.add(singleLog);

		}
		connectionClose(con, rs, ps);
		return mailLogs;

	}

	public static List getPastWeekMailLogs(HttpServletRequest request,
			HttpServletResponse response) throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {

		ResultSet rs = null;
		int counter = 1;
		Connection con = null;
		PreparedStatement ps = null;
		con = ConnectionManager.getConnection(request);
		ps = con.prepareStatement(SqlQueries.getLastWeekMailLogs);

		Date currentDate = new Date();
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
		String parsedCurrentDate = dt1.format(currentDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, -7);
		Date dateBefore7Days = cal.getTime();
		String parseddateBefore7Days = dt1.format(dateBefore7Days);
		ps.setString(counter++, parseddateBefore7Days);
		ps.setString(counter++, parsedCurrentDate);
		rs = ps.executeQuery();
		List<HashMap> mailLogs = new ArrayList<HashMap>();
		while (rs.next()) {
			HashMap singleLog = new HashMap();
			singleLog.put("id", rs.getInt("id"));
			singleLog.put("name", rs.getString("name"));
			singleLog.put("email", rs.getString("email"));
			singleLog.put("dateofdelivery", rs.getString("dateofdelivery"));
			mailLogs.add(singleLog);

		}
		connectionClose(con, rs, ps);
		return mailLogs;

	}

	public static void XlsToDb(int id, String name, String dob, String month,
			String location, String email, HttpServletRequest request,
			HttpServletResponse response) throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		int counter = 1;
		con = ConnectionManager.getConnection(request);

		pstm = con.prepareStatement(SqlQueries.insertXlsToDb);
		pstm.setInt(counter++, id);
		pstm.setString(counter++, name);
		pstm.setString(counter++, dob);
		pstm.setString(counter++, month);
		pstm.setString(counter++, location);
		pstm.setString(counter++, email);

		int stas = pstm.executeUpdate();
		connectionClose(con, rs, pstm);

	}

	public static ResultSet getAllEmpData(HttpServletRequest request,
			HttpServletResponse response) throws ClassNotFoundException,
			SQLException, FileNotFoundException, IOException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		Statement stmt = null;
		con = ConnectionManager.getConnection(request);
		con.setAutoCommit(false);
		stmt = con.createStatement();

		rs = stmt.executeQuery(SqlQueries.getAllEmpData);

		return rs;

	}

	public static void connectionClose(Connection con, ResultSet rs,
			PreparedStatement ps) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}

	public static ResultSet authenticateUser(String username, String password,
			HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ClassNotFoundException,
			NoSuchAlgorithmException, FileNotFoundException, IOException {

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		Statement stmt = null;
		int counter = 1;
		String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
		String cryptPassword = "";
		cryptPassword = GenericUtility.encryptPassword(password + salt);
		con = ConnectionManager.getConnection(request);
		con.setAutoCommit(false);
		pstm = con.prepareStatement(SqlQueries.authenticateUser);
		pstm.setString(counter++, username);
		pstm.setString(counter++, cryptPassword);
		rs = pstm.executeQuery();
		return rs;
	}

	public static String encryptPassword(String password) throws SQLException,
			ClassNotFoundException, NoSuchAlgorithmException {
		String md5 = null;
		if (null == password)
			return null;
		try {
			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// Update input string in message digest
			digest.update(password.getBytes(), 0, password.length());
			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

	public static void callupdateCronJobTime(HttpServletRequest req,
			HttpServletResponse res) throws IOException {

		String url = req.getRequestURL().toString()
				+ "?mode=updateCronJobTimeToRun";
		hitUrl(url, req.getSession(false).getId());
	}

	public static void callGetForMail(String req) throws IOException {
		String url = req + "?mode=sendMail";
		hitUrl(url, null);
	}

	public static void callGetOfController(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		String url = req.getRequestURL().toString();
		hitUrl(url, req.getSession(false).getId());
	}

	public static void hitUrl(String url, String sessionId) throws IOException {

		final String USER_AGENT = "Mozilla/5.0";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
	}

	public static int changePassword(String conf_pass,
			HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		Statement stmt = null;
		int counter = 1;
		int status = 0;
		try {
			con = ConnectionManager.getConnection(request);

			String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
			String cryptPassword = "";
			cryptPassword = GenericUtility.encryptPassword(conf_pass + salt);

			pstm = con.prepareStatement(SqlQueries.updatePassword);
			counter = 1;
			pstm.setString(counter++, cryptPassword);
			status = pstm.executeUpdate();

		} catch (SQLException | ClassNotFoundException
				| NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return status;
	}

	public static int changeTimeStamp(String timestamp,
			HttpServletRequest request) throws FileNotFoundException,
			IOException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		Statement stmt = null;
		int counter = 1;
		int status = 0;
		try {
			con = ConnectionManager.getConnection(request);

			pstm = con.prepareStatement(SqlQueries.updatetimestamp);
			counter = 1;
			pstm.setString(counter++, timestamp);
			status = pstm.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return status;
	}

	public static ResultSet findLink(String token, HttpServletRequest request,
			HttpServletResponse response) throws SQLException,
			ClassNotFoundException, FileNotFoundException, IOException {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		Statement stmt = null;
		int counter = 1;
		con = ConnectionManager.getConnection(request);
		pstm = con.prepareStatement(SqlQueries.checkLinkToken);
		pstm.setString(counter++, token);
		rs = pstm.executeQuery();
		return rs;

	}

	public static boolean validateTimeToRun(String hours, String minutes,
			String am_pm) {
		if (hours.matches("\\d+") == false || minutes.matches("\\d+") == false
				|| am_pm.matches("[0-9]") == true) {
			return false;
		} else {
			int hoursToInt = Integer.parseInt(hours);
			int minutesToInt = Integer.parseInt(minutes);
			if (hoursToInt >= 1 && hoursToInt <= 12 && minutesToInt >= 0
					&& (am_pm.equals("AM") || am_pm.equals("PM"))) {
				return true;
			} else {
				return false;
			}
		}

	}

	public static boolean checkExtension(Part part) {
		String filename = "", fileExtension = "";
		filename = getFileName(part);
		fileExtension = filename.split("\\.")[1];
		if (fileExtension.equals("xls")) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean checkZipExtension(Part part) {
		String filename = "", fileExtension = "";
		filename = getFileName(part);
		fileExtension = filename.split("\\.")[1];
		if (fileExtension.equals("zip") || fileExtension.equals("jpg")) {
			return true;
		} else {
			return false;
		}
	}

}
