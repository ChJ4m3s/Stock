package tag;

import database.Database;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PrintGraphic extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        PageContext context = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        String title = request.getParameter("title");
        String query = "SELECT Price, Date FROM Stock WHERE Title = ? ORDER BY Date ASC";
        Database db = new Database();
        try {
            PreparedStatement ps = db.getDb().prepareStatement(query);
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            try {
                out.println("<script>");
                out.println("$(function () {");
                out.println("\"use strict\";");
                out.println("var line = new Morris.Line({");
                out.println("element: 'line-chart',");
                out.println("data: [");
                while (rs.next()) {
                    out.println(((i > 0)? ", " : "")+"{day: '"+rs.getString("Date")+"',");
                    out.println("a: "+rs.getString("Price")+"}");
                    i++;
                }
                out.println("],");
                out.println("xkey: 'day',");
                out.println("ykeys: ['a'],");
                out.println("parseTime: false,");
                out.println("labels: ['Price'],");
                out.println("pointSize: 4,");
                out.println("hideHover: true,");
                out.println("resize: true");
                out.println("});");
                out.println("});");
                out.println("</script>");
            } catch (IOException ex) {
                Logger.getLogger(PrintGraphic.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrintGraphic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
