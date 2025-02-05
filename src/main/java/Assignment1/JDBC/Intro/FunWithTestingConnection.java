package Assignment1.JDBC.Intro;

import Assignment1.JDBC.MariaDBProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Simple class to test a connection
 */
public class FunWithTestingConnection {

    public static void main(String[] args) {
        try{
                //Test the connection with a single complete URL
                Connection conn = DriverManager.getConnection(MariaDBProperties.DATABASE_URL_COMPLETE);
                System.out.println("Connection Successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
