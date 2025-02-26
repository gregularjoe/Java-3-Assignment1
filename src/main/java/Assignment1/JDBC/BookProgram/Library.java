package Assignment1.JDBC.BookProgram;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a Library containing collections of books and authors.
 */
public class Library {
    private List<Book> books;
    private List<Author> authors;

    /**
     * Constructs a new Library with empty collections of books and authors.
     */
    public Library() {
        books = new ArrayList<>();
        authors = new ArrayList<>();
    }

    /**
     * Adds a book to the library's collection if it is not already present.
     *
     * @param book the book to be added
     */
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        }
    }

    /**
     * Adds an author to the library's collection if they are not already present.
     *
     * @param author the author to be added
     */
    public void addAuthor(Author author) {
        if (!authors.contains(author)) {
            authors.add(author);
        }
    }

    /**
     * Returns the list of books in the library.
     *
     * @return the list of books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Returns the list of authors in the library.
     *
     * @return the list of authors
     */
    public List<Author> getAuthors() {
        return authors;
    }
}
