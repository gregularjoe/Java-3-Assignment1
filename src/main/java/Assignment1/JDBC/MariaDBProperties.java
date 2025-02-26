package Assignment1.JDBC;

import java.io.PrintStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The properties used in the Maria DB setup used throughout the course lectures.
 */
public class MariaDBProperties {

    private static final String DATABASE_NAME = "/books";
    public static final String DATABASE_URL = "jdbc:mariadb://localhost:3308" + DATABASE_NAME;

    public static final String DATABASE_USER = "root";

    //TODO Update this with your Password!
    public static final String DATABASE_PASSWORD = "root";

    public static final String DATABASE_URL_COMPLETE = "jdbc:mariadb://localhost:3308?user="+ DATABASE_USER +"&password=" + DATABASE_PASSWORD;

    /**
     * This method attempts to find and load the MariaDB JDBC driver class.
     *
     * @param printStream The PrintStream to output status messages.
     * @return true if the driver class is found and loaded successfully, false otherwise.
     */
    public static boolean findDriverClass(PrintStream printStream) {
        try {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            printStream.println("Option 1: Find the class worked!");
            return true;
        } catch (ClassNotFoundException ex) {
            printStream.println("Error: unable to load driver class!");
        } catch (IllegalAccessException ex) {
            printStream.println("Error: access problem while loading!");
        } catch (InstantiationException ex) {
            printStream.println("Error: unable to instantiate driver!");
        }
        return false;
    }

    /**
     * This method attempts to register the MariaDB JDBC driver with the DriverManager.
     *
     * @param printStream The PrintStream to output status messages.
     * @return true if the driver is registered successfully, false otherwise.
     */
    public static boolean registerDriver(PrintStream printStream) {
        try {
            Driver myDriver = new org.mariadb.jdbc.Driver();
            DriverManager.registerDriver(myDriver);
            printStream.println("Option 2: Register the Driver worked!");
            return true;
        } catch (SQLException ex) {
            printStream.println("Error: unable to load driver class!");
        }
        return false;
    }

    /**
     * This method checks if the MariaDB JDBC driver is registered by first attempting to find the driver class
     * and then attempting to register the driver.
     *
     * @param printStream The PrintStream to output status messages.
     * @return true if both the driver class is found and the driver is registered successfully, false otherwise.
     */
    public static boolean isDriverRegistered(PrintStream printStream) {
        boolean isFound = findDriverClass(printStream);
        boolean isRegistered = registerDriver(printStream);
        return isFound && isRegistered;
    }

















    /**
     * Register the driver using two options - Class.forName and Driver class
     * //TODO clean this method up / split it into two methods?
     * @param printStream where you will print failure information (System.err recommended)
     * @return true if registered successfully
     */
//    public static boolean isDriverRegistered(PrintStream printStream){
//        //Option 1: Find the class
//        try {
//            Class.forName("org.mariadb.jdbc.Driver").newInstance();
//            printStream.println("Option 1: Find the class worked!");
//        } catch (ClassNotFoundException ex) {
//            printStream.println("Error: unable to load driver class!");
//            return false;
//        } catch (IllegalAccessException ex) {
//            printStream.println("Error: access problem while loading!");
//            return false;
//        } catch (InstantiationException ex) {
//            printStream.println("Error: unable to instantiate driver!");
//            return false;
//        }
//
//        //Option 2: Register the Driver
//        try {
//            Driver myDriver = new org.mariadb.jdbc.Driver();
//            DriverManager.registerDriver(myDriver);
//            printStream.println("Option 2: Register the Driver worked!");
//        } catch (SQLException ex) {
//            printStream.println("Error: unable to load driver class!");
//            return false;
//        }
//        return true;
//    }

}
