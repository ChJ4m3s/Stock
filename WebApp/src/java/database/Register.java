package database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Database db = new Database();
        Connection cn = db.getDb();
        if (cn != null) {
            if (!existsEmail(cn, email)) {
                try {
                    PreparedStatement ps = cn.prepareStatement("INSERT INTO Users(Name, Surname, Email, Password) VALUES(?, ?, ?, md5(?))");
                    ps.setString(1, name);
                    ps.setString(2, surname);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        cn.commit();
                        out.println("<html>");
                        out.println("<head>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<p>You are logged correctly! click <a href=\"index.jsp\">here</a> to login</p>");
                        out.println("</body>");
                        out.println("</html>");
                    }
                    else {
                        cn.rollback();
                        out.println("Error registration");
                    }
                } catch (SQLException ex) {
                    System.err.println("error prepareStatement()");
                }
            }
            else {
                out.println("<html>");
                out.println("<head>");
                out.println("</head>");
                out.println("<body>");
                out.println("<p>email is already registered click <a href=\"index.html\">here</a> to login</p>");
                out.println("</body>");
                out.println("</html>");
            }
        }
        else {
            out.println("Error connection");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Missing values");
    }
    boolean existsEmail(Connection cn, String email) {
        boolean exists = false;
        try {
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM Users WHERE Email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.getRow() != 0)
                exists = true;
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
}