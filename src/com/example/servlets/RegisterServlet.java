package com.example.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
public class RegisterServlet extends HttpServlet {
	PrintWriter pw;

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("userName");
		String password = request.getParameter("userPassword");
		pw = response.getWriter();
		register(username, password);
	}

	private void register(String username, String password) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // The ojdbc7.jar should be put in WEB-INF/lib folder and then Build path -> Add Web App Libraries
			Connection cnct = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/pdb01", "scott", "tiger");
			PreparedStatement stmt = cnct.prepareStatement("select * from users where username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				pw.write(String.format("<html><h1>Sorry, the username has been used! Please <a href=http://localhost:8080/WebProject/register.jsp>try again</a></h1></html>"));
			} else {
				stmt = cnct.prepareStatement("insert into users values(?, ?)");
				stmt.setString(1, username);
				stmt.setString(2, password);
				stmt.executeUpdate();
				pw.write(String.format("<html><h1>Registered successfully! Please <a href=http://localhost:8080/WebProject/>log in</a></h1></html>"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
