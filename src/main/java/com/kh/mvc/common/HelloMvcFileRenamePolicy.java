package com.kh.mvc.common;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * 
 * abc.txt => 20220628_183030333_123.txt
 * 
 *
 */
public class HelloMvcFileRenamePolicy implements FileRenamePolicy {

	@Override
	public File rename(File oldFile) {
		File newFile = null;
		
		do {
			// 새 파일명 형식 지정
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			DecimalFormat df = new DecimalFormat("000"); // 30 -> 030으로 자리수 맞춰줌
			
			// 확장자 추출
			String oldName = oldFile.getName();
			String ext = ""; // 확장자
			int dot = oldName.lastIndexOf(".");
			if(dot > -1)
				ext = oldName.substring(dot); // .txt
			
			// 새 파일명 생성
			String newName = sdf.format(new Date()) + df.format(Math.random() * 1000) + ext;
			
			// File객체 새로 생성
			newFile = new File(oldFile.getParent(), newName);
			
		} while(!createNewFile(newFile));
		
		return newFile;
	}
	
	/**
	 * {@link File#createNewFile()}
	 * - 실제파일이 존재하지 않는 경우, 파일생성 후 true 리턴
	 * - 실제파일이 존재하는 경우, IOException 발생!
	 * 
	 * @param f
	 * @return
	 */
	private boolean createNewFile(File f) {
		try {
			return f.createNewFile();
		}
		catch (IOException ignored) {
			return false;
		}
	}
}
