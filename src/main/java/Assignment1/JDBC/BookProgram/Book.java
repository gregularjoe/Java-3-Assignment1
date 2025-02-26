package Assignment1.JDBC.BookProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Book with an ISBN, title, edition number, copyright, and a list of authors.
 */
public class Book {
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    /**
     * Constructs a new Book with the specified details.
     *
     * @param isbn the ISBN of the book
     * @param title the title of the book
     * @param editionNumber the edition number of the book
     * @param copyright the copyright year of the book
     */
    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    /**
     * Returns the ISBN of the book.
     *
     * @return the ISBN of the book
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn the new ISBN of the book
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the new title of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the edition number of the book.
     *
     * @return the edition number of the book
     */
    public int getEditionNumber() {
        return editionNumber;
    }

    /**
     * Sets the edition number of the book.
     *
     * @param editionNumber the new edition number of the book
     */
    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    /**
     * Returns the copyright year of the book.
     *
     * @return the copyright year of the book
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the copyright year of the book.
     *
     * @param copyright the new copyright year of the book
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Returns the list of authors for the book.
     *
     * @return the list of authors for the book
     */
    public List<Author> getAuthorList() {
        return authorList;
    }

    /**
     * Adds an author to the book. Also ensures the book is added to the author's book list.
     *
     * @param author the author to be added
     */
    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            if (!author.getBookList().contains(this)) {
                author.addBook(this);
            }
        }
    }

    /**
     * Returns a string representation of the book, including its ISBN, title, edition number, copyright year,
     * and list of authors.
     *
     * @return a string representation of the book
     */
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

    /**
     * Checks if this book is equal to another object. Two books are considered equal if they have the same ISBN.
     *
     * @param obj the object to compare with
     * @return true if the books have the same ISBN, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return isbn.equals(book.isbn);
    }
}
