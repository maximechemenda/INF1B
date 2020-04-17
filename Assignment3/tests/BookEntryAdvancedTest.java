import org.junit.Before;
import org.junit.Test;

import java.util.InputMismatchException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BookEntryAdvancedTest extends BookEntryTest {

    protected static final String ADVANCED_TITLE = "Odes to Common Things";

    protected static final String[] ADVANCED_AUTHORS = { "J.K. Rowling", null };
    protected static final String[] ADVANCED_AUTHORS1 = { "Pablo Neruda-Ferris", "Cook-Ken Krabbenhoft", "Hello World" };
    protected static final String[] ADVANCED_AUTHORS2 = { "Hello World", "Pablo Neruda-Ferris", "Cook-Ken Krabbenhoft" };

    protected static final float ADVANCED_RATING = 4.38f;

    protected static final String ADVANCED_ISBN1 = null;
    protected static final String ADVANCED_ISBN2 = "821220802";

    protected static final int ADVANCED_PAGES2 = 152;

    protected static final String ADVANCED_TOSTRING_RESULT = "Odes to Common Things\nby Pablo Neruda-Ferris, Cook-Ken Krabbenhoft, Hello World\nRating: 4.38\nISBN: 821220802\n152 pages";

    public static final Object[] BOOK_ENTRY_FIELD_VALUES_ADVANCED = { ADVANCED_TITLE, ADVANCED_AUTHORS1,
            ADVANCED_RATING, ADVANCED_ISBN2, ADVANCED_PAGES2 };

    // ------------------------- check fields --------------------
    protected BookEntry testBook;

    public BookEntryAdvancedTest() {
        testBook = null;
    }

    @Before
    public void setup() {
        testBook = null;
        // testBook = new BookEntry(ADVANCED_TITLE, ADVANCED_AUTHORS, ADVANCED_RATING,
        // DEFAULT_ISBN, ADVANCED_PAGES);
    }

    @Test(expected = NullPointerException.class)
    public void invalidAuthors() {
        testBook = new BookEntry(DEFAULT_TITLE, ADVANCED_AUTHORS, DEFAULT_RATING, DEFAULT_ISBN, DEFAULT_PAGES);
    }

    @Test(expected = NullPointerException.class)
    public void invalidISBN() {
        testBook = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, ADVANCED_ISBN1, DEFAULT_PAGES);
    }

    @Test
    public void testFieldTypes() {
        for (int i = 0; i < BOOK_ENTRY_FIELD_NAMES.length; i++) {
            FieldTestUtils.checkFieldType(testBook, BOOK_ENTRY_FIELD_TYPES[i], BOOK_ENTRY_FIELD_NAMES[i]);
        }
    }

    @Before
    public void reset() {
        testBook = new BookEntry(ADVANCED_TITLE, ADVANCED_AUTHORS1, ADVANCED_RATING, ADVANCED_ISBN2, ADVANCED_PAGES2);
    }
    // ------------------------- check constructor --------------------

    @Test
    public void testCtorFieldInitialisation() {
        BookEntryTestUtils.checkBookFieldValues(testBook, BOOK_ENTRY_FIELD_NAMES, BOOK_ENTRY_FIELD_VALUES_ADVANCED);
    }

    // ------------------------- check getters --------------------

    @Test
    public void testGetTitle() {
        String fieldName = TITLE_FIELD_NAME;
        String expected = "Test Title";
        FieldTestUtils.setPrivateField(testBook, testBook.getClass(), fieldName, expected);

        String actual = testBook.getTitle();
        assertEquals("Unexpected " + fieldName + " returned by getter.", expected, actual);
    }

    @Test
    public void testGetAuthors() {
        String fieldName = AUTHORS_FIELD_NAME;
        String[] expected = { "Test Author A", "Test Author B" };
        FieldTestUtils.setPrivateField(testBook, testBook.getClass(), fieldName, expected);

        String[] actual = testBook.getAuthors();
        assertArrayEquals("Unexpected " + fieldName + " returned by getter.", expected, actual);
    }

    @Test
    public void testGetRating() {
        String fieldName = RATING_FIELD_NAME;
        float expected = 2.3f;
        FieldTestUtils.setPrivateField(testBook, testBook.getClass(), fieldName, expected);

        float actual = testBook.getRating();
        assertEquals("Unexpected " + fieldName + " returned by getter.", expected, actual, CMP_DELTA);
    }

    @Test
    public void testGetISBN() {
        String fieldName = ISBN_FIELD_NAME;
        String expected = "158234681X";
        FieldTestUtils.setPrivateField(testBook, testBook.getClass(), fieldName, expected);

        String actual = testBook.getISBN();
        assertEquals("Unexpected " + fieldName + " returned by getter.", expected, actual);
    }

    @Test
    public void testGetPages() {
        String fieldName = PAGES_FIELD_NAME;
        int expected = 123;
        FieldTestUtils.setPrivateField(testBook, testBook.getClass(), fieldName, expected);

        int actual = testBook.getPages();
        assertEquals("Unexpected " + fieldName + " returned by getter.", expected, actual);
    }

    // ------------------------- check equals and hash code --------

    private void checkEquality(BookEntry bookA, BookEntry bookB, String field, boolean expected) {
        if (expected) {
            assertTrue("True return value expected for same fields.", bookA.equals(bookB) && bookB.equals(bookA));
            assertEquals("Hashcode expected to be the same for objects with the same state.", bookA.hashCode(),
                    bookB.hashCode());
        } else {
            assertTrue("False return value expected for different " + field + ".",
                    !bookA.equals(bookB) && !bookB.equals(bookA));
            assertNotEquals("Hashcode should be different for objects with different state.", bookA.hashCode(),
                    bookB.hashCode());
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        BookEntry bookA = new BookEntry(ADVANCED_TITLE, ADVANCED_AUTHORS1, ADVANCED_RATING, ADVANCED_ISBN2,
                ADVANCED_PAGES2);
        BookEntry bookB = new BookEntry(ADVANCED_TITLE, ADVANCED_AUTHORS2, ADVANCED_RATING, ADVANCED_ISBN2,
                ADVANCED_PAGES2);
        // different ordering different books
        assertTrue("True return value expected for same book instance.", bookA.equals(bookA) && bookB.equals(bookB));
        assertEquals("Hashcode expected to be the same for same instance.", bookA.hashCode(), bookA.hashCode());

        assertFalse("False return value expected if compared to different object type.", bookA.equals("test"));
        assertFalse("False return value expected if compared to null.", bookA.equals(null));

        checkEquality(bookA, bookB, TITLE_FIELD_NAME, false);

        bookA = new BookEntry(ADVANCED_TITLE, ADVANCED_AUTHORS1, ADVANCED_RATING, ADVANCED_ISBN2, ADVANCED_PAGES2);
        bookB = new BookEntry(ADVANCED_TITLE, ADVANCED_AUTHORS1, ADVANCED_RATING, ADVANCED_ISBN2, ADVANCED_PAGES2);

        checkEquality(bookA, bookB, TITLE_FIELD_NAME, true);

        bookA = new BookEntry(DEFAULT_TITLE, new String[] { "Author A" }, DEFAULT_RATING, DEFAULT_ISBN, DEFAULT_PAGES);
        bookB = new BookEntry(DEFAULT_TITLE, new String[] { "Author B" }, DEFAULT_RATING, DEFAULT_ISBN, DEFAULT_PAGES);

        checkEquality(bookA, bookB, AUTHORS_FIELD_NAME, false);

        bookA = new BookEntry(DEFAULT_TITLE, new String[] { "Author A" }, DEFAULT_RATING, DEFAULT_ISBN, DEFAULT_PAGES);
        bookB = new BookEntry(DEFAULT_TITLE, new String[] { "Author A", "Author B" }, DEFAULT_RATING, DEFAULT_ISBN,
                DEFAULT_PAGES);

        checkEquality(bookA, bookB, AUTHORS_FIELD_NAME, false);

        bookA = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, 3.0999999f, DEFAULT_ISBN, DEFAULT_PAGES);
        bookB = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, 3.1f, DEFAULT_ISBN, DEFAULT_PAGES); // approximation test

        checkEquality(bookA, bookB, RATING_FIELD_NAME, true);

        bookA = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, "1400054036", DEFAULT_PAGES);
        bookB = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, "1400054036   ", DEFAULT_PAGES);

        checkEquality(bookA, bookB, ISBN_FIELD_NAME, false);
        bookA = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, "1400054036", DEFAULT_PAGES);
        bookB = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, "1111111111", DEFAULT_PAGES);

        bookA = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, DEFAULT_ISBN, 1);
        bookB = new BookEntry(DEFAULT_TITLE, DEFAULT_AUTHORS, DEFAULT_RATING, DEFAULT_ISBN, 1);

        checkEquality(bookA, bookB, PAGES_FIELD_NAME, true);
    }

    // ------------------------- check toString --------------------

    @Test
    public void testToStringReturnValue() {
        String actualResult = testBook.toString();

        // ignore leading and trailing white spaces
        // and correct for potential Windows line endings
        assertEquals("ToString result not as expected.", ADVANCED_TOSTRING_RESULT.replaceAll("\r", "").trim(),
                actualResult.replaceAll("\r", "").trim());
    }
}