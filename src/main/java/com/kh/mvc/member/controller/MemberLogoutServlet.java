package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MemberLogoutServlet
 */
@WebServlet("/member/logout")
public class MemberLogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 업무로직: 세션객체를 가져와서 무효화처리
		HttpSession session = request.getSession(false); // 세션객체가 존재하지 않으면 null을 반환
		// 세션객체는 기본적으로 30분동안 유효하며, 처음 로그인한 후 30분동안 아무요청도 안하면 세션만료가 됨 -> null을 리턴하게 됨
		
		if(session != null)
			session.invalidate(); // 세션을 무효화함
		
		// 2. 리다이렉트
		response.sendRedirect(request.getContextPath() + "/");
	}
}
