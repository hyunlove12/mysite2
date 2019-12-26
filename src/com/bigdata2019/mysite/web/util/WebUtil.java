package com.bigdata2019.mysite.web.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {
	public static void forward(
			HttpServletRequest request,
			HttpServletResponse response,
			String path) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);		
	}
	
	public static void redirect(
			HttpServletRequest request,
			HttpServletResponse response,			
			String url) throws ServletException, IOException {		
		request.setAttribute("result",request.getAttribute("result"));
		//파라미터 post전달하는 방법
		response.sendRedirect(url);
	}
	
	
	
}
