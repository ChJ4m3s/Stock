package android;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteTitle extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String title = request.getParameter("title");
        Database db = new Database();
        Connection cn = db.getDb();
        String query = "DELETE FROM UserTitle WHERE User = ? AND Title = ?";
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, title);
            int i = ps.executeUpdate();
            if (i > 0) {
                try {
                    PrintWriter pw = response.getWriter();
                    pw.print("OK");
                } catch (IOException ex) {
                    Logger.getLogger(DeleteTitle.class.getName()).log(Level.SEVERE, null, ex);
                }  
                cn.commit();
            }
            else 
                cn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(DeleteTitle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
