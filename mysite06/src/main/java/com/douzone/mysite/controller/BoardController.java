package com.douzone.mysite.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douzone.mysite.security.Auth;
import com.douzone.mysite.security.AuthUser;
import com.douzone.mysite.service.BoardService;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.mysite.vo.UserVo;
import com.douzone.mysite.web.WebUtil;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping("")
	public String index(@RequestParam(value = "p", required = true, defaultValue = "1") Integer page,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		Map<String, Object> map = boardService.getPageAndList(page, kwd);
		model.addAttribute("map", map);
		return "board/index";
	}

	@RequestMapping("/view/{no}")
	public String view(HttpServletResponse response,
			@CookieValue(value = "hit", required = true, defaultValue = "") String cookieHit,
			@PathVariable("no") Long no, Model model) {
		boardService.updateHit(response, cookieHit, no);
		model.addAttribute("boardVo", boardService.getContents(no));
		return "board/view";
	}

	@Auth
	@RequestMapping(value = { "/write", "/write/{no}" }, method = RequestMethod.GET)
	public String write(@PathVariable(value = "no", required = false) Long no, Model model) {
		BoardVo vo = boardService.getContents(no);
		model.addAttribute("boardVo", vo);
		return "board/write";
	}

	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@AuthUser UserVo authUser, BoardVo vo,
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer page,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd) {
		vo.setUserNo(authUser.getNo());
		boardService.addContents(vo);
		return "redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(kwd, "UTF-8");
	}

	@Auth
	@RequestMapping(value = "/modify/{no}", method = RequestMethod.GET)
	public String modify(@AuthUser UserVo authUser, @PathVariable("no") Long no, Model model) {
		BoardVo vo = boardService.getContents(no, authUser.getNo());
		model.addAttribute("boardVo", vo);
		return "board/modify";
	}

	@Auth
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@AuthUser UserVo authUser, BoardVo boardVo,
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer page,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd) {
		boardVo.setUserNo(authUser.getNo());
		boardService.updateContents(boardVo);
		return "redirect:/board/view/" + boardVo.getNo() + "?p=" + page + "&kwd=" + WebUtil.encodeURL(kwd, "UTF-8");
	}

	@Auth
	@RequestMapping("/delete/{no}")
	public String delete(@AuthUser UserVo authUser, @PathVariable("no") Long no, @PathVariable("no") Long boardNo,
			@RequestParam(value = "p", required = true, defaultValue = "1") Integer page,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd) {
		boardService.deleteContents(no, authUser.getNo());
		return "redirect:/board?p=" + page + "&kwd=" + WebUtil.encodeURL(kwd, "UTF-8");
	}
}