<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.kh.mvc.board.model.dto.CommentLevel"%>
<%@page import="com.kh.mvc.board.model.dto.BoardComment"%>
<%@page import="com.kh.mvc.board.model.dto.Attachment"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.mvc.board.model.dto.BoardExt"%>
<%@page import="com.kh.mvc.board.model.dto.Board"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/board.css" />
<%
	Board board = (Board) request.getAttribute("board");
	List<Attachment> attachments = ((BoardExt) board).getAttachments();
	List<BoardComment> commentList = (List<BoardComment>) request.getAttribute("commentList");
%>
<section id="board-container">
	<h2>게시판</h2>
	<table id="tbl-board-view">
		<tr>
			<th>글번호</th>
			<td><%= board.getNo()%></td>
		</tr>
		<tr>
			<th>제 목</th>
			<td><%= board.getTitle()%></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><%= board.getWriter()%></td>
		</tr>
		<tr>
			<th>조회수</th>
			<td><%= board.getReadCount()%></td>
		</tr>
		<% 
			if(attachments != null && !attachments.isEmpty()) { 
				for(Attachment attach : attachments){
		%>
		<tr>
			<th>첨부파일</th>
			<td>
				<%-- 첨부파일이 있을경우만, 이미지와 함께 original파일명 표시 --%>
				<img alt="첨부파일" src="<%=request.getContextPath() %>/images/file.png" width=16px>
				<a href="<%= request.getContextPath()%>/board/fileDownload?no=<%= attach.getNo()%>"><%= attach.getOriginalFilename() %></a>
			</td>
		</tr>
		<%
				}
			} 
		%>
		<tr>
			<th>내 용</th>
			<td><%= board.getContent()%></td>
		</tr>
		<%
			boolean canEdit = loginMember != null &&
						(loginMember.getMemberId().equals(board.getWriter()) || loginMember.getMemberRole() == MemberRole.A);
			if(canEdit){
		%>
		<tr>
			<%-- 작성자와 관리자만 마지막행 수정/삭제버튼이 보일수 있게 할 것 --%>
			<th colspan="2">
				<input type="button" value="수정하기" onclick="updateBoard()">
				<input type="button" value="삭제하기" onclick="deleteBoard()">
			</th>
		</tr>
	<%
		}
	%>
	</table>
	
	
	<hr style="margin-top:30px;" />    
    
    <div class="comment-container">
    	<!-- 댓글작성부 -->
        <div class="comment-editor">
            <form
            	name="boardCommentFrm"
            	action="<%=request.getContextPath()%>/board/boardCommentEnroll" method="post">
                <input type="hidden" name="boardNo" value="<%= board.getNo() %>" />
                <input type="hidden" name="writer" value="<%= loginMember != null ? loginMember.getMemberId() : "" %>" />
                <input type="hidden" name="commentLevel" value="1" />
                <input type="hidden" name="commentRef" value="0" />    
                <textarea name="content" cols="60" rows="3"></textarea>
                <button type="submit" id="btn-comment-enroll1">등록</button>
            </form>
        </div>
        <!--table#tbl-comment>tr>td*2-->
        
        <table id="tbl-comment">
        <%
        	if(commentList != null && !commentList.isEmpty()){
        		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        		for(BoardComment bc : commentList){
        			
        		
        %>
        	<tr class="<%= bc.getCommentLevel() == CommentLevel.COMMENT ? "level1" : "level2" %>">
        		<td>
        			<sub class="comment-writer"><%= bc.getWriter() %></sub>
        			<sub class="comment-date"><%= sdf.format(bc.getRegDate()) %></sub>
        			<div>
        				<%= bc.getContent() %>
        			</div>
        		</td>
        		<td>
        			<% if(bc.getCommentLevel() == CommentLevel.COMMENT) { %>
        			<button class="btn-reply" value="<%= bc.getNo()%>">답글</button>
        			<% } %>
        		</td>
        	</tr>
        <%
        		}
        	}
        %>
        </table>
    </div>
    
    
    
</section>
<script>
	document.boardCommentFrm.content.addEventListener('focus', (e) => {
		if(<%= loginMember == null %>)
			loginAlert();	
	});
	
	document.boardCommentFrm.addEventListener('submit', (e) => {
		if(<%= loginMember == null %>){
			loginAlert();
			e.preventDefault();
			return;
		}
		
		if(!/^(.|\n)+$.test(e.target.content.value)/){
			alert("내용을 작성해주세요");
			e.preventDefault();
			return;
		}
	});
	const loginAlert = () => {
		alert("로그인 후 이용할 수 있습니다.");
		document.querySelector("#memberId").focus();
	};
</script>


<% if(canEdit){ %>
<form
	action="<%= request.getContextPath() %>/board/boardDelete" method="POST" name="boardDelFrm">
	<input type="hidden" name="no" value="<%= board.getNo() %>" />
</form>
<script>
const updateBoard = () => {
	location.href = "<%= request.getContextPath()%>/board/boardUpdate?no=<%= board.getNo() %>";
};
const deleteBoard = () => {
	if(confirm("정말 게시글을 삭제하시겠습니까?"))
		document.boardDelFrm.submit();
};
	
</script>
<% } %>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
