package Assignment1.JDBC.BookProgram;

import Assignment1.JDBC.MariaDBProperties;

import java.sql.*;
import java.util.*;

public class BookDatabaseManager1 {
    private static final String URL = MariaDBProperties.DATABASE_URL ;
    private static final String USER = MariaDBProperties.DATABASE_USER;
    private static final String PASSWORD = MariaDBProperties.DATABASE_PASSWORD;


    private Connection connection;
    private final List<Book> books;
    private final List<Author> authors;

    public BookDatabaseManager1() {
        books = new ArrayList<>();
        authors = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close connection method
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createBook(Book book) {
        // Check if book already exists
        if (getBookByISBN(book.getIsbn()) != null) {
            System.out.println("Book with ISBN " + book.getIsbn() + " already exists. Skipping insertion.");
            return;
        }

        String sql = "INSERT INTO titles (isbn, title, editionNumber, copyright) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setInt(3, book.getEditionNumber());
            preparedStatement.setString(4, book.getCopyright());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in titles table: " + rowsAffected);

            // Add book to local list
            books.add(book);

            // Add relationships in authorisbn table
            for (Author author : book.getAuthorList()) {
                addBookAuthorRelation(book, author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createAuthor(Author author) {
        // Check if author already exists
        if (getAuthorByID(author.getAuthorID()) != null) {
            System.out.println("Author with ID " + author.getAuthorID() + " already exists. Skipping insertion.");
            return;
        }

        String sql = "INSERT INTO authors (authorID, firstName, lastName) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, author.getAuthorID());
            preparedStatement.setString(2, author.getFirstName());
            preparedStatement.setString(3, author.getLastName());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in authors table: " + rowsAffected);

            // Add author to local list
            authors.add(author);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addBookAuthorRelation(Book book, Author author) {
        String sql = "INSERT INTO authorisbn (authorID, isbn) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, author.getAuthorID());
            preparedStatement.setString(2, book.getIsbn());
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in authorisbn table: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Book getBookByISBN(String isbn) {
        String sql = "SELECT * FROM titles WHERE isbn = ?";
        Book book = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));

                // Retrieve and set authors for the book
                List<Author> authors = getAuthorsByISBN(isbn);
                for (Author author : authors) {
                    book.addAuthor(author);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public Author getAuthorByID(int authorID) {
        String sql = "SELECT * FROM authors WHERE authorID = ?";
        Author author = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorID);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));

                // Retrieve and set books for the author
                List<Book> books = getBooksByAuthorID(authorID);
                for (Book book : books) {
                    author.addBook(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return author;
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM titles";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Book book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));

                // Retrieve and set authors for the book
                List<Author> authors = getAuthorsByISBN(book.getIsbn());
                for (Author author : authors) {
                    book.addAuthor(author);
                }

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<Author> getAllAuthors() {
        String sql = "SELECT * FROM authors";
        List<Author> authors = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Author author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));

                // Retrieve and set books for the author
                List<Book> books = getBooksByAuthorID(author.getAuthorID());
                for (Book book : books) {
                    author.addBook(book);
                }

                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }


    private List<Author> getAuthorsByISBN(String isbn) {
        String sql = "SELECT a.authorID, a.firstName, a.lastName FROM authors a " +
                "JOIN authorisbn ai ON a.authorID = ai.authorID WHERE ai.isbn = ?";
        List<Author> authors = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Author author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }

    private List<Book> getBooksByAuthorID(int authorID) {
        String sql = "SELECT b.isbn, b.title, b.editionNumber, b.copyright FROM titles b " +
                "JOIN authorisbn ai ON b.isbn = ai.isbn WHERE ai.authorID = ?";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Book book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    public void deleteBook(String isbn) {
        // First, delete relationships in authorisbn table
        deleteBookAuthorRelations(isbn);

        // Then, delete from titles table
        String sql = "DELETE FROM titles WHERE isbn = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in titles table (delete): " + rowsAffected);

            // Remove from local list
            books.removeIf(book -> book.getIsbn().equals(isbn));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteAuthor(int authorID) {
        // First, delete relationships in authorisbn table
        deleteAuthorBookRelations(authorID);

        // Then, delete from authors table
        String sql = "DELETE FROM authors WHERE authorID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorID);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in authors table (delete): " + rowsAffected);

            // Remove from local list
            authors.removeIf(author -> author.getAuthorID() == authorID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteBookAuthorRelations(String isbn) {
        String sql = "DELETE FROM authorisbn WHERE isbn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in authorisbn table (delete book relations): " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAuthorBookRelations(int authorID) {
        String sql = "DELETE FROM authorisbn WHERE authorID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorID);
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected in authorisbn table (delete author relations): " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
