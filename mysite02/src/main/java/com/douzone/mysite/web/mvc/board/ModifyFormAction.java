package com.douzone.mysite.web.mvc.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.mysite.vo.UserVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.WebUtil;

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 접근제어(Access Control)
		/////////////////////////////////////////////////////////////////////
		HttpSession session = request.getSession();
		if (session == null) {
			WebUtil.forword(request, response, "user/loginform");
			return;
		}

		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if (authUser == null) {
			WebUtil.forword(request, response, "user/loginform");
			return;
		}
		/////////////////////////////////////////////////////////////////////

		Long no = Long.parseLong(request.getParameter("no"));

		BoardVo vo = new BoardVo();
		vo.setNo(no);

		BoardVo boardVo = new BoardRepository().findContents(vo);

		request.setAttribute("boardVo", boardVo);
		WebUtil.forword(request, response, "board/modify");
	}
}