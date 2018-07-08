package tag;

import database.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class NameSurname extends SimpleTagSupport {

    @Override
    public void doTag() throws JspTagException {
        try {
            String email = (String) getJspContext().getAttribute("email", PageContext.SESSION_SCOPE);
            Database db = new Database();
            Connection cn = db.getDb();
            String query = "SELECT Name, Surname FROM Users WHERE Email = ?";
            String nameSurname = "";
            try {
                PreparedStatement ps = cn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) 
                    nameSurname = nameSurname+rs.getString("Name")+" "+rs.getString("Surname");
            } catch (SQLException ex) {
                Logger.getLogger(NameSurname.class.getName()).log(Level.SEVERE, null, ex);
            }
            getJspContext().getOut().print(nameSurname);
        }
        catch (IOException e) {
            throw new JspTagException("Error: write to JSP out");
        }
        //return EVAL_BODY_INCLUDE;
    }
}
