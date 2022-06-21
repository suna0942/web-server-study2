package com.kh.mvc.member.model.service;

import static com.kh.mvc.common.JdbcTemplate.*;
import java.sql.Connection;

import com.kh.mvc.member.model.dao.MemberDao;
import com.kh.mvc.member.model.dto.Member;

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
	
}
