package tag;

import database.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TitleMissingList extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        String email = (String) getJspContext().getAttribute("email", PageContext.SESSION_SCOPE);
        Database db = new Database();
        Connection cn = db.getDb();
        String query = "SELECT Title FROM Titles WHERE Title NOT IN (SELECT Title FROM UserTitle WHERE User = ?) ORDER BY Title";
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            try {
                out.println("<option value=\"null\">----</option>");
            } catch (IOException ex) {
                Logger.getLogger(TitleMissingList.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (rs.next() != false) {
                String title = rs.getString("Title");
                try {
                    out.println("<option value=\""+title+"\">"+title+"</option>");
                } catch (IOException ex) {
                    Logger.getLogger(TitleMissingList.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TitleMissingList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
