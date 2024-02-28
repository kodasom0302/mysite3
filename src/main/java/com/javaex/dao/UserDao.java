package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {
	
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs = null;
		private String driver = "com.mysql.cj.jdbc.Driver";
		private String url = "jdbc:mysql://localhost:3306/web_db";
		private String id = "web";
		private String pw = "web";
		
	public void getConnection() {
		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);

		} catch (SQLException e) {
			System.out.println("error:" + e);

		}
	}
	
	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	public int insertUser(UserVo userVo) {
		int count=-1;

		this.getConnection();
		
		try {
			String query="";
			query+=" insert into users ";
			query+=" values(null, ?, ?, ?, ?) ";
			
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPw());
			pstmt.setString(3, userVo.getName());
			pstmt.setString(4, userVo.getGender());
			
			count=pstmt.executeUpdate();
			
			System.out.println(count+"건 등록 되었습니다");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		this.close();
		
		return count;
		
	}//insertUser
	
	public UserVo selectUserByIdPw(UserVo userVo) {
		UserVo authUser=null;
		
		this.getConnection();
		
		try {
			String query="";
			query+=" select no, ";
			query+=" 		name ";
			query+=" from users ";
			query+=" where id=? ";
			query+=" and password=? ";
			
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPw());
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				int no=rs.getInt("no");
				String name=rs.getString("name");
				
				authUser=new UserVo();
				authUser.setNo(no);
				authUser.setName(name);
				
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		this.close();
		
		return authUser;
	}//selectUserByIdPw
	
	public int modifyUser(int no, String id, String pw, String name, String gender) {
		int count = -1;
		
		this.getConnection();
		
		try {
			String query = "";
			query += " update users ";
			query += " set password =?, ";
			query += " name =?, ";
			query += " gender =? ";
			query += " where no =? ";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, pw);
			pstmt.setString(2, name);
			pstmt.setString(3, gender);
			pstmt.setInt(4, no);
			
			count = pstmt.executeUpdate();

			System.out.println(count + "건 수정 되었습니다.");
			
		}  catch (SQLException e) {
			System.out.println("error:" + e);
		} 
		
		this.close();
		
		return count;
		
	}//modifyUser

}
