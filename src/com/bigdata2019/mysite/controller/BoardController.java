package com.bigdata2019.mysite.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bigdata2019.mysite.repository.BoardDao;
import com.bigdata2019.mysite.repository.UserDao;
import com.bigdata2019.mysite.vo.BoardVo;
import com.bigdata2019.mysite.vo.UserVo;
import com.bigdata2019.mysite.web.util.PagingUtil;
import com.bigdata2019.mysite.web.util.WebUtil;

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("a");
		//글쓰기 화면
		if("writeform".equals(action)) {
			WebUtil.forward(request, response, "/WEB-INF/views/board/write.jsp");
		} 
		//게시판 글 등록
		else if("write".equals(action)){
			HttpSession session = request.getSession();						
			if(session == null) {
				request.setAttribute("result", "noUser");
				WebUtil.redirect(request, response, request.getContextPath() + "/board");
				return;
			} 
			UserVo userVo = (UserVo)session.getAttribute("authUser");
			if(userVo == null) {
				request.setAttribute("result", "noUser");
				
				WebUtil.redirect(request, response, request.getContextPath() + "/board");
				return;
			}	
			Long userNo = userVo.getNo();
			String title = request.getParameter("title");
			String content = request.getParameter("content");			
			
			BoardVo vo = new BoardVo();
			vo.setUserNo(userNo);
			vo.setTitle(title);
			vo.setContents(content);
			
			Boolean boo = new BoardDao().insert(vo);
			String a = "";
			if(boo) {
				a = "suc";
			} else {
				a = "fail";
			}
			request.setAttribute("result", a);
			WebUtil.redirect(request, response, request.getContextPath() + "/board");
		
		}  else if("updateform".equals(action)){
			/* 접근 제어(ACL) */
			HttpSession session = request.getSession();
			if(session == null) {
				WebUtil.redirect(request, response, request.getContextPath());
				return;
			}
			
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				WebUtil.redirect(request, response, request.getContextPath());
				return;
			}
			
			Long no = authUser.getNo();
			UserVo userVo = new UserDao().find(no);
			
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/updateform.jsp");
			
		}  else {
			//getAttribute vs getParameter
			List<BoardVo> list = new ArrayList<BoardVo>();
			//페이징 처리 
			String paging = "";	//slq의 LIMIT옵션
			String tableName = "BOARD";	//테이블 명
			PagingUtil pg = new PagingUtil();
			int totalPage = pg.totalCount(tableName);	//토탈 페이지
			int startPage = 0;	//쿼리가 시작되는 row 번호
			int lastPage = 0;	//10개 단위의 페이징 그룹 개수
			int selectedPage = 1;	//선택된 페이지
			int currentPage = 1;	//현재 페이지 그룹(5개 기준으로 나눔)
			int prevPage = 0;	//전 페이지(5, 10, 15, .... )
			int nextPage = 6;	//넥스트 페이지(6, 11, 16, 21, ...)
			if(request.getParameter("selectedPage") == null || ("").equals(request.getParameter("selectedPage"))) {
				paging = "LIMIT 0, 10";
			}else {	
				selectedPage = Integer.parseInt(request.getParameter("selectedPage"));
				startPage = selectedPage * 10 - 10;						
				paging = "LIMIT " + startPage + ", 10";
				currentPage = selectedPage / 5 + 1; //선택된 페이지 그룹
				if(selectedPage % 5 == 0) {
					currentPage--; 
				}
				prevPage = (currentPage - 1) * 5;				
			}
			lastPage = totalPage / 10 + 1;
			if(lastPage % 10 == 0) {
				lastPage--; 
			}
			
			list = new BoardDao().findAll(paging);
			
			request.setAttribute("prevPage", prevPage);
			request.setAttribute("nextPage", nextPage);			
			request.setAttribute("list", list);
			request.setAttribute("lastPage", lastPage);
			String result = "";
			if(request.getAttribute("result") == null ) {
			} else {				
				result = request.getAttribute("result").toString();
			}
			request.setAttribute("result", result);
			request.setAttribute("currentPage", currentPage);
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp?selectedPage=" + selectedPage);
		} 
		
		
		
		//WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
