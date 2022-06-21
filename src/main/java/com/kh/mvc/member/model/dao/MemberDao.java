package com.kh.mvc.member.model.dao;

import static com.kh.mvc.common.JdbcTemplate.close;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.kh.mvc.member.model.dto.Gender;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.dto.MemberRole;
import com.kh.mvc.member.model.exception.MemberException;

public class MemberDao {
	private Properties prop = new Properties();
	
	public MemberDao() {
		String filename = MemberDao.class.getResource("/sql/member/member-query.properties").getPath();
		System.out.println("filename@MemberDao = " + filename);
		try {
			prop.load(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * DQL요청 - dao
	 * 1. PreparedStatement 객체 생성(sql 전달) & 값대입
	 * 2. 쿼리실행 executeQuery:ResultSet 반환
	 * 3. ResultSet 처리 - dto 객체 변환
	 * 4. ResultSet, PreparedStatement 객체 반환
	 * 
	 * @param conn
	 * @param memberId
	 * @return
	 */
	public Member findById(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		String sql = prop.getProperty("findById");
		// select * from member where member_id = ?
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				memberId = rset.getString("member_id");
				String password = rset.getString("password");
				String memberName = rset.getString("member_name");
				MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));
				Gender gender = Gender.valueOf(rset.getString("gender")); // enum은 valueOf로 감싸면 알아서 변환해줌
				Date birthday = rset.getDate("birthday");
				String email = rset.getString("email");
				String phone = rset.getString("phone");
				String hobby = rset.getString("hobby");
				int point = rset.getInt("point");
				Timestamp enrollDate = rset.getTimestamp("enroll_date");
				member = new Member(memberId, password, memberName, memberRole, gender,
									birthday, email, phone, hobby, point, enrollDate);
			}
			
		} catch (SQLException e) {
			throw new MemberException("회원 아이디 조회 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return member;
	}
	
	/**
	 * DML요청 - dao
	 * 1. PreparedStatement 객체 생성(sql 전달) & 값대입
	 * 2. 쿼리실행 executeUpdate:int 반환(몇행이 처리됐는지, insert는 무조건 1)
	 * 3. PreparedStatement 객체 반환
	 * 
	 * @param conn
	 * @param member
	 * @return
	 * @throws SQLException 
	 */
	public int insertMember(Connection conn, Member member){
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertMember");
		// insertMember = insert into member values (?, ?, ?, default, ?, ?, ?, ?, ?, default, default)
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getGender() != null ? member.getGender().name() : null); // enum의 값을 꺼내는 것(name, toString) 
			pstmt.setDate(5, member.getBirthday());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getPhone());
			pstmt.setString(8, member.getHobby());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// service 예외 던짐(unchecked, 비지니스를 설명가능한 구체적 커스텀예외 전환)
			throw new MemberException("회원가입 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}
}
