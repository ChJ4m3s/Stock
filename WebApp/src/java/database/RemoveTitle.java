package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RemoveTitle extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession();
        String email = (String) s.getAttribute("email");
        String title = request.getParameter("title");
        Database db = new Database();
        Connection cn = db.getDb();
        String query = "DELETE FROM UserTitle WHERE User = ? AND Title = ?";
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, title);
            int i = ps.executeUpdate();
            if (i == 1) {
                cn.commit();
                response.sendRedirect("profile.jsp");
            }
            else {
                cn.rollback();
                response.sendRedirect("graphic.jsp?title="+title);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RemoveTitle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
