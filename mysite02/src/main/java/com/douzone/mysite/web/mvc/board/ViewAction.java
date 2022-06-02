package com.douzone.mysite.web.mvc.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.douzone.mysite.repository.BoardRepository;
import com.douzone.mysite.vo.BoardVo;
import com.douzone.web.mvc.Action;
import com.douzone.web.util.WebUtil;

public class ViewAction implements Action {
	private static final String COOKIE_NAME = "hit";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Long no = Long.parseLong(request.getParameter("no"));

		BoardVo vo = new BoardVo();
		vo.setNo(no);

		BoardVo boardVo = new BoardRepository().findByNo(vo);

		String hit = "";
		Cookie hitCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName())) {
					hitCookie = cookie;
					String[] numbers = cookie.getValue().split("/");
					boolean check = true;
					for (String number : numbers) {
						if (number.equals(String.valueOf(no))) {
							hit = cookie.getValue();
							check = false;
							break;
						}
					}
					if (check) {
						hit = cookie.getValue() + no + "/";
						hitCookie.setValue(String.valueOf(hit));
						response.addCookie(hitCookie);
						new BoardRepository().updateHit(vo);
					}
				}
			}
		}

		if (hitCookie == null) {
			hitCookie = new Cookie(COOKIE_NAME, String.valueOf(hit));
			hitCookie.setPath(request.getContextPath());
			hitCookie.setMaxAge(24 * 60 * 60);
			response.addCookie(hitCookie);
		}

		request.setAttribute("boardVo", boardVo);
		WebUtil.forword(request, response, "board/view");
	}
}