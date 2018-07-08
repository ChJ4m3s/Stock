package android;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NameSurname extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Database db = new Database();
        Connection cn = db.getDb();
        String query = "SELECT Name, Surname FROM Users WHERE Email = ? AND Password = md5(?)";
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                try {
                    PrintWriter pw = response.getWriter();
                    pw.println(rs.getString("Name") + " " + rs.getString("Surname"));
                } catch (IOException ex) {
                    Logger.getLogger(NameSurname.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(NameSurname.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
}
