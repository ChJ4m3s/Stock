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

public class ListTitlesMissing extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String query = "SELECT Title FROM Titles WHERE Title NOT IN (SELECT Title FROM UserTitle WHERE User = ?)";
        Database db = new Database();
        Connection cn = db.getDb();
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            try {
                PrintWriter pw = response.getWriter();
                while (rs.next()) {
                    String title = rs.getString("Title");
                    pw.print(title+"&");
                }
            } catch (IOException ex) {
                Logger.getLogger(ListTitlesMissing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListTitlesMissing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
