package com.douzone.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.douzone.mysite.service.BoardService;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping("")
	public String index(@RequestParam(value = "p", required = true, defaultValue = "1") int page,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		int lastPage = (boardService.totalCount(kwd) - 1) / 5 + 1;

		model.addAttribute("currentPage", page);
		model.addAttribute("list", boardService.getPageList(page, kwd));
		model.addAttribute("count", boardService.totalCount(kwd) - (5 * (page - 1)));
		model.addAttribute("startPage", boardService.getPage(page, lastPage).get("startPage"));
		model.addAttribute("endPage", boardService.getPage(page, lastPage).get("endPage"));
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("kwd", kwd);
		return "board/index";
	}

	@RequestMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, Model model) {
		BoardVo vo = boardService.getContents(no);
		boardService.updateHit(no);
		model.addAttribute("boardVo", vo);
		return "board/view";
	}

	@RequestMapping(value = { "/write", "/write/{no}" }, method = RequestMethod.GET)
	public String write(HttpSession session, @PathVariable(value = "no", required = false) Long no, Model model) {
		// 접근제어(Access Control)
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		///////////////////////////

		BoardVo vo = boardService.getContents(no);
		model.addAttribute("boardVo", vo);
		return "board/write";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(HttpSession session, BoardVo vo) {
		// 접근제어(Access Control)
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		///////////////////////////

		vo.setUserNo(authUser.getNo());
		boardService.write(vo);
		return "redirect:/board";
	}

	@RequestMapping(value = "/modify/{no}", method = RequestMethod.GET)
	public String modify(HttpSession session, @PathVariable("no") Long no, Model model) {
		// 접근제어(Access Control)
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		///////////////////////////

		BoardVo vo = boardService.getContents(no);
		model.addAttribute("boardVo", vo);
		return "board/modify";
	}

	@RequestMapping(value = "/modify/{no}", method = RequestMethod.POST)
	public String modify(HttpSession session, @PathVariable("no") Long no, BoardVo vo) {
		// 접근제어(Access Control)
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		///////////////////////////

		boardService.updateContents(vo);
		return "redirect:/board/view/{no}";
	}

	@RequestMapping("/delete/{no}")
	public String delete(HttpSession session, @PathVariable("no") Long no, BoardVo vo) {
		// 접근제어(Access Control)
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			return "redirect:/";
		}
		///////////////////////////

		boardService.delete(no);
		return "redirect:/board";
	}
}