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
			String paging = "";
			int totalPage = 19;
			int startPage = 0;
			int lastPage = 0;
			String selectedPage = "1";
			int currentPage = 1;
			if(request.getParameter("selectedPage") == null || ("").equals(request.getParameter("selectedPage"))) {
				paging = "LIMIT 0, 10";
			}else {	
				selectedPage = request.getParameter("selectedPage");
				lastPage = Integer.parseInt(selectedPage) * 10;
				startPage = lastPage - 10;						
				paging = "LIMIT " + startPage + ", 10";
				currentPage = Integer.parseInt(selectedPage) / 5 + 1;
				
				
			}
			
			
			
			list = new BoardDao().findAll(paging);
			
			
			
			request.setAttribute("list", list);
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
