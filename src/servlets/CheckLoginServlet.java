package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.jdbc.PreparedStatement;

import classes.UserAccounts;
import utils.DatabaseConnection;

/**
 * Servlet implementation class CheckLoginServlet
 */
@WebServlet("/CheckLoginServlet")
public class CheckLoginServlet extends HttpServlet {
	
	int dbOwnerId;
	
	private static final long serialVersionUID = 1L;

    public CheckLoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//set session object so we can get it for home page dash board
		
    	UserAccounts mobileUser =  null;
    	HttpSession session = request.getSession(false);
    	
		String username = request.getParameter("username").toLowerCase();
        String password = request.getParameter("password").toLowerCase();

        System.out.println("username: " + username);
        System.out.println("password: " + password);
 
        if(checkLogin(username, password)) {
        	//set session variables so we can get it for home page dashboard
        	session.setAttribute("username", request.getParameter("username"));
        	session.setAttribute("dbOwnerId", request.getParameter("bdOwnerId"));
        	
        	session = request.getSession();
        	session.setAttribute("mobileUser", mobileUser);		

        	//if login info is correct send to home screen to view dash board. 
        	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pages/HomePage.jsp");
        	dispatcher.forward(request, response);
        	
        } else {
        	System.out.println("PETE: Username and password dont match.");
        	System.out.println("PETE: send javascript error.");
        }
        
	}
	
	public boolean checkLogin(String username, String password) {
    	String query;
        String dbUsername, dbPassword;		// need to be able to query database on jsp side and set to session variable to make is easy
        boolean login = false;
           
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/pets", "root", "gels0207");
            Statement stmt = (Statement) con.createStatement();
            
            query = "SELECT iduser, username, password "
            		+ "FROM users "
            		+ "WHERE username='" + username + "' AND password='" + password + "';";
            
            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();
            while(rs.next()){
                dbUsername = rs.getString("Username");
                dbPassword = rs.getString("Password");
                dbOwnerId = rs.getInt("idUser");
                
                if(dbUsername.equals(username) && dbPassword.equals(password)){
                    System.out.println("Found User");
                    login = true;
                } else {
                	login = false;
                }
            }
        } catch (SQLException e) {
			System.out.println("Could Not connect to database");
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
             return login;
                 
    }

}
