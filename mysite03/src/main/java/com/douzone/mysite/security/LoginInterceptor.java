package com.douzone.mysite.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.douzone.mysite.service.UserService;
import com.douzone.mysite.vo.UserVo;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		UserVo authUser = userService.getUser(email, password);
		if (authUser == null) {
			request.setAttribute("email", email);
			request.setAttribute("result", "fail");
			request.getRequestDispatcher("WEB-INF/views/user/login.jsp").forward(request, response);
			return false;
		}

		/* session 처리 */
		System.out.println(authUser);
		return false;
	}

}