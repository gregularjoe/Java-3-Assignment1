package Assignment1.JDBC.BookProgram;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private int authorID;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new ArrayList<>();
    }

    // Getters and setters
    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Book> getBookList() {
        return bookList;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Author author = (Author) obj;
        return authorID == author.authorID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(authorID);
    }
}