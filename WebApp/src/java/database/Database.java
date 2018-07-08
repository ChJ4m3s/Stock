package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Database {
    Connection conn;
    public Database() {
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Driver not found.");
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://sql11.freesqldatabase.com:3306/sql11167665?zeroDateTimeBehavior=convertToNull", "sql11167665", "61NcRJNEhl");
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            System.err.println("Error connection");
        }
    }
    public Connection getDb() {
        return conn;
    }
}
