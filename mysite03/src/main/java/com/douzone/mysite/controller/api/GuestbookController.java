package com.douzone.mysite.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.mysite.dto.JSONResult;
import com.douzone.mysite.service.GuestbookService;
import com.douzone.mysite.vo.GuestbookVo;

@RestController("GuestbookApiController")
@RequestMapping("/api/guestbook")
public class GuestbookController {

	@Autowired
	GuestbookService guestbookService;

	@PostMapping("")
	public JSONResult add(@RequestBody GuestbookVo vo) {
		guestbookService.addMessage(vo);
		vo.setPassword("");
		return JSONResult.success(vo);
	}

	@GetMapping("/list/{no}")
	public JSONResult index(@PathVariable("no") Long sno) {
		List<GuestbookVo> list = guestbookService.getMessageList(sno);
		return JSONResult.success(list);
	}

	@DeleteMapping("")
	public JSONResult delete(Long no, String password) {
		Boolean result = guestbookService.deleteMessage(no, password);
		return JSONResult.success(result ? no : -1);
	}
}