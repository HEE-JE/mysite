package com.douzone.mysite.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;

@Service
public class BoardService {
	private static final String COOKIE_NAME = "hit";

	@Autowired
	private BoardRepository boardRepository;

	public List<BoardVo> findAll(int page, String kwd) {
		page = (page - 1) * 5;
		return boardRepository.findAll(page, kwd);
	}

	public Map<String, Object> getPageAndList(int page, String kwd) {
		Map<String, Object> map = new HashMap<>();

		int count = totalCount(kwd) - (5 * (page - 1));
		int lastPage = (totalCount(kwd) - 1) / 5 + 1;
		int startPage = 0, endPage = 0;
		if (page < 4 || lastPage <= 5) {
			startPage = 1;
			endPage = 5;
		} else if ((lastPage - page) > 1) {
			startPage = page - 2;
			endPage = page + 2;
		} else {
			endPage = lastPage;
			startPage = endPage - 4;
		}

		map.put("currentPage", page);
		map.put("kwd", kwd);
		map.put("list", findAll(page, kwd));
		map.put("count", count);
		map.put("startPage", startPage);
		map.put("endPage", endPage);
		map.put("lastPage", lastPage);
		return map;
	}

	public int totalCount(String kwd) {
		return boardRepository.totalCount(kwd);
	}

	public BoardVo getContents(Long no) {
		BoardVo result = null;
		if (no != null) {
			result = boardRepository.findByNo(no);
		}
		return result;
	}

	public BoardVo getContents(Long no, Long userNo) {
		return boardRepository.findByNoAndUserNo(no, userNo);
	}

	public void addContents(BoardVo vo) {
		if (vo.getgNo() != null) {
			boardRepository.updateOrderNo(vo);
		}
		boardRepository.insert(vo);
	}

	public void updateContents(BoardVo vo) {
		boardRepository.update(vo);
	}

	public void deleteContents(Long no, Long userNo) {
		boardRepository.delete(no, userNo);
	}

	public void updateHit(HttpServletResponse response, String cookieHit, Long no) {
		boolean check = false;
		String[] numbers = cookieHit.split("/");
		for (String number : numbers) {
			if (number.equals(String.valueOf(no))) {
				check = true;
				break;
			}
		}

		if (!check) {
			Cookie cookie = new Cookie(COOKIE_NAME, cookieHit + no + "/");
			cookie.setPath("/");
			cookie.setMaxAge(24 * 60 * 60); // 1day
			response.addCookie(cookie);
			boardRepository.updateHit(no);
		}
	}

}