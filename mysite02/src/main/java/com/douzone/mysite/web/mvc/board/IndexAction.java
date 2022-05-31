package com.douzone.mysite.web.mvc.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.WebUtil;

public class IndexAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
		int lastPage = 0;
		int count = 0;

		List<BoardVo> list = new ArrayList<>();
		String kwd = request.getParameter("page") != null ? request.getParameter("kwd") : null;
		if (request.getParameter("kwd") != null) {
			list = new BoardRepository().findByKwd(currentPage, kwd);
			lastPage = (new BoardRepository().countByKwd(kwd) - 1) / 5 + 1;
			count = new BoardRepository().countByKwd(kwd) - (5 * (currentPage - 1));
		} else {
			list = new BoardRepository().findAll(currentPage);
			lastPage = (new BoardRepository().count() - 1) / 5 + 1;
			count = new BoardRepository().count() - (5 * (currentPage - 1));
		}
		int startPage = 0;
		int endPage = 0;

		if (currentPage < 4 || lastPage <= 5) {
			startPage = 1;
			endPage = 5;
		} else if ((lastPage - currentPage) > 1) {
			startPage = currentPage - 2;
			endPage = currentPage + 2;
		} else {
			endPage = lastPage;
			startPage = endPage - 4;
		}

		request.setAttribute("count", count);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("list", list);
		WebUtil.forword(request, response, "board/index");
	}
}