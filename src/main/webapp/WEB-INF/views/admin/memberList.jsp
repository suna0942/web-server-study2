<%@page import="com.kh.mvc.member.model.dto.Gender"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	List<Member> memberList = (List<Member>) request.getAttribute("list");
	String type = request.getParameter("searchType");
	String kw = request.getParameter("searchKeyword");
%>
<!-- 관리자용 admin.css link -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/admin.css" />
<style>
	div#search-container {
		width: 100%;
		margin: 0 0 10px 0;
		padding: 3px;
		background-color: rgba(0,188,212, .3);
	}
	div#search-memberId {
		display: <%= type == null || "member_id".equals(type) ? "inline-block" : "none"%>;
	}
	div#search-memberName {
		display: <%= "member_name".equals(type) ? "inline-block" : "none"%>;
	}
	div#search-gender {
		display: <%= "gender".equals(type) ? "inline-block" : "none"%>;
	}
</style>
<script>
window.addEventListener('load', (e) => {
	document.querySelector("select#searchType").onchange = (e) => {
		document.querySelectorAll(".search-type").forEach((div, index) => {
			div.style.display = "none";
		});
		let id;
		switch(e.target.value){
		case "member_id" : id = "memberId"; break;
		case "member_name" : id = "memberName"; break;
		case "gender" : id = "gender"; break;
		}
		document.querySelector(`#search-\${id}`).style.display = "inline-block";
	};
});
</script>
<section id="memberList-container">
	<h2>회원관리</h2>
	    <div id="search-container">
        <label for="searchType">검색타입 :</label> 
        <select id="searchType">
            <option value="member_id" <%= "member_id".equals(type) ? "selected" : "" %>>아이디</option>        
            <option value="member_name" <%= "member_name".equals(type) ? "selected" : "" %>>회원명</option>
            <option value="gender" <%= "gender".equals(type) ? "selected" : "" %>>성별</option>
        </select>
        <div id="search-memberId" class="search-type">
            <form action="<%=request.getContextPath()%>/admin/memberFinder">
                <input type="hidden" name="searchType" value="member_id"/>
                <input type="text" name="searchKeyword" size="25" placeholder="검색할 아이디를 입력하세요." value="<%= "member_id".equals(type) ? kw : ""%>"/>
                <button type="submit">검색</button>            
            </form>    
        </div>
        <div id="search-memberName" class="search-type">
            <form action="<%=request.getContextPath()%>/admin/memberFinder">
                <input type="hidden" name="searchType" value="member_name"/>
                <input type="text" name="searchKeyword" size="25" placeholder="검색할 이름을 입력하세요." value="<%= "member_name".equals(type) ? kw : ""%>"/>
                <button type="submit">검색</button>            
            </form>    
        </div>
        <div id="search-gender" class="search-type">
            <form action="<%=request.getContextPath()%>/admin/memberFinder">
                <input type="hidden" name="searchType" value="gender"/>
                <input type="radio" name="searchKeyword" value="M" <%= "gender".equals(type) && "M".equals(kw) ? "checked" : "" %>> 남
                <input type="radio" name="searchKeyword" value="F" <%= "gender".equals(type) && "F".equals(kw) ? "checked" : "" %>> 여
                <button type="submit">검색</button>
            </form>
        </div>
    </div>
	
	<table id="tbl-member">
		<thead>
			<tr>
				<th>아이디</th>
				<th>이름</th>
				<th>회원권한</th><%-- select태그로 처리 --%>
				<th>성별</th>
				<th>생년월일</th>
				<th>이메일</th>
				<th>전화번호</th>
				<th>포인트</th><%-- 세자리마다 콤마(,) DecimalFormat --%>
				<th>취미</th>
				<th>가입일</th> <%-- 날짜형식 yyyy-MM-dd --%>
			</tr>
		</thead>
		<tbody>
		<%
			if(memberList == null || memberList.isEmpty()){
		%>
			<tr>
				<td colspan="10" align="center"> 검색 결과가 없습니다. </td>
			</tr>
		<%
			} 
			else {
				for(Member m : memberList){
		%>
			<tr>
				<td><%= m.getMemberId() %></td>
				<td><%= m.getMemberName() %></td>
				<td>
					<select class="member-role" data-member-id="<%= m.getMemberId() %>">
						<option value="A" <%= MemberRole.A == m.getMemberRole() ? "selected" : "" %>>관리자</option>
						<option value="U" <%= MemberRole.U == m.getMemberRole() ? "selected" : "" %>>일반</option>
					</select>	
				</td>
				<td><%= m.getGender() == null ? "" : (m.getGender() == Gender.M ? "남" : "여") %></td>
				<td><%= m.getBirthday() %></td>
				<td><%= m.getEmail()!=null ? m.getEmail() : "" %></td>
				<td><%= m.getPhone() %></td>
				<td><%= new DecimalFormat("#,###").format(m.getPoint()) %></td>
				<td><%= m.getHobby() != null ? m.getHobby() : "" %></td>
				<td><%= new SimpleDateFormat("yyyy-MM-dd").format(m.getEnrollDate()) %></td>		
			</tr>
		<%		} 
			}
		%>
		</tbody>
	</table>
	<div id="pagebar">
		<%= request.getAttribute("pagebar") %>
	</div>
</section>
<form action="<%= request.getContextPath() %>/admin/memberRoleUpdate" method="POST" name="memberRoleUpdateFrm">
	<input type="hidden" name="memberId" />
	<input type="hidden" name="memberRole" />
</form>
<script>
document.querySelectorAll(".member-role").forEach((select, index) => {
	select.onchange = ((e) => {
		console.log(e.target.dataset.memberId, e.target.value);
		
		if(confirm(`해당 회원의 권한을 \${e.target.value}로 변경하시겠습니까?`)){
			const frm = document.memberRoleUpdateFrm;
			frm.memberId.value = e.target.dataset.memberId;
			frm.memberRole.value = e.target.value;
			frm.submit();
		}
		else {
			// 원상복구코드
			e.target.querySelector("[selected]").selected = true;
		}
	});
	
});
</script>


<%@ include file="/WEB-INF/views/common/footer.jsp" %>