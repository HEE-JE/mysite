package com.douzone.mysite.web.mvc.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.WebUtil;

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long no = Long.parseLong(request.getParameter("no"));

		BoardVo vo = new BoardVo();
		vo.setNo(no);

		BoardVo boardVo = new BoardRepository().findContents(vo);
		new BoardRepository().updateHit(vo);

		request.setAttribute("boardVo", boardVo);
		WebUtil.forword(request, response, "board/view");
	}
}