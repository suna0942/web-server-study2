package com.kh.mvc.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public class HelloMvcUtils {
	
	/**
	 * 단방향(hashing)
	 * 1. 암호화
	 * 2. 인코딩 처리
	 * 
	 * @param rawPassword
	 * @return
	 */
	public static String getEncryptedPassword(String rawPassword, String salt) {
		String encryptedPassword = null;
		
		try {
			// 1. 암호화
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] input = rawPassword.getBytes("utf-8");
			byte[] saltBytes = salt.getBytes("utf-8");
			md.update(saltBytes);
			byte[] encryptedBytes = md.digest(input);
//			System.out.println(new String(encryptedBytes));
			
			// 2. 인코딩처리 : 영문자 숫자 + / (= 패딩문자)
			Encoder encoder = Base64.getEncoder();
			encryptedPassword = encoder.encodeToString(encryptedBytes);
			
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return encryptedPassword;
	}
	
	/**
	 * pagebar 준비물
	 * 
	 * @param cPage
	 * @param numPerPage
	 * @param totalContent
	 * @param url
	 * @return
	 * 
	 * totalPage 전체페이지 수
	 * pagebarSize 한페이지에 표시할 페이지 번호 개수(1 2 3 4 5 6 7 8 9 10 >>>)
	 * pagebarStart ~ pagebarEnd
	 * pageNo 증감변수
	 * 
	 * 1. 이전영역
	 * 2. pageNo영역
	 * 3. 다음영역
	 * 
	 */
	public static String getPagebar(int cPage, int numPerPage, int totalContent, String url) {
		StringBuilder pagebar = new StringBuilder();
		url += (url.indexOf("?") < 0) ? "?cPage=" : "&cPage="; // /mvc/admin/memberList?cPage=?? 이렇게 작성예정
		int totalPage = (int) Math.ceil((double) totalContent / numPerPage);
		int pagebarSize = 5; // 1 2 3 4 5 다음, 이전 6 7 8 9 10 다음, 이전 11 12 => 3가지 타입으로 보여주기
		// pagebarStart는 1, 6, 11이고 pagebarEnd는 5, 10, 15가 됨
		int pagebarStart = (pagebarSize * ((cPage - 1)/pagebarSize)) + 1;
		int pagebarEnd = pagebarStart + pagebarSize - 1;
		int pageNo = pagebarStart;
		
		// 이전영역
		if(pageNo == 1) {
			
		}
		else {
			pagebar.append("<a href='" + url + (pageNo - 1) + "'>이전</a>\n");
		}
		
		// pageNo영역
		while(pageNo <= pagebarEnd && pageNo <= totalPage) { // totalPage는 12p인데 13p는 12를 넘어버리기때문에 false로 탈출하게됨
			// 현재 페이지
			if(pageNo == cPage) {
				pagebar.append("<span class='cPage'>" + pageNo + "</span>\n");
			}
			// 현재 페이지가 아닌 경우
			else {
				pagebar.append("<a href='" + url + pageNo + "'>" + pageNo + "</a>\n");
			}
			pageNo++;
		}
		
		// 다음영역
		if(pageNo > totalPage) {
			
		}
		else {
			pagebar.append("<a href='" + url + pageNo + "'>다음</a>\n");
		}
		
		return pagebar.toString();
	}

}
