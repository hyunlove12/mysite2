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
				WebUtil.redirect(request, response, request.getContextPath() + "/board?a=noUser");
				return;
			} 
			UserVo userVo = (UserVo)session.getAttribute("authUser");
			if(userVo == null) {				
				WebUtil.redirect(request, response, request.getContextPath() + "/board?a=noUser");
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
			WebUtil.redirect(request, response, request.getContextPath() + "/board?a="+a);
		
		} else if("joinsuccess".equals(action)){
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinsuccess.jsp");
		} else if("loginform".equals(action)){
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginform.jsp");
		} else if("updateform".equals(action)){
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
			
		} else if("login".equals(action)){
			String email = request.getParameter("email");
			String password = request.getParameter("password");
		
			UserVo vo = new UserDao().find(email, password);
			if(vo == null) {
				WebUtil.redirect(request, response, request.getContextPath() + "/user?a=loginform&result=fail");
				return;
			}
			
			// 로그인 처리
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", vo);
			
			// main으로 리다이렉트
			WebUtil.redirect(request, response, request.getContextPath());
			
		} else if("logout".equals(action)){
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
			
			// logout 처리
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, request.getContextPath());
		} else {
			List<BoardVo> list = new ArrayList<BoardVo>();
			list = new BoardDao().findAll();
			request.setAttribute("list", list);
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		} 
		
		
		
		//WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
