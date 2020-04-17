import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * Immutable class encapsulating data for a single book entry.
 */
public class BookEntry {

    /**
     * Maximal possible rating of a book.
     */
    private static final int MAXIMAL_RATING = 5;

    /**
     * Minimal possible rating of a book.
     */
    private static final int MINIMAL_RATING = 0;

    /**
     * Minimal possible number of pages of a book.
     */
    private static final int MINIMAL_PAGES_NUMBER = 0;

    /**
     * Title of the book.
     */
    private final String title;

    /**
     * Authors of the book.
     */
    private final String[] authors;

    /**
     * Rating of the book.
     */
    private final float rating;

    /**
     * ISBN of the book.
     */
    private final String ISBN;

    /**
     * Number of pages of the book.
     */
    private final int pages;

    /**
     * Create a single book entry.
     *
     * @param title   Title of the book
     * @param authors Authors of the book
     * @param rating  Rating of the book
     * @param ISBN    ISBN of the book
     * @param pages   Number of pages of the book
     * @throws NullPointerException     If one of the object parameters is null.
     * @throws IllegalArgumentException If rating is not within the correct range.
     * @throws IllegalArgumentException If pages is not within the correct range.
     */
    public BookEntry(String title, String[] authors, float rating, String ISBN, int pages) {
        Objects.requireNonNull(title, "Given title must not be null.");
        Objects.requireNonNull(authors, "Given authors must not be null.");
        Objects.requireNonNull(ISBN, "Given ISBN must not be null.");

        if (Arrays.asList(authors).contains(null)) {
            throw new NullPointerException("No author can be null.");
        }

        if (rating < MINIMAL_RATING || rating > MAXIMAL_RATING) {
            throw new IllegalArgumentException(String.format(Locale.UK, "Given rating should be" +
                    " between %d and %d: %.2f", MINIMAL_RATING, MAXIMAL_RATING, rating));
        }

        if (pages < MINIMAL_PAGES_NUMBER) {
            throw new IllegalArgumentException(String.format("Given number of pages" +
                    " should be greater than %d: %d", MINIMAL_PAGES_NUMBER, pages));
        }

        this.title = title;
        this.authors = authors;
        this.rating = rating;
        this.ISBN = ISBN;
        this.pages = pages;
    }

    /**
     * Get the title of the book.
     *
     * @return Title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get all the authors of the book.
     *
     * @return Authors of the book
     */
    public String[] getAuthors() {
        return authors;
    }

    /**
     * Get the rating of the book.
     *
     * @return Rating of the book
     */
    public float getRating() {
        return rating;
    }

    /**
     * Get the ISBN of the book.
     *
     * @return ISBN of the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Get the number of pages of the book.
     *
     * @return Number of pages of the book
     */
    public int getPages() {
        return pages;
    }

    /**
     * Return a string representation of the BookEntry. This string "textually represents"
     * this BookEntry. The result should be a concise but informative representation that
     * is easy for a person to read.
     *
     * @return String representation of the BookEntry.
     */
    @Override
    public String toString() {
        String authors = Arrays.toString(this.authors);
        authors = authors.substring(1, authors.length() - 1); // same string with no brackets at the beginning and end
        String rating = String.format(Locale.UK, "%.2f", this.rating);

        return String.format("%s\nby %s\nRating: %s\nISBN: %s\n%d pages", title, authors, rating, ISBN, pages);
    }

    /**
     * Indicate whether some other BookEntry is "equal to" this one.
     *
     * @param other The reference BookEntry with which to compare.
     * @return True if this BookEntry is the same as the BookEntry argument.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null | !(other instanceof BookEntry)) {
            return false;
        }

        BookEntry book = (BookEntry) other;

        return title.equals(book.title) &&
                Arrays.equals(authors, book.authors) &&
                rating == book.rating &&
                ISBN.equals(book.ISBN) &&
                pages == book.pages;
    }

    /**
     * Return a hash code value for the BookEntry.
     *
     * @return Hash code value for this BookEntry.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(title, rating, ISBN, pages);
        result = 31 * result + Arrays.hashCode(authors);
        return result;
    }
}
