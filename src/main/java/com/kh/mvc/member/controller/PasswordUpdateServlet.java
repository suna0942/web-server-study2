package com.kh.mvc.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class PasswordUpdateServlet
 */
@WebServlet("/member/passwordUpdate")
public class PasswordUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/passwordUpdate.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			String oldPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("oldPassword"), memberId);
			String newPassword = HelloMvcUtils.getEncryptedPassword(request.getParameter("newPassword"), memberId);
			
			// 2. 업무로직
			// a. 기존 비밀번호 검증
			Member member = memberService.findById(memberId);
			
			String msg = null;
			String location = request.getContextPath();
			
			if(member != null && oldPassword.equals(member.getPassword())) {
				// b. 신규 비밀번호 업데이트
				// update member set password =? where member_id = ?
				int result = memberService.updatePassword(memberId, newPassword);
				
				msg = "비밀번호를 성공적으로 변경했습니다.";
				location += "/member/memberView";
				
				// 세션 비밀번호도 갱신
				Member loginMember = (Member) request.getSession().getAttribute("loginMember");
				loginMember.setPassword(newPassword);
			}
			else {
				msg = "기존 비밀번호가 일치하지 않습니다.";
				location += "/member/passwordUpdate";
			}
			// 3. 응답
			// a. 비밀번호 정상 변경 후 내정보보기 페이지로 이동
			// b. 비밀번호 변경 실패시(기존비밀번호 불일치) 비밀번호 변경페이지로 이동
			request.getSession().setAttribute("msg", msg);
			response.sendRedirect(location);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
