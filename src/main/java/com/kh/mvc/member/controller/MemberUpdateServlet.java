package com.kh.mvc.member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberUpdateServlet
 */
@WebServlet("/member/memberUpdate")
public class MemberUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			// 1. 사용자 입력값 처리
			String memberId = request.getParameter("memberId");
			String memberName = request.getParameter("memberName");
			String _gender = request.getParameter("gender");
			String _birthday = request.getParameter("birthday");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String[] hobbies = request.getParameterValues("hobby");
			
			Gender gender = _gender != null ? 
								Gender.valueOf(_gender) : 
									null;
			String hobby = hobbies != null ? 
								String.join(",", hobbies) : 
									null; 
			Date birthday = (_birthday != null && !"".equals(_birthday)) ? 
								Date.valueOf(_birthday) :
									null;
			
			Member member = 
					new Member(memberId, null, memberName, null, gender, 
							birthday, email, phone, hobby, 0, null);
			System.out.println("member@MemberEnrollServlet = " + member);
			
			//2.업무로직
			int result = memberService.updateMember(member);  

			//3. 응답 리다이렉트
			HttpSession session = request.getSession();
			String msg = "";

			if(result>0){
				msg = "회원정보 수정성공!";
				// 세션 정보 갱신
				session.setAttribute("loginMember", memberService.findById(memberId));
			}
			
			session.setAttribute("msg", msg);
			response.sendRedirect(request.getContextPath() + "/member/memberView");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


}
