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

public class GetTitleValues extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String titleName = request.getParameter("title");
        String query = "SELECT Date, Price FROM Stock WHERE Title = ? ORDER BY Date";
        Database db = new Database();
        Connection cn = db.getDb();
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, titleName);
            ResultSet rs = ps.executeQuery();
            StringBuilder resultBuilder = new StringBuilder();
            while (rs.next()) {
                resultBuilder.append(rs.getString("Date"));
                resultBuilder.append("&");
                resultBuilder.append(rs.getString("Price"));
                resultBuilder.append("&");
            }
            String result = resultBuilder.toString();
            PrintWriter pw = response.getWriter();
            pw.print(result);
        } catch (SQLException ex) {
            Logger.getLogger(GetTitleValues.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetTitleValues.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
