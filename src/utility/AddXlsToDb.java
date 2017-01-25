package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

public class AddXlsToDb {
	static FileInputStream input;
	static HSSFWorkbook wb;
	static POIFSFileSystem fs;
	static ResultSet rs;

	public static void insertData(String fileName, String uploadFilePath,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ClassNotFoundException, SQLException {

		input = new FileInputStream(uploadFilePath + File.separator + fileName);
		fs = new POIFSFileSystem(input);
		wb = new HSSFWorkbook(fs);

		HSSFSheet sheet = wb.getSheetAt(0);
		Row row;

		rs = GenericUtility.getAllEmpData(request, response);

		if (!rs.next()) {
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);

				int id = Integer.parseInt(row.getCell(0).getStringCellValue());
				String name = row.getCell(1).getStringCellValue();
				String dob = row.getCell(2).getStringCellValue();
				String month = row.getCell(3).getStringCellValue();
				String location = row.getCell(4).getStringCellValue();
				String email = row.getCell(5).getStringCellValue();

				GenericUtility.XlsToDb(id, name, dob, month, location, email,
						request, response);

			}
			System.out.println("Adding xls data to DB");
			System.out.println("Success import excel to database");
		} else {
			rs.previous();
			HashMap allEmpId = new HashMap();
			while (rs.next()) {
				allEmpId.put(rs.getInt("id"), rs.getInt("id"));
			}
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				int id = Integer.parseInt(row.getCell(0).getStringCellValue());
				for (int j = 0; j < allEmpId.size(); j++) {
					if (allEmpId.get(id) == null) {
						allEmpId.put(id, id);
						String name = row.getCell(1).getStringCellValue();
						String dob = row.getCell(2).getStringCellValue();
						String month = row.getCell(3).getStringCellValue();
						String location = row.getCell(4).getStringCellValue();
						String email = row.getCell(5).getStringCellValue();

						GenericUtility.XlsToDb(id, name, dob, month, location,
								email, request, response);
					}
				}

			}
			System.out.println("DB updated successfully");
		}
		GenericUtility.connectionClose(null, rs, null);
		input.close();

	}
}
