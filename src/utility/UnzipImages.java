package utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

public class UnzipImages {

	public static void unzipFile(HttpServletRequest request, String fileName,
			String uploadFilePath) throws IOException {

		String applicationPath = request.getServletContext().getRealPath("");
		FileInputStream fis = null;
		ZipInputStream zipIs = null;
		ZipEntry zEntry = null;

		fis = new FileInputStream(uploadFilePath + "/" + fileName);
		zipIs = new ZipInputStream(new BufferedInputStream(fis));
		while ((zEntry = zipIs.getNextEntry()) != null) {
			String filename = zEntry.getName();
			String[] splitted = filename.split("\\.");

			if (splitted[0].matches("\\d{4}")) {

				byte[] tmp = new byte[4 * 1024];
				FileOutputStream fos = null;
				String opFilePath = uploadFilePath + File.separator
						+ zEntry.getName();
				fos = new FileOutputStream(opFilePath);
				int size = 0;
				while ((size = zipIs.read(tmp)) != -1) {
					fos.write(tmp, 0, size);
				}
				fos.flush();
				fos.close();

			} else {
			}
		}

		zipIs.close();

	}

}