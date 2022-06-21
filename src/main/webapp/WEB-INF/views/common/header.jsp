<%@page import="com.kh.mvc.member.model.dto.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%
 	String msg = (String) session.getAttribute("msg");
 	if(msg != null) session.removeAttribute("msg"); // 한 번 만 사용 후 제거
 	Member loginMember = (Member) session.getAttribute("loginMember"); // jsp에서 자동 import = 타입에 커서를 놓고 ctrl+space
 	
 	String saveId = null;
 	Cookie[] cookies = request.getCookies();
 	for(Cookie c : cookies){
 		String name = c.getName();
 		String value = c.getValue();
 		System.out.println("[cookie] " + name + " = " + value);
 		if("saveId".equals(name)){
 			saveId = value;
 		}
 	}
 	
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello MVC</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/style.css" />
<script>
window.onload = () => {
	
<% if(msg != null) { %>
	alert("<%= msg %>");
<% } %>	
<% if(loginMember == null) {%>
	document.loginFrm.onsubmit = (e) => {
		const memberId = document.querySelector("#memberId");
		const password = document.querySelector("#password");
		
		if(!/^.{4,}$/.test(memberId.value)){
			alert("유효한 아이디를 입력해주세요.");
			memberId.select();
			return false;
		}
		
		if(!/^.{4,}$/.test(password.value)){
			alert("유효한 비밀번호를 입력해주세요.");
			password.select();
			return false;
		}
	};
<% } %>
};
</script>
</head>
<body>
	<div id="container">
		<header>
			<h1>Hello MVC</h1>
			<div class="login-container">
			<% if(loginMember == null) { %>
	       	 <!-- 로그인폼 시작 -->
	 	       <form id="loginFrm" name="loginFrm" action="<%= request.getContextPath() %>/member/login" method="POST">
		            <table>
		                <tr>
		                    <td><input type="text" name="memberId" id="memberId" placeholder="아이디" tabindex="1"
		                    		value="<%= saveId != null ? saveId : ""%>"></td>
		                    <td><input type="submit" value="로그인" tabindex="3"></td>
		                </tr>
		                <tr>
		                    <td><input type="password" name="password" id="password" placeholder="비밀번호" tabindex="2"></td>
		                    <td></td>
		                </tr>
		                <tr>
		                    <td colspan="2">
		                        <input type="checkbox" name="saveId" id="saveId"
		                        		<%= saveId != null ? "checked" : "" %>/>
		                        <label for="saveId">아이디저장</label>&nbsp;&nbsp;
		                        <input type="button" value="회원가입"
		                        	onclick="location.href='<%= request.getContextPath() %>/member/memberEnroll';">
		                    </td>
		                </tr>
		            </table>
		        </form>
	      	  <!-- 로그인폼 끝-->
	     	<% } else { %>
	     		<table id="login">
	     			<tr>
	     				<td>[<%= loginMember.getMemberName() %>]님, 안녕하세요.</td>
	     			</tr>
	     			<tr>
	     				<td>
	     					<input type="button" value="내정보보기"
	     						onclick="location.href='<%= request.getContextPath() %>/member/memberView';"/>
	     					<input type="button" value="로그아웃"
	     						onclick="location.href='<%= request.getContextPath() %>/member/logout';"/>
	     				</td>
	     			</tr>
	     		</table>
	     	
	     	<% } %>
	    </div>
	    <!-- 메인메뉴 시작 -->
	    <nav>
	        <ul class="main-nav">
	            <li class="home"><a href="<%= request.getContextPath() %>">Home</a></li>
	            <li class="notice"><a href="#">공지사항</a></li>
	            <li class="board"><a href="#">게시판</a></li>
	        </ul>
	    </nav>
	    <!-- 메인메뉴 끝-->
		</header>
		
		<section id="content">
			