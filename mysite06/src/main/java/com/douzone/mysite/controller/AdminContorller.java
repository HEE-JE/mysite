package com.douzone.mysite.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.service.FileUploadService;
import com.douzone.mysite.service.SiteService;
import com.douzone.mysite.vo.SiteVo;

@Auth(role = "ADMIN")
@Controller
@RequestMapping("/admin")
public class AdminContorller {

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private SiteService siteService;

	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping("")
	public String main() {
		return "admin/main";
	}

	@RequestMapping("/main/update")
	public String update(SiteVo vo, @RequestParam("file") MultipartFile multipartFile) {
		String url = fileUploadService.restoreImage(multipartFile);
		vo.setProfileURL(url);
		siteService.updatesite(vo);

		vo = siteService.getSite();
		servletContext.setAttribute("site", vo);
		return "redirect:/admin";
	}

	@RequestMapping("/guestbook")
	public String guestbook() {
		return "admin/guestbook";
	}

	@RequestMapping("/board")
	public String board() {
		return "admin/board";
	}

	@RequestMapping("/user")
	public String user() {
		return "admin/user";
	}
}