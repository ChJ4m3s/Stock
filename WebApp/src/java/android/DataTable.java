package android;

import database.Database;
import database.ParseWebsite;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataTable extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        if (email != null) {
            List<String> titles = ParseWebsite.parse();
            Database db = new Database();
            String query = "SELECT Title FROM UserTitle WHERE User = ? ORDER BY Title";
            try {
                PreparedStatement ps = db.getDb().prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                List<String[]> table = new <String[]>ArrayList();
                for (String row : titles) {
                    String[] rows = row.split("&");
                    table.add(rows);
                }
                int i = 0;
                PrintWriter out;
                try {
                    out = response.getWriter();
                    while (rs.next() != false) {
                        String title = rs.getString("Title");
                        while (i < table.size() && !title.equals(table.get(i)[0])) 
                            i++;
                        if (i < table.size()) {
                            for (int j = 0; j < 7; j++) {
                                out.print(table.get(i)[j]+((i == table.size() - 1 && j == 6)? "" : "&"));
                            }
                        }
                        i++;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}