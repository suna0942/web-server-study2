package com.kh.mvc.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.common.HelloMvcUtils;
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
			// 1. 사용자입력값
			int cPage = 1; // 기본값
			int numPerPage = 10;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
				// request.getParameter("cPage")가 null이라면 NumberFormatException 발생하기때문에 try catch절로 잡아줌
			} catch (NumberFormatException e) {}
			
			
			// 2. 업무로직
			// a. content 영역 - paging Query
			int start = (cPage - 1) * numPerPage + 1;
			int end = cPage * numPerPage;
			Map<String, Object> param = new HashMap<>();
			param.put("start", start);
			param.put("end", end);
			
			System.out.printf("cPage = %s, numPerPage = %s, start = %s, end = %s%n", cPage, numPerPage, start, end);
			
			// select * from member order by enroll_date desc 이거에서 수정됨
			// select * from (select row_number () over (order by enroll_date desc) rnum, m.* from member m) m where rnum between ? and ?
			List<Member> list = memberService.findAll(param);
//			System.out.println("list = " + list);
			
			// b. pagebar 영역
			// select count(*) from member
			int totalContent = memberService.getTotalContent();
			System.out.println("totalContent = " + totalContent); // 확인용
			String url = request.getRequestURI();
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalContent, url);
			System.out.println("pagebar = " + pagebar); // 확인용
			
			// 3. view단 응답처리			
			request.setAttribute("list", list);
			request.setAttribute("pagebar", pagebar);
			request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp").forward(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
