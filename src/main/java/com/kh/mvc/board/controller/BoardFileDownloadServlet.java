package com.kh.mvc.board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.mvc.board.model.dto.Attachment;
import com.kh.mvc.board.model.service.BoardService;

/**
 * Servlet implementation class BoardFileDownloadServlet
 */
@WebServlet("/board/fileDownload")
public class BoardFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자입력값 처리
			int no = Integer.parseInt(request.getParameter("no"));
			System.out.println("no = " + no);
			
			// 2. 업무로직
			// a. attach 조회
			Attachment attach = boardService.findAttachmentByNo(no);
			System.out.println("attach = " + attach);
			
			
			// 3. 응답처리 - 파일 입출력 처리
			// 입력 - saveDirectory + renamedFilename
			// 출력 - http응답메세지 출력스트림 (byte기반)response.getOutputStream()
			// 응답 헤더 contentType application/octet-stream  --> 입/출력 작성 전에 작성
			response.setContentType("application/octet-stream");
			String filename = URLEncoder.encode(attach.getOriginalFilename(), "utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename); // 인코딩 처리를 하지 않으면 한글이 깨진다
			
			String saveDirectory = getServletContext().getRealPath("/upload/board");
			File downFile = new File(saveDirectory, attach.getRenamedFilename());
			try (
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downFile));
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream()))
			{
				byte[] buffer = new byte[8192]; // 8kb
				int len = 0; // 읽어온 byte수
				while((len = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, len); // buffer에서 0 ~ len미만까지 출력
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
