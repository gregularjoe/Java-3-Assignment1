package Assignment1.JDBC.BookProgram;

import Assignment1.JDBC.MariaDBProperties;

import java.sql.*;
import java.util.*;

/**
 * Manages the connection to the Book Database and operations related to books and authors.
 */
public class BookDatabaseManager {
    private static final String URL = MariaDBProperties.DATABASE_URL ;
    private static final String USER = MariaDBProperties.DATABASE_USER;
    private static final String PASSWORD = MariaDBProperties.DATABASE_PASSWORD;

    private Connection connection;
    private final List<Book> books;
    private final List<Author> authors;

    /**
     * Initializes the BookDatabaseManager, connects to the database, and initializes the local lists.
     */
    public BookDatabaseManager() {
        books = new ArrayList<>();
        authors = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the database if it is open.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new book record in the database and adds it to the local list. If the book already exists, it skips insertion.
     *
     * @param book The book to be added to the database.
     */
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




    /**
     * Creates a new author record in the database and adds it to the local list. If the author already exists, it skips insertion.
     *
     * @param author The author to be added to the database.
     */
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

    /**
     * Adds a relationship between a book and an author in the authorisbn table.
     *
     * @param book The book for the relationship.
     * @param author The author for the relationship.
     */
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



    /**
     * Retrieves a Book object by its ISBN.
     *
     * @param isbn The ISBN of the book to retrieve.
     * @return The Book object corresponding to the provided ISBN, or null if not found.
     */
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

    /**
     * Retrieves an Author object by its ID.
     *
     * @param authorID The ID of the author to retrieve.
     * @return The Author object corresponding to the provided ID, or null if not found.
     */
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

    /**
     * Retrieves a list of all books.
     *
     * @return A list of all Book objects.
     */
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


    /**
     * Retrieves a list of all authors.
     *
     * @return A list of all Author objects.
     */
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

    /**
     * Retrieves a list of authors for a given ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return A list of Author objects associated with the provided ISBN.
     */
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

    /**
     * Retrieves a list of books for a given author ID.
     *
     * @param authorID The ID of the author.
     * @return A list of Book objects associated with the provided author ID.
     */
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

    /**
     * Deletes a book and its related author associations from the database.
     *
     * @param isbn The ISBN of the book to delete.
     */
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

    /**
     * Deletes an author and its related book associations from the database.
     *
     * @param authorID The ID of the author to delete.
     */
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

    /**
     * Deletes the relationships between a book and its authors in the authorisbn table.
     *
     * @param isbn The ISBN of the book whose relations are to be deleted.
     */
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

    /**
     * Deletes the relationships between an author and their books in the authorisbn table.
     *
     * @param authorID The ID of the author whose relations are to be deleted.
     */
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
