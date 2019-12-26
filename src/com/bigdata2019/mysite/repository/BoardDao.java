package com.bigdata2019.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bigdata2019.mysite.vo.BoardVo;
import com.bigdata2019.mysite.vo.UserVo;

public class BoardDao {
	
	/*
	 * 게시판 리스트 출력
	 * 
	 * */	
	public List<BoardVo> findAll(String paging){
		List<BoardVo> result = new ArrayList<BoardVo>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
					" SELECT B.NO " +
					", B.TITLE " +
				 	", B.CONTENT " +
					", B.REG_DATE "+
					", B.HIT " +
					", B.G_NO " +
					", B.O_NO " +
					", B.DEPTH " +
					", B.NO2 AS USER_NO " +
					", U.GENDER " +
					", U.NAME " +
					", U.EMAIL " +
				    " FROM BOARD B" +
				    " LEFT OUTER JOIN USER U " +
				    " ON B.NO2 = U.NO " +
				    " ORDER BY B.G_NO, B.DEPTH, B.O_NO ";
			
			if(("").equals(paging) || paging != null) {
				sql += paging; 
			}
			
			pstmt = conn.prepareStatement(sql);
			//이상 미만
			//pstmt.setInt(1, 0);
			//pstmt.setInt(2, 2);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getLong(1));
				vo.setTitle(rs.getString(2));
				vo.setContents(rs.getString(3));
				vo.setRegDate(rs.getString(4));
				vo.setHit(rs.getInt(5));
				vo.setGroupNo(rs.getInt(6));
				vo.setOrderNo(7);
				vo.setDepth(rs.getInt(8));
				vo.setName(rs.getString(9));
				vo.setGender(rs.getString(10));
				vo.setName(rs.getString(11));
				vo.setEmail(rs.getString(12));
				result.add(vo);
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
	
	public UserVo find(Long no){
		UserVo result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				"select no, name, email, gender" + 
				"  from user" + 
				" where no=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = new UserVo();
				result.setNo(rs.getLong(1));
				result.setName(rs.getString(2));
				result.setEmail(rs.getString(3));
				result.setGender(rs.getString(4));
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
	
	public UserVo find(String email, String password){
		UserVo result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = 
				"select no, name" + 
				"  from user" + 
				" where email=?" + 
				"   and password=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = new UserVo();
				result.setNo(rs.getLong(1));
				result.setName(rs.getString(2));
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
	
	public Boolean insert(BoardVo vo) {
		Boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
	
			//SQL 준비
			String sql = "INSERT INTO BOARD(TITLE, CONTENT, REG_DATE, HIT, G_NO, O_NO, DEPTH, NO2)" +
						 "VALUES( ?, ?, NOW(), 0, (SELECT MAX(G_NO) + 1 FROM BOARD AS G_NO ) , 1, 1, ? )";
			pstmt = conn.prepareStatement(sql);			
		
			//값 바인딩
			pstmt.setString(1, vo.getTitle());	//제목
			pstmt.setString(2, vo.getContents());	//내용
			pstmt.setLong(3, vo.getUserNo());	//사용자
			
			
			//쿼리 실행
			int count = pstmt.executeUpdate();
			result = (count == 1);
			
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
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
