package utility;

public class SqlQueries {

	public static final String getAllEmpData = "select * from xls2db";
	public static final String getAllMailLogs = "select * from mail_logs";
	public static final String getLastWeekMailLogs = "select * from mail_logs where dateofdelivery >= ? or dateofdelivery<=?";
	public static final String insertXlsToDb = "insert into xls2db(id,name,dob,month,location,email)values(?,?,?,?,?,?)";
	public static final String getEmpOnBirthDay = "select * from xls2db where dob like ?";
	public static final String getAllEmpEmails = "select email from xls2db";
	public static final String insertMail_Logs = "insert into mail_logs(name,email,dateofdelivery)values(?,?,?)";
	public static final String getColumnNames = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'xls2db' ORDER BY ORDINAL_POSITION";
	public static String authenticateUser = "select * from user where username=? and password=?";
	public static String updatePassword = "update user set password=? where id=1";
	public static String updatetimestamp= "update user set timestamp=? where id=1";
	public static String checkLinkToken= "select * from user where timestamp=?";
	
}
