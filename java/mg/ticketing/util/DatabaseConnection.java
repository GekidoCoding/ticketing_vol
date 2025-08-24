package mg.ticketing.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/ORCLPDB1";
    private static final String USER = "DEV_DEPL";
    private static final String PASSWORD = "DEV04";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC Oracle non trouvé", e);
        }
    }
}