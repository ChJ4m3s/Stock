package database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        boolean rememberMeChecked = request.getParameter( "checkbox" ) != null;
        if (email.equals("") || password.equals(""))
            response.sendRedirect("login.jsp?error=missing%20values");
        else {
            Database db = new Database();
            Connection cn = db.getDb();
            String preparedQuery = "SELECT * FROM Users WHERE Email=? AND Password=md5(?)";
            try {
                PreparedStatement ps = cn.prepareStatement(preparedQuery);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("email", email);
                    session.setAttribute("password", password);
                    if (rememberMeChecked) {
                        Cookie cookieEmail = new Cookie("email", email);
                        cookieEmail.setMaxAge(-1);
                        Cookie cookiePassword = new Cookie("password", password);
                        cookiePassword.setMaxAge(-1);
                        response.addCookie(cookieEmail);
                        response.addCookie(cookiePassword);
                    }
                    response.sendRedirect("profile.jsp");
                }
                else
                    response.sendRedirect("login.jsp?error=wrong%20username%20or%20password");
            } catch (SQLException ex) {
                response.getWriter().println(ex.toString());
            }
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Missing values");
    }
}