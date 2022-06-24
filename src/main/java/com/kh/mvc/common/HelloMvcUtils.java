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

}
