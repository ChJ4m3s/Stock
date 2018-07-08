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

public class AddTitle extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String title = request.getParameter("title");
        if (!title.equals("null")) {
            String query = "INSERT INTO UserTitle(User, Title, Code) VALUES(?, ?, DEFAULT)";
            Database db = new Database();
            Connection cn = db.getDb();
            try {
                PreparedStatement ps = cn.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, title);
                int i = ps.executeUpdate();
                PrintWriter pw = response.getWriter();
                if (i > 0) {
                    cn.commit();
                    pw.print("OK");
                }
                else {
                    cn.rollback();
                    pw.print("ERROR");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddTitle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
