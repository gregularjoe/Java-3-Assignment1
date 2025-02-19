package Assignment1.JDBC.BookProgram;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import Assignment1.JDBC.MariaDBProperties;
import java.util.List;

public class Book {
    private int isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    public Book(int isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    // Getters and setters
    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void addAuthor(Author author) {
        // Check if author is already in the list to avoid circular adds
        if (!authorList.contains(author)) {
            authorList.add(author);
            // Add this book to the author's book list if not already there
            if (!author.getBookList().contains(this)) {
                author.addBook(this);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ISBN: ").append(isbn)
                .append(", Title: ").append(title)
                .append(", Edition: ").append(editionNumber)
                .append(", Copyright: ").append(copyright)
                .append("\nAuthors: ");

        if (authorList.isEmpty()) {
            sb.append("None");
        } else {
            for (int i = 0; i < authorList.size(); i++) {
                Author author = authorList.get(i);
                sb.append(author.getFirstName()).append(" ").append(author.getLastName());
                if (i < authorList.size() - 1) {
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
        Book book = (Book) obj;
        return isbn == book.isbn;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(isbn);
    }
}