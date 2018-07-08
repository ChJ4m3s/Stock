package android;

import database.Database;
import database.Register;
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

public class Registration extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Database db = new Database();
        Connection cn = db.getDb();
        String name, surname, email, password, result = "";
        name = request.getParameter("name");
        surname = request.getParameter("surname");
        email = request.getParameter("email");
        password = request.getParameter("password");
        if (name != null && surname != null && password != null && email != null) {
            if (!existsEmail(cn, email)) {
                String query = "INSERT INTO Users (Name, Surname, Email, Password) VALUES (?, ?, ?, md5(?))";
                try {
                    PreparedStatement ps = cn.prepareStatement(query);
                    ps.setString(1, name);
                    ps.setString(2, surname);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        cn.commit();
                        result = "OK";
                    }
                    else {
                        result = "ERROR";
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else 
                result = "EXISTS";
        }
        try {
            PrintWriter pw = response.getWriter();
            pw.println(result);
        } catch (IOException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    boolean existsEmail(Connection cn, String email) {
        boolean exists = false;
        try {
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM Users WHERE Email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.getRow() != 0)
                exists = true;
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return exists;
    }
}
