package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.mvc.common.HelloMvcUtils;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.service.MemberService;

/**
 * Servlet implementation class MemberLoginServlet
 */
@WebServlet("/member/login")
public class MemberLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 2. 사용자입력값 처리
			String memberId = request.getParameter("memberId");
			String password = HelloMvcUtils.getEncryptedPassword(request.getParameter("password"), memberId);
			String saveId= request.getParameter("saveId");
			System.out.println("memberId = " + memberId);
			System.out.println("password = " + password);
			System.out.println("saveId = " + saveId); // 체크한 경우 "on" | 미체크한 경우 null
			
			// 3. 업무로직 : 로그인여부 판단
			Member member = memberService.findById(memberId);
			System.out.println("member@MemberLoginServlet = " + member);
			
			HttpSession session = request.getSession(true); // 세션이 존재하지 않으면, 새로 생성해서 반환. true는 생략가능
			// 생명주기가 request보다 길기때문에 request를 session으로 변경함(header.jsp도)
			System.out.println(session.getId()); // 클라이언트쪽과 동일_cookie값과 동일하다(session id)
			
			// 로그인 성공
			if(member != null && password.equals(member.getPassword())) {
				session.setAttribute("loginMember", member);
				
				// saveId 처리(쿠기값을 통해서 처리) - key / value 값을 가짐
				Cookie cookie = new Cookie("saveId", memberId); // servlet cookie import
				cookie.setPath(request.getContextPath());
				
				// saveId를 사용하는 경우
				if(saveId != null) {
					// /mvc -> /mvc로 시작하는 요청주소에 cookie를 함께 전송(/mvc, /mvc/board, /mvc/admin...일때 모두 cookie를 전송함, contextPath로 시작하면)
					// session cookie(서버에 접속한 동안만 client에 보관) | persistent cookie(maxAge를 지정한 경우)
					cookie.setMaxAge(7 * 24 * 60 * 60); // 초단위로 설정 - 7일(7 * 24 * 60 * 60)	
				}
				// saveId를 사용하지 않는 경우
				else {
					cookie.setMaxAge(0); // 즉시 삭제	
				}
				response.addCookie(cookie); // 응답메세지에 Set-cookie 항목으로 전송됨
			} 
			// 로그인 실패(아이디가 존재하지 않는 경우 || 비밀번호를 틀린 경우)
			else {
				session.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			
			// 4. 응답 처리: 로그인 후 url변경을 위해 리다이렉트처리
			// 응답 302 redirect 전송.
			// 브라우저에게 302 location으로 재요청을 명령.
			// forward - DQL(requestDispatcher) / redirect - DML(responce)
			response.sendRedirect(request.getContextPath() + "/"); // /mvc/ -> 그냥 /mvc를하면 파일명인줄 알기때문에 끝에도 / 를 붙여준다
		} catch (IOException e) {
			e.printStackTrace(); // 로깅
			throw e; // tomcat에게 예외던짐
		}
		
	}

}
