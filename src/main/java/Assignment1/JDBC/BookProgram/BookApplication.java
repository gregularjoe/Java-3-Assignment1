package Assignment1.JDBC.BookProgram;

import Assignment1.JDBC.MariaDBProperties;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class BookApplication {
    private static BookDatabaseManager dbManager;
    private static Library library;
    private static Scanner scanner;

    public static void main(String[] args) {
        dbManager = new BookDatabaseManager();
        library = new Library();
        scanner = new Scanner(System.in);

        // Load initial data
        refreshLibraryData();
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> printAllBooks();
                case 2 -> printAllAuthors();
                case 3 -> editBookOrAuthor();
                case 4 -> addNewBook();
                case 5 -> deleteBook();
                case 6 -> deleteAuthor();
                case 7 -> running = false;
            }
        }
        cleanup();
    }

    private static void displayMenu() {
        System.out.println("\n=== Greg's Super Slick Book App ===");
        System.out.println("1. Print all books");
        System.out.println("2. Print all authors");
        System.out.println("3. Edit a book or author");
        System.out.println("4. Add a new book");
        System.out.println("5. Delete a book");
        System.out.println("6. Delete an author");
        System.out.println("7. Quit");
        System.out.print("Enter your choice (1-7): ");
    }

    private static void printAllBooks() {
        System.out.println("\n=== All Books ===");
        List<Book> books = dbManager.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found in the database.");
        } else {
            for (Book book : books) {
                System.out.println(book);
                System.out.println("--------------------------------------------------------------------------------");
            }
        }
    }

    private static void printAllAuthors() {
        System.out.println("\n=== All Authors ===");
        List<Author> authors = dbManager.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors found in the database.");
        } else {
            for (Author author : authors) {
                System.out.println(author);
                System.out.println("--------------------------------------------------------------------------------");
            }
        }
    }

    private static void editBookOrAuthor() {
        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Edit a book");
        System.out.println("2. Edit an author");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 1) {
            editBook();
        } else {
            editAuthor();
        }
    }

    private static void editBook() {
        System.out.print("Enter the ISBN of the book to edit: ");
        String isbn = scanner.nextLine();
        Book book = dbManager.getBookByISBN(isbn);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Current book details: " + book);
        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Title");
        System.out.println("2. Edition Number");
        System.out.println("3. Copyright");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new title: ");
                String newTitle = scanner.nextLine();
                book.setTitle(newTitle);
            }
            case 2 -> {
                System.out.print("Enter new edition number: ");
                int newEdition = Integer.parseInt(scanner.nextLine());
                book.setEditionNumber(newEdition);
            }
            case 3 -> {
                System.out.print("Enter new copyright year: ");
                String newCopyright = scanner.nextLine();
                book.setCopyright(newCopyright);
            }
        }

        // Update in database
        dbManager.deleteBook(isbn);
        dbManager.createBook(book);
        System.out.println("Book updated successfully.");
        refreshLibraryData();
    }

    private static void editAuthor() {
        System.out.print("Enter the Author ID to edit: ");
        int authorId = Integer.parseInt(scanner.nextLine());
        Author author = dbManager.getAuthorByID(authorId);

        if (author == null) {
            System.out.println("Author not found.");
            return;
        }

        System.out.println("Current author details: " + author);
        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new first name: ");
                String newFirstName = scanner.nextLine();
                author.setFirstName(newFirstName);
            }
            case 2 -> {
                System.out.print("Enter new last name: ");
                String newLastName = scanner.nextLine();
                author.setLastName(newLastName);
            }
        }

        // Update in database
        dbManager.deleteAuthor(authorId);
        dbManager.createAuthor(author);
        System.out.println("Author updated successfully.");
        refreshLibraryData();
    }

    private static void deleteBook() {
        System.out.println("\n=== Delete Book ===");
        printAllBooks();

        System.out.print("\nEnter the ISBN of the book to delete: ");
        String isbn = scanner.nextLine();

        Book book = dbManager.getBookByISBN(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Are you sure you want to delete this book?");
        System.out.println(book);
        System.out.print("Enter 'yes' to confirm: ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            dbManager.deleteBook(isbn);
            System.out.println("Book deleted successfully.");
            refreshLibraryData();
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void deleteAuthor() {
        System.out.println("\n=== Delete Author ===");
        printAllAuthors();

        System.out.print("\nEnter the ID of the author to delete: ");
        int authorId = Integer.parseInt(scanner.nextLine());

        Author author = dbManager.getAuthorByID(authorId);
        if (author == null) {
            System.out.println("Author not found.");
            return;
        }

        System.out.println("Are you sure you want to delete this author?");
        System.out.println(author);
        System.out.print("Enter 'yes' to confirm: ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            dbManager.deleteAuthor(authorId);
            System.out.println("Author deleted successfully.");
            refreshLibraryData();
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void addNewBook() {
        System.out.println("\n=== Add New Book ===");

        // Get book details
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        if (dbManager.getBookByISBN(isbn) != null) {
            System.out.println("A book with this ISBN already exists.");
            return;
        }

        System.out.print("Enter title: ");
        String title = scanner.nextLine();

        System.out.print("Enter edition number: ");
        int editionNumber = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter copyright year: ");
        String copyright = scanner.nextLine();

        Book newBook = new Book(isbn, title, editionNumber, copyright);

        // Handle authors
        boolean addingAuthors = true;
        while (addingAuthors) {
            System.out.println("\nAdd author to the book:");
            System.out.println("1. Select existing author");
            System.out.println("2. Add new author");
            System.out.println("3. Finish adding authors");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addExistingAuthorToBook(newBook);
                case 2 -> addNewAuthorToBook(newBook);
                case 3 -> addingAuthors = false;
            }
        }

        // Save to database
        if (!newBook.getAuthorList().isEmpty()) {
            // Save all new authors first
            for (Author author : newBook.getAuthorList()) {
                if (dbManager.getAuthorByID(author.getAuthorID()) == null) {
                    dbManager.createAuthor(author);
                }
            }
            // Then save the book
            dbManager.createBook(newBook);
            System.out.println("Book added successfully.");
            refreshLibraryData();
        } else {
            System.out.println("Book must have at least one author.");
        }
    }

    private static void addExistingAuthorToBook(Book book) {
        List<Author> existingAuthors = dbManager.getAllAuthors();
        if (existingAuthors.isEmpty()) {
            System.out.println("No existing authors found. Please add a new author.");
            return;
        }

        System.out.println("\nExisting authors:");
        for (Author author : existingAuthors) {
            System.out.println(author.getAuthorID() + ": " + author.getFirstName() + " " + author.getLastName());
        }

        System.out.print("Enter the Author ID to add: ");
        int authorId = Integer.parseInt(scanner.nextLine());
        Author selectedAuthor = dbManager.getAuthorByID(authorId);

        if (selectedAuthor != null) {
            book.addAuthor(selectedAuthor);
            System.out.println("Author added to book.");
        } else {
            System.out.println("Author not found.");
        }
    }

    private static void addNewAuthorToBook(Book book) {
        System.out.println("\nEnter new author details:");

        System.out.print("Enter Author ID: ");
        int authorId = Integer.parseInt(scanner.nextLine());

        if (dbManager.getAuthorByID(authorId) != null) {
            System.out.println("An author with this ID already exists.");
            return;
        }

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        Author newAuthor = new Author(authorId, firstName, lastName);
        book.addAuthor(newAuthor);
        System.out.println("New author added to book.");
    }

    private static void refreshLibraryData() {
        library.getBooks().clear();
        library.getAuthors().clear();

        for (Book book : dbManager.getAllBooks()) {
            library.addBook(book);
        }

        for (Author author : dbManager.getAllAuthors()) {
            library.addAuthor(author);
        }
    }

    private static void cleanup() {
        System.out.println("\nThank you for using Greg's Super Slick Application!");
        scanner.close();
        dbManager.closeConnection();
    }
}