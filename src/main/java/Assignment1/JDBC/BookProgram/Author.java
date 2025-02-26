package Assignment1.JDBC.BookProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Author with an ID, first name, last name, and a list of books.
 */
public class Author {
    private int authorID;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    /**
     * Constructs a new Author with the specified details.
     *
     * @param authorID the unique ID of the author
     * @param firstName the first name of the author
     * @param lastName the last name of the author
     */
    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new ArrayList<>();
    }

    /**
     * Returns the unique ID of the author.
     *
     * @return the unique ID of the author
     */
    public int getAuthorID() {
        return authorID;
    }

    /**
     * Sets the unique ID of the author.
     *
     * @param authorID the new unique ID of the author
     */
    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    /**
     * Returns the first name of the author.
     *
     * @return the first name of the author
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the author.
     *
     * @param firstName the new first name of the author
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the author.
     *
     * @return the last name of the author
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the author.
     *
     * @param lastName the new last name of the author
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the list of books written by the author.
     *
     * @return the list of books written by the author
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * Adds a book to the author's book list if not already present.
     * Also ensures the bidirectional relationship by adding this author to the book's author list.
     *
     * @param book the book to be added
     */
    public void addBook(Book book) {
        // Check if book is already in the list to avoid circular adds
        if (!bookList.contains(book)) {
            bookList.add(book);
            // Add this author to the book's author list if not already there
            if (!book.getAuthorList().contains(this)) {
                book.addAuthor(this);
            }

        }
    }

    /**
     * Returns a string representation of the author, including their ID, name, and list of books.
     *
     * @return a string representation of the author
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(authorID)
                .append(", Name: ").append(firstName).append(" ").append(lastName)
                .append("\nBooks: ");

        if (bookList.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < bookList.size(); i++) {
                Book book = bookList.get(i);
                sb.append(book.getTitle());
                if (i < bookList.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }

    /**
     * Compares this author to the specified object. The result is {@code true} if and only if
     * the argument is not {@code null} and is an {@code Author} object that has the same ID.
     *
     * @param obj the object to compare this {@code Author} against
     * @return {@code true} if the given object represents an {@code Author} equivalent to this author, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Author author = (Author) obj;
        return authorID == author.authorID;
    }

}