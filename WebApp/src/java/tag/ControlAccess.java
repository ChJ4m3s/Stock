package tag;

import database.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ControlAccess extends SimpleTagSupport {

    @Override
    public void doTag() {
        String email = (String) getJspContext().getAttribute("email", PageContext.SESSION_SCOPE);
        String password = (String) getJspContext().getAttribute("password", PageContext.SESSION_SCOPE);
        PageContext context = (PageContext) getJspContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        if (email != null || password != null) {
            Database db = new Database();
            Connection cn = db.getDb();
            String query = "SELECT * FROM Users WHERE Email = ? AND Password = md5(?)";
            PreparedStatement ps;
            try {
                ps = cn.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if (rs.next() == false) {       
                    try {
                        response.sendRedirect("login.jsp");
                    } catch (IOException ex) {
                        Logger.getLogger(ControlAccess.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControlAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            try {
                response.sendRedirect("login.jsp");
            } catch (IOException ex) {
                Logger.getLogger(ControlAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
