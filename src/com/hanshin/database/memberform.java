package com.hanshin.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet("/Project2")
public class memberform extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset = UTF-8");
		PrintWriter out = resp.getWriter();
		
		int count = 0;
		
		String mode = req.getParameter("submit");
		
		String id = req.getParameter("id");
		String pwd = req.getParameter("pwd");
		String name = req.getParameter("name");
		String tel = req.getParameter("tel");
		String email = req.getParameter("email");
		String dept = req.getParameter("dept");
		String gender = req.getParameter("gender");
		String birth = req.getParameter("birth");
		String introduction = req.getParameter("introduction");
		
		String id_check = "";
		String pwd_check = "";
		String name_check = "";
		
		String jdbc_driver = "com.mysql.cj.jdbc.Driver";
		String jdbc_url = "jdbc:mysql://127.0.0.1:3306/dbproject2?serverTimezone=UTC";
		
		try {
			Class.forName(jdbc_driver).newInstance();
			Connection con = DriverManager.getConnection(jdbc_url, "root", "112400ss");
			Statement st = con.createStatement();
			
			String sql = "SELECT * FROM dbproject2.member";
			ResultSet rs = st.executeQuery(sql);
			PreparedStatement pst = con.prepareStatement(sql);
			
			if(mode.equals("전송")) {
				while(rs.next()) {	
					id_check = rs.getString("id");
					pwd_check = rs.getString("pwd");
					name_check = rs.getString("name");
					
					if (id.equals(id_check) && name.equals(name_check)) {
						if(!pwd.equals(pwd_check)) {
							//아이디와 이름은 일치하지만 비밀번호가 일치하지 않을 때 입니다.
							count = -1;
							JOptionPane.showMessageDialog(null, "인증 실패. 다시 시도하세요.");
							resp.sendRedirect("member.html");
						}
						if(pwd.equals(pwd_check)) {
							//아이디와 이름은 일치하고, 비밀번호도 일치할 때 입니다.
							count = 1;
							sql = "UPDATE dbproject2.member SET tel = ?, email = ?, dept = ?, gender = ?, birth = ?, introduction = ? WHERE (id = ?)";
							pst = con.prepareStatement(sql);
							System.out.println("1");
							
							pst.setString(1, tel);
							pst.setString(2, email);
							pst.setString(3, dept);
							pst.setString(4, gender);
							pst.setString(5, birth);
							pst.setString(6, introduction);
							pst.setString(7, id);
							pst.executeUpdate();
							
							break;
						}
					}
				}
			
				if(count == 0) {
					System.out.println("입력");
					sql = "INSERT INTO dbproject2.member VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					pst = con.prepareStatement(sql);
					pst.setString(1, id);
					pst.setString(2, pwd);
					pst.setString(3, name);
					pst.setString(4, tel);
					pst.setString(5, email);
					pst.setString(6, dept);
					pst.setString(7, gender);
					pst.setString(8, birth);
					pst.setString(9, introduction);
					pst.executeUpdate();
				}

				out.print("<html><head><title>전송 결과</title></head>");
				out.print("<table bgcolor=\"#000000\" cellpadding=\"5\" cellspacing=\"1\" border=\"0\" align=\"left\" style=\"text-align:left;\">");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">아이디</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("id")+"</td>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">이름</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("name")+"</td></tr>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">전화번호</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("tel")+"</td></tr>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">이메일</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("email")+"</td></tr>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">학과</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("dept")+"</td></tr>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">성별</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("gender")+"</td></tr>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">태어난 달</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("birth")+"</td></tr>");
				out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">자기소개</td><td width=\"*\" bgcolor=\"#ffffff\">"+req.getParameter("introduction")+"</td></tr>");
				out.print("<tr><td bgcolor=\"#cacaca\"><input type=\"button\" value=\"BACK\" onClick=\"history.go(-1)\"</td></table>");
				out.print("</html>");
			}
			
			if(mode.equals("DB보기")) {
				//submit의 values가 DB보기이면 모든 정보를 조회합니다.
				count = 2;
				while(rs.next()) {
					out.print("<html><head><title>DB보기 결과</title></head>");
					out.print("<table bgcolor=\"#000000\" cellpadding=\"5\" cellspacing=\"1\" border=\"0\" align=\"left\" style=\"text-align:left;\">");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">아이디</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("id")+"</td>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">이름</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("name")+"</td></tr>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">전화번호</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("tel")+"</td></tr>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">이메일</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("email")+"</td></tr>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">학과</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("dept")+"</td></tr>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">성별</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("gender")+"</td></tr>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">태어난 달</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("birth")+"</td></tr>");
					out.print("<tr><td width=\"20%\" bgcolor=\"#cacaca\">자기소개</td><td width=\"*\" bgcolor=\"#ffffff\">"+rs.getString("introduction")+"</td></tr>");
					out.print("<tr><td bgcolor=\"#cacaca\"><input type=\"button\" value=\"BACK\" onClick=\"history.go(-1)\"</td></table>");
					out.print("</html>");
				}
			}
				
			if(mode.equals("DB삭제")) {	
				//submit의 values가 DB삭제이면 모든 정보를 삭제하고 초기 화면으로 돌아갑니다.
				count = 3;
				rs = st.executeQuery(sql);
				
				String id_is = "";
				
				while(rs.next()) {
					sql = "SELECT * FROM dbproject2.member";
					id_is = rs.getString("id");
					System.out.println(id_is);
					sql = "DELETE FROM dbproject2.member WHERE (id = ?)";
					pst = con.prepareStatement(sql);
					pst.setString(1, id_is);
					pst.executeUpdate();
				}
				
				resp.sendRedirect("member.html");
			}
			
			pst.close();
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(count == 0) JOptionPane.showMessageDialog(null, "멤버 생성에 성공하였습니다");
		if(count == 1) JOptionPane.showMessageDialog(null, "멤버 업데이트에 성공하였습니다");
		if(count == 2) JOptionPane.showMessageDialog(null, "멤버 전체 조회에 성공하였습니다");
		if(count == 3) JOptionPane.showMessageDialog(null, "멤버 전체 삭제에 성공하였습니다");

		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
}
