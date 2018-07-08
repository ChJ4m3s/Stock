package tag;

import database.Database;
import database.ParseWebsite;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;


public class TitleTable extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        try {
            out.println("<center><h2>Your titles:</h2></center>");
            out.println("<table class=\"table table-bordered table-hover\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Title</th><th>Price</th><th>Var %</th><th>Time</th><th>Min</th><th>Max</th><th>Qta</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");
            String email = (String) getJspContext().getAttribute("email", PageContext.SESSION_SCOPE);
            Database db = new Database();
            Connection cn = db.getDb();
            String query = "SELECT Title FROM UserTitle WHERE User = ? ORDER BY Title";
            try {
                PreparedStatement ps =cn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                List<String> table = ParseWebsite.parse();
                List<String[]> rows = new ArrayList();
                int i = 0;
                for (String s : table) {
                    String[] row = s.split("&");
                    rows.add(row);
                }
                while (rs.next() != false) {
                    String title = rs.getString("Title");
                    while (i < rows.size() && !title.equals(rows.get(i)[0])) 
                        i++;
                    if (i < rows.size()) {
                        out.println("<tr>");
                        for (int j = 0; j < rows.get(i).length; j++) {
                            if (j != 0)
                                out.println("<td>"+rows.get(i)[j]+"</td>");
                            else 
                                out.println("<td><a target=\"_blak\" href=\"graphic.jsp?title="+rows.get(i)[j]+"\">"+rows.get(i)[j]+"</a></td>");
                        }
                        out.println("</tr>");
                    }
                    i++;
                }
            } catch (SQLException ex) {
                Logger.getLogger(TitleTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("</tbody>");
            out.println("</table>");
        } catch (IOException ex) {
            Logger.getLogger(TitleTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
