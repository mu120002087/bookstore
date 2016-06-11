package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloServlet
 */

public class LoginServlet extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("userName");
		String password = request.getParameter("userPassword");
		PrintWriter pw = response.getWriter();
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection cnct = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/pdb01", "scott", "tiger");
			PreparedStatement stmt = cnct.prepareStatement("select * from users where username=? and password=?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			System.out.println(username + " " + password);
			
			if (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2));
				pw.write(String.format("<html><h1>Welcome %s, you're logged in!</h1></html>", username));
			}
			else {
				pw.write("<html><h1>Sorry, the username or password is invalid. Please <a href=http://localhost:8080/WebProject/>log in</a> again or <a href=http://localhost:8080/WebProject/register.jsp>register</a></h1></html>");
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
