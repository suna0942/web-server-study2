package com.kh.mvc.admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberListServlet
 */
@WebServlet("/admin/memberList")
public class AdminMemberListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자입력값(admin은 없음)
			
			// 2. 업무로직
			// select * from member order by enroll_date desc
			List<Member> list = memberService.findAll();
			System.out.println("list = " + list);
			
			// 3. view단 응답처리
			request.setAttribute("list", list);
			request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp").forward(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
