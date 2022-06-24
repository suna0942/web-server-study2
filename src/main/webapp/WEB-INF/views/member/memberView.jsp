<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "com.kh.mvc.member.model.dto.Gender"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%
	String email = loginMember.getEmail() != null ? loginMember.getEmail() : "";

%>
<section id=enroll-container>
	<h2>회원 정보</h2>
	<form 
		name="memberUpdateFrm"
		action="<%=request.getContextPath() %>/member/memberUpdate" 
		method="post">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" name="memberId" id="memberId" value="<%= loginMember.getMemberId() %>" readonly>
				</td>
			</tr>
			<tr>
				<th>이름<sup>*</sup></th>
				<td>	
				<input type="text"  name="memberName" id="memberName" value="<%= loginMember.getMemberName() %>"  required><br>
				</td>
			</tr>
			<tr>
				<th>생년월일</th>
				<td>	
				<input type="date" name="birthday" id="birthday" value="<%= loginMember.getBirthday() %>"><br>
				</td>
			</tr> 
			<tr>
				<th>이메일</th>
				<td>	
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="<%= email %>"><br>
				</td>
			</tr>
			<tr>
				<th>휴대폰<sup>*</sup></th>
				<td>	
					<input type="tel" placeholder="(-없이)01012345678" name="phone" id="phone" maxlength="11" value="<%= loginMember.getPhone() %>" required><br>
				</td>
			</tr>
			<tr>
				<th>포인트</th>
				<td>	
					<input type="text" placeholder="" name="point" id="point" value="<%= loginMember.getPoint() %>" readonly><br>
				</td>
			</tr>
			<tr>
				<th>성별 </th>
				<td>
			       		 <input type="radio" name="gender" id="gender0" value="M">
						 <label for="gender0">남</label>
						 <input type="radio" name="gender" id="gender1" value="F">
						 <label for="gender1">여</label>
				</td>
			</tr>
			<tr>
				<th>취미 </th>
				<td>
					<input type="checkbox" name="hobby" id="hobby0" value="운동" ><label for="hobby0">운동</label>
					<input type="checkbox" name="hobby" id="hobby1" value="등산" ><label for="hobby1">등산</label>
					<input type="checkbox" name="hobby" id="hobby2" value="독서" ><label for="hobby2">독서</label><br />
					<input type="checkbox" name="hobby" id="hobby3" value="게임" ><label for="hobby3">게임</label>
					<input type="checkbox" name="hobby" id="hobby4" value="여행" ><label for="hobby4">여행</label><br />


				</td>
			</tr>
		</table>
        <input type="submit" value="정보수정"/>
        <input type="button" value="비밀번호 변경" onclick="updatePassword();"/>
        <input type="button" onclick="deleteMember();" value="탈퇴"/>
	</form>
</section>
<!-- 회원탈퇴폼 : POST /member/memberDelete 전송을 위해 시각화되지 않는 폼태그 이용 -->
<form name="memberDelFrm" action="<%= request.getContextPath() %>/member/memberDelete" method="POST">
	<input type="hidden" name="memberId" value="<%= loginMember.getMemberId() %>" />
</form>
<script>
const updatePassword = () => {
	location.href = "<%= request.getContextPath() %>/member/passwordUpdate";
};
/**
 * POST / member/memberDelete
 * memberDelFrm 제출
 */
 
const deleteMember = () => {
	if(confirm("정말로 탈퇴하시겠습니까?"))
		document.memberDelFrm.submit();
};

/**
 * 폼 유효성 검사
 */
document.memberUpdateFrm.onsubmit = (e) => {
	const memberName = document.querySelector("#memberName");
	if(!/^[가-힣]{2,}$/.test(memberName.value)){
		alert("한글 2글자이상 입력해주세요");
		memberName.select();
		return false;
	}
	
	const phone = document.querySelector("#phone");
	if(!/^010[0-9]{8}$/.test(phone.value)){
		alert("유효한 전화번호를 입력해주세요");
		phone.select();
		return false;
	}
}

const genderCheck = document.querySelectorAll("[name=gender]");
<%
	Gender gender = loginMember.getGender();
	if(gender.M.equals(gender)){
%>
		genderCheck[0].checked = true;
<%
	} else if(gender.F.equals(gender)) {
%>
		genderCheck[1].checked = true;
<%
	}
%>

const hobbyCheck = document.querySelectorAll("[name=hobby]");

<%
	if(loginMember.getHobby() != null){
		String hobby = loginMember.getHobby();
		String[] hobbies = hobby.split(",");
		if(hobbies != null){
			for(int i = 0; i < hobbies.length; i++){ %>
				for(let j = 0; j < hobbyCheck.length; j++){
					if(hobbyCheck[j].value == '<%= hobbies[i]%>'){
						hobbyCheck[j].checked = true;
					}
				}
	<%
			}
		}
	}
%>

</script>

<%--
강사님 작성 코드
<%!
/**
* compile시 메소드로 선언처리됨.
* 선언위치는 어디든 상관없다.
*/ 
public String hobbyChecked(List<String> hobbyList, String hobby){
	return hobbyList != null && hobbyList.contains(hobby) ? "checked" : "";
}

%>
--%>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
