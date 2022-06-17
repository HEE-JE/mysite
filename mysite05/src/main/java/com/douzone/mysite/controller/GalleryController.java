package com.douzone.mysite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.service.FileUploadService;
import com.douzone.mysite.service.GalleryService;
import com.douzone.mysite.vo.GalleryVo;

@Controller
@RequestMapping("/gallery")
public class GalleryController {

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private GalleryService galleryService;

	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("list", galleryService.getImages());
		return "gallery/index";
	}

	@Auth(role = "ADMIN")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam(value = "comments", required = true, defaultValue = "") String comments,
			@RequestParam("file") MultipartFile multipartfile) {
		String url = fileUploadService.restoreImage(multipartfile);
		GalleryVo vo = new GalleryVo();
		vo.setUrl(url);
		vo.setComments(comments);
		galleryService.saveImage(vo);
		return "redirect:/gallery";
	}

	@Auth(role = "ADMIN")
	@RequestMapping("/delete/{no}")
	public String remove(@PathVariable("no") Long no) {
		galleryService.removeImage(no);
		return "redirect:/gallery";
	}
}