<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	int totalPage = (int) request.getAttribute("totalPage");
%>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/photo.css" />
<section id="photo-wrapper">
	<h2>사진게시판 </h2>
	<div id="photo-container"></div>
	<hr />
	<div id='btn-more-container'>
		<button id="btn-more" value="" >더보기(<span id="cPage"></span> / <span id="totalPage"><%= totalPage %></span>)</button>
	</div>
</section>
<script>
document.querySelector("#btn-more").addEventListener('click', (e) => {
	const cPage = Number(document.querySelector("#cPage").textContent) + 1;
	getPage(cPage);
});

const getPage = (cPage) => {
	$.ajax({
		url : '<%= request.getContextPath() %>/photo/morePage',
		data : {cPage},
		success(response){
			console.log(response);
			const container = document.querySelector("#photo-container");
			
			response.forEach((photo) => {
				const {no, writer, content, renamedFilename, regDate} = photo;
				console.log(no, writer, content, renamedFilename, regDate);
				
				const html = `
				<div class="polaroid">
					<img src="<%= request.getContextPath() %>/upload/photo/\${renamedFilename}" alt="" />
					<p class="info">
						<span class="writer">\${writer}</span>
						<span class="photoDate">\${regDate}</span>
					</p>
					<p class="caption">\${content}</p>
				</div>
				`;
				container.insertAdjacentHTML('beforeend', html);
				
			});
		},
		error : console.log,
		complete() {
			document.querySelector('#cPage').innerHTML = cPage;
			
			// 마지막페이지
			if(cPage == <%= totalPage%>){
				document.querySelector("#btn-more").disabled = true;
			}
		}
	});
};
getPage(1); 
</script>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
