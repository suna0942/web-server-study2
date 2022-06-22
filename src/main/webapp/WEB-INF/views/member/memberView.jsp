<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "com.kh.mvc.member.model.dto.Gender"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<script>
/* document.memberUpdateFrm.onsubmit = () => {
	document.querySelector("#password").onblur = (e) => {
		if(!/ /.test(password.value)){
			alert("비밀번호는 영문자/숫자/특수문자(!@#$%^&*())로 최소 4글자 이상이어야합니다.");
		}
	};
	
	document.querySelector("#passwordCheck").onblur = (e) => {
		const password = document.querySelector("#password");
		const passwordCheck = e.target;
		if(password.value !== passwordCheck.value){
			alert("비밀번호가 일치하지 않습니다.");
			password.select();
		}
	};
}; */

</script>
<%
	String email = loginMember.getEmail() != null ? loginMember.getEmail() : "";

%>
<section id=enroll-container>
	<h2>회원 정보</h2>
	<form name="memberUpdateFrm" method="post">
		<table>
			<tr>
				<th>아이디<sup>*</sup></th>
				<td>
					<input type="text" name="memberId" id="memberId" value="<%= loginMember.getMemberId() %>" readonly>
				</td>
			</tr>
			<tr>
				<th>패스워드<sup>*</sup></th>
				<td>
					<input type="password" name="password" id="password" value="" required>
				</td>
			</tr>
			<tr>
				<th>패스워드확인<sup>*</sup></th>
				<td>	
					<input type="password" id="passwordCheck" value="" required><br>
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
					<input type="email" placeholder="abc@xyz.com" name="email" id="email" value="<%=email %>"><br>
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
        <input type="button" onclick="deleteMember();" value="탈퇴"/>
	</form>
</section>
<form action="" name="memberDelFrm"></form>
<script>
/**
 * POST / member/memberDelete
 * memberDelFrm 제출
 */
const deleteMember = () => {
	
};

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
%>

</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
