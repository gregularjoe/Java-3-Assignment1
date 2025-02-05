package Assignment1.JDBC.Intro;

import Assignment1.JDBC.MariaDBProperties;

import java.sql.*;

/**
 * This is the first example involving queries in the Objective 2 notes.
 */
public class FunWithQuerying {

    public static final String DB_NAME = "/employee_example";
    public static final String QUERY = "SELECT id, first, last, age FROM Employees";

    public static void main(String[] args) {

        try{
            Connection conn = DriverManager.getConnection(
                    MariaDBProperties.DATABASE_URL + DB_NAME, MariaDBProperties.DATABASE_USER, MariaDBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", Age: " + rs.getInt("age"));
                System.out.print(", First: " + rs.getString("first"));
                System.out.println(", Last: " + rs.getString("last"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
