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

public class AddTitle extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("email");
        String title = request.getParameter("title");
        if (title != null && !title.equals("")) {
            String query = "INSERT INTO UserTitle(User, Title, Code) VALUES(?, ?, DEFAULT)";
            Database db = new Database();
            Connection cn = db.getDb();
            try {
                PreparedStatement ps = cn.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, title);
                int i = ps.executeUpdate();
                if (i > 0)
                    cn.commit();
                else
                    cn.rollback();
                response.sendRedirect("profile.jsp");
            } catch (SQLException ex) {
                Logger.getLogger(AddTitle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
