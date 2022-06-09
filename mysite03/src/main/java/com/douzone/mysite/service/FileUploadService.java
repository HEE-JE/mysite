package com.douzone.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private static String RESTORE_PATH = "/mysite-uploads";
	private static String URL_BASE = "/assets/gallery";

	public String restore(MultipartFile multipartfile) {
		String url = null;

		try {
			if (multipartfile.isEmpty()) {
				return url;
			}

			File restoreDirectory = new File(RESTORE_PATH);
			if (!restoreDirectory.exists()) {
				restoreDirectory.mkdirs();
			}

			String originFileName = multipartfile.getOriginalFilename();
			String extName = originFileName.substring(originFileName.lastIndexOf('.') + 1);
			String restoreFileName = generateSaveFileName(extName);

			byte[] data = multipartfile.getBytes();
			OutputStream os = new FileOutputStream(RESTORE_PATH + "/" + restoreFileName);
			os.write(data);
			os.close();

			url = URL_BASE + "/" + restoreFileName;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return url;
	}

	private String generateSaveFileName(String extName) {
		String flieName = "";
		Calendar calendar = Calendar.getInstance();

		flieName += calendar.get(Calendar.YEAR);
		flieName += calendar.get(Calendar.MONTH);
		flieName += calendar.get(Calendar.DATE);
		flieName += calendar.get(Calendar.HOUR);
		flieName += calendar.get(Calendar.MINUTE);
		flieName += calendar.get(Calendar.SECOND);
		flieName += calendar.get(Calendar.MILLISECOND);
		flieName += ("." + extName);

		return flieName;
	}
}