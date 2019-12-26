package com.bigdata2019.mysite.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PagingUtil {
	private int prevPage = 0;
	private int nextPage = 0;
	private int totalpage = 0;
	private int pageCount = 0;
	private int clickPage = 0;
	private int currentPage = 0;
	private int selectedPage = 0;
	
	/*
	 * 
	 * 필요한 값 계산
	 * */	
	public void paging() {
		//총 카운트 개수
		totalpage = this.find();
		//총 페이징 개수
		pageCount = totalpage / 10 ; 
		
		
	    //LIMIT 0 , 2;
	}
	
	
	
	
	public int find(){		
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();			
			String sql = "SELECT COUNT(*) AS TOTAL_COUNT FROM BOARD";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				result = rs.getInt("TOTAL_COUNT");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 클래스 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("에러:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return result;
	}
	
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		//1. JDBC Driver(Mysql) 로딩
		Class.forName("com.mysql.jdbc.Driver");
		
		//2. 연결하기
		String url = "jdbc:mysql://localhost:3306/webdb";
		Connection conn = DriverManager.getConnection(url, "webdb", "webdb");
		
		return conn;
	}
}
