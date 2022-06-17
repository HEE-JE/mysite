package com.douzone.mysite.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.exception.FileUploadException;

@Service
public class FileUploadService {
	private static String SAVE_PATH = "/mysite-uploads";
	private static String URL_BASE = "/assets/gallery";

	public String restoreImage(MultipartFile multipartfile) throws FileUploadException {
		try {
			File uploadDirectory = new File(SAVE_PATH);
			if (!uploadDirectory.exists()) {
				uploadDirectory.mkdirs();
			}

			if (multipartfile.isEmpty()) {
				return null;
			}

			String originFileName = multipartfile.getOriginalFilename();
			String extName = originFileName.substring(originFileName.lastIndexOf('.') + 1);
			String saveFileName = generateSaveFileName(extName);

			byte[] data = multipartfile.getBytes();
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
			os.write(data);
			os.close();

			return URL_BASE + "/" + saveFileName;
		} catch (IOException e) {
			throw new FileUploadException("file upload error:" + e);
		}
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