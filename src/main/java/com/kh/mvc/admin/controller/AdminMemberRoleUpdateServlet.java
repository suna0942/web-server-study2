package com.kh.mvc.admin.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.dto.MemberRole;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberRoleUpdateServlet
 */
@WebServlet("/admin/memberRoleUpdate")
public class AdminMemberRoleUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			MemberRole memberRole = MemberRole.valueOf(request.getParameter("memberRole"));
			Member member = new Member();
			member.setMemberId(memberId);
			member.setMemberRole(memberRole);
			System.out.println("member = " + member);
			
			// 2. 업무로직
			int result = memberService.updateMemberRole(member);
			
			// 3. redirect 처리
			request.getSession().setAttribute("msg", "회원권한을 성공적으로 수정했습니다.");
			String referer = request.getHeader("Referer");
//			response.sendRedirect(request.getContextPath() + "/admin/memberList");
			response.sendRedirect(referer);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
