package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    public static final String DB_URL = "dbUrl";
    public static final String DB_USER = "user";
    public static final String DB_PASSWORD = "password";

    public static Connection connection = null; // connecting to db with a variable
    private final String jdbcURL;
    private final Properties properties = new Properties();

    public DBConnection() {
        try (InputStream inputStream = new FileInputStream("project.properties")) { // setting properties value from file
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.jdbcURL = properties.getProperty(DB_URL);
    }

    // Driver reference
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // ----------   START DB CONNECTION  ----------//
    public Connection startConnection() {
        try {
            Class.forName(MYSQL_JDBC_DRIVER); // outdated
            connection = DriverManager.getConnection(jdbcURL, properties);
            System.out.println("Successfully connected to DB");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Failed to connect to DB");
        }
        return connection;
    }

    // ----------   CLOSE DB CONNECTION  ----------//
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("mySQL DB connection closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

