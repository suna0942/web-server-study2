package com.kh.mvc.member.model.service;

import static com.kh.mvc.common.JdbcTemplate.*;
import java.sql.Connection;
import java.util.List;

import com.kh.mvc.member.model.dao.MemberDao;
import com.kh.mvc.member.model.dto.Member;
import com.kh.mvc.member.model.exception.MemberException;

public class MemberService {
	private MemberDao memberDao = new MemberDao();
	
	/**
	 * DQL요청 - service
	 * 1. Connection 객체 생성
	 * 2. Dao 요청 & Connection 전달
	 * 3. Connection 객체 반환
	 * 
	 * @param memberId
	 * @return
	 */
	public Member findById(String memberId) {
		Connection conn = getConnection();
		Member member = memberDao.findById(conn, memberId);
		close(conn);
		return member;
	}

	
	/**
	 * DML요청 - service
	 * 1. Connection 객체 생성
	 * 2. Dao 요청 & Connection 전달
	 * 3. 트랜잭션처리(정상처리 시 commit, 예외발생 시 rollback)
	 * 4. Connection 객체 반환
	 * 
	 * @param member
	 * @return
	 */
	public int insertMember(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.insertMember(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public int updateMember(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.updateMember(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e; // controller에 예외 던짐.
		} finally {
			close(conn);
		}
		return result;
	}


	public int updatePassword(String memberId, String newPassword) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.updatePassword(conn, memberId, newPassword);
			commit(conn);
		}
		catch(Exception e) {
			rollback(conn);
			throw e;
		}
		finally {
			close(conn);
		}
		return result;
	}


	public List<Member> findAll() {
		Connection conn = getConnection();
		List<Member> list = memberDao.findAll(conn);
		close(conn);
		return list;
	}


	public int deleteMember(String memberId) {
		Connection conn = null;
		int result = 0;
		try{
			conn = getConnection();
			result = memberDao.deleteMember(conn, memberId);
//			update와 delete는 간혹 0이 들어올 수 있음
			if(result == 0)
				throw new MemberException("해당 회원은 존재하지 않습니다.");
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);			
		}
		return result;
	}

	
}
