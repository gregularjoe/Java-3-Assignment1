package Assignment1.JDBC.BookProgram;

import Assignment1.JDBC.MariaDBProperties;

import java.sql.*;

/**
 * This is the first example involving queries in the Objective 2 notes.
 */
public class BookDatabaseManager {

    public static final String DB_NAME = "/books";

    public void getBooksWithAuthors() {

        String QUERY = "SELECT \n" +
                "    titles.title AS Title, \n" +
                "    GROUP_CONCAT(CONCAT(authors.firstName, ' ', authors.lastName) SEPARATOR ', ') AS Authors, \n" +
                "    titles.isbn AS ISBN, \n" +
                "    titles.editionNumber AS EditionNumber, \n" +
                "    titles.copyright AS Copyright\n" +
                "FROM \n" +
                "    titles\n" +
                "INNER JOIN \n" +
                "    authorISBN ON titles.isbn = authorISBN.isbn\n" +
                "INNER JOIN \n" +
                "    authors ON authorISBN.authorID = authors.authorID\n" +
                "GROUP BY \n" +
                "    titles.isbn, \n" +
                "    titles.title, \n" +
                "    titles.editionNumber, \n" +
                "    titles.copyright\n" +
                "ORDER BY \n" +
                "    titles.title;\n";
        try{
            Connection conn = DriverManager.getConnection(
                    MariaDBProperties.DATABASE_URL + DB_NAME, MariaDBProperties.DATABASE_USER, MariaDBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.println("-----------------------------------------------");
                System.out.println("ISBN: " + rs.getString("ISBN"));
                System.out.println("Title: " + rs.getString("Title"));
                System.out.println("Authors: " + rs.getString("Authors"));
                System.out.println("-----------------------------------------------");


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void getAuthorsWithBooks() {

        String QUERY = "SELECT " +
                "authors.authorID, " +
                "CONCAT(authors.firstName, ' ', authors.lastName) AS Author, " +
                "titles.isbn AS ISBN, " +
                "titles.title AS BookTitle " +
                "FROM authors " +
                "INNER JOIN authorISBN ON authors.authorID = authorISBN.authorID " +
                "INNER JOIN titles ON authorISBN.isbn = titles.isbn " +
                "ORDER BY Author, BookTitle;";

        try {
            Connection conn = DriverManager.getConnection(
                    MariaDBProperties.DATABASE_URL + DB_NAME,
                    MariaDBProperties.DATABASE_USER,
                    MariaDBProperties.DATABASE_PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);

            String currentAuthor = "";
            while (rs.next()) {
                String author = rs.getString("Author");
                String isbn = rs.getString("ISBN");
                String bookTitle = rs.getString("BookTitle");

                if (!author.equals(currentAuthor)) {
                    // New author encountered
                    if (!currentAuthor.equals("")) {
                        System.out.println("-----------------------------------------------");
                    }
                    currentAuthor = author;
                    System.out.println("Author: " + author);
                    System.out.println("Books:");
                }

                System.out.println(" - Title: " + bookTitle + " (ISBN: " + isbn + ")");
            }
            System.out.println("-----------------------------------------------");

            // Close resources
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
