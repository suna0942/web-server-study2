package com.kh.mvc.board.model.dto;

/**
 * 댓글 COMMENT
 * 답글 REPLY
 *
 */
public enum CommentLevel {
	// enum은 숫자가 안됨 1, 2 ...
	// COMMENT, REPLY는 name값임
	COMMENT(1), REPLY(2);
	
	private int value;
	
	// enum의 생성자
	// enum은 내부에서만 생성자메소드를 호출할 수 있음
	CommentLevel(int value){
		this.value = value;
	}
	
	// enum -> value 호출
	public int getValue() {
		return this.value;
	}
	
	// value -> enum 호출
	public static CommentLevel valueOf(int value) {
		switch(value) {
		case 1 : return COMMENT;
		case 2 : return REPLY;
		default : throw new AssertionError("Unknown CommentLevel : " + value);
		}
	}
	
}
