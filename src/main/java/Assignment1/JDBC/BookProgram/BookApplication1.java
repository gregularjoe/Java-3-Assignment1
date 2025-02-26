package Assignment1.JDBC.BookProgram;

import Assignment1.JDBC.MariaDBProperties;

import java.io.PrintStream;

public class BookApplication1 {
    public static void main(String[] args) {
        BookDatabaseManager dbManager = new BookDatabaseManager();
        Library library = new Library();

        // Create some authors and books
        Author author1 = new Author(22, "George", "Orwell");
        Author author2 = new Author(23, "Aldous", "Huxley");

        Book book1 = new Book("123456789", "1984", 1, "1949");
        Book book2 = new Book("987654321", "Brave New World", 1, "1932");

        // Add authors to books
        book1.addAuthor(author1);
        book2.addAuthor(author2);

        // Add books and authors to library
        library.addBook(book1);
        library.addBook(book2);
        library.addAuthor(author1);
        library.addAuthor(author2);

        System.out.println("--------------------------------------");
        MariaDBProperties.findDriverClass(new PrintStream(System.out));
        MariaDBProperties.isDriverRegistered(new PrintStream(System.out));
        System.out.println("___________________________________________");


        // Save books and authors to the database
        // AUTHORS MUST COME BEFORE BOOKS
        dbManager.createAuthor(author1);
        dbManager.createAuthor(author2);

        dbManager.createBook(book1);
        dbManager.createBook(book2);

        // Delete books first
//        dbManager.deleteBook("123456789");
//        dbManager.deleteBook("987654321");
//
//        dbManager.deleteAuthor(22);
//        dbManager.deleteAuthor(23);


// Clear existing data in library
        library.getBooks().clear();
        library.getAuthors().clear();

// Load all books from the database into the library
        for (Book book : dbManager.getAllBooks()) {
            library.addBook(book);
        }

// Load all authors from the database into the library
        for (Author author : dbManager.getAllAuthors()) {
            library.addAuthor(author);
        }


// Retrieve and print all books from the library
        System.out.println("Books in Database:");
        for (Book book : library.getBooks()) {
            System.out.println(book);
        }

// Retrieve and print all authors from the library
        System.out.println("\nAuthors in Database:");
        for (Author author : library.getAuthors()) {
            System.out.println(author);
        }


        // Close the database connection
        dbManager.closeConnection();
    }
}

