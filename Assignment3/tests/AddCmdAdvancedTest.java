import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AddCmdAdvancedTest extends CommandTest {

    private static final String TEST_PATH = "booksTestData01.csv";

    @Override
    protected CommandType getCmdType() {
        return CommandType.ADD;
    }

    @Before
    public void setup() {
        testCommand = new AddCmd(TEST_PATH);

        testLibrary = new LibraryData();
        List<BookEntry> bookData = new ArrayList<>();
        bookData.add(new BookEntry("TitleA", new String[] { "AuthorA" }, 3.2f, "ISBNA", 500));
        FieldTestUtils.setPrivateField(testLibrary, testLibrary.getClass(), "books", bookData);
    }

    // ------------------------- parseArguments tests --------------------

    @Test
    public void testParseArgumentsIllegalArgument() {

        String WHITE_SPACE = " ";
        CommandTestUtils.checkArgumentInput(testCommand, false, WHITE_SPACE);
        String invalidPath = "csv.dat";
        CommandTestUtils.checkArgumentInput(testCommand, false, invalidPath);
        invalidPath = "hello.txt";
        CommandTestUtils.checkArgumentInput(testCommand, false, invalidPath);
        invalidPath = "csv.py";
        CommandTestUtils.checkArgumentInput(testCommand, false, invalidPath);
    }

    @Test(expected = NullPointerException.class)
    public void testParseArgumentsNull() {
        String invalidPath = null; // null pointer expected if the string path is null
        CommandTestUtils.checkArgumentInput(testCommand, false, invalidPath);
    }

    @Test
    public void testParseArgumentsLegalArgument() {
        String validPath;
        CommandTestUtils.checkArgumentInput(testCommand, true, TEST_PATH);
        validPath = "csv.csv";
        CommandTestUtils.checkArgumentInput(testCommand, true, validPath);
        validPath = "this/is/some/test/path/for/books.csv";
        CommandTestUtils.checkArgumentInput(testCommand, true, validPath);
        validPath = "txt/csv/dat/py/hello.csv";
        CommandTestUtils.checkArgumentInput(testCommand, true, validPath);
        validPath = "books03.csv";
        CommandTestUtils.checkArgumentInput(testCommand, true, validPath);
        validPath = ".csv";
        CommandTestUtils.checkArgumentInput(testCommand, true, validPath);
        validPath = " .csv";
        CommandTestUtils.checkArgumentInput(testCommand, true, validPath);
    }

    // ------------------------- execute tests --------------------

    @Test
    public void testExecuteLoadData() {

        testCommand.execute(testLibrary);

        List<BookEntry> books = testLibrary.getBookData();
        books = new ArrayList<>();
        int expectedBookAmount = 0;
        assertEquals("Unexpected amount of books in library after loading file.", expectedBookAmount, books.size());
        // implementing empty array
        List<Object[]> expectedBookValues = new ArrayList<>();
        expectedBookValues.add(new Object[] { "TitleA", new String[] { "AuthorA" }, 3.2f, "ISBNA", 500 });
        expectedBookValues.add(
                new Object[] { "The Changeling", new String[] { "Zilpha Keatley Snyder" }, 4.17f, "595321801", 228 });
        expectedBookValues
                .add(new Object[] { "Animal Farm", new String[] { "George Orwell" }, 3.91f, "452284244", 122 });

        for (int i = 0; i < books.size(); i++) {
            BookEntryTestUtils.checkBookFieldValues(books.get(i), BookEntryBasicTest.BOOK_ENTRY_FIELD_NAMES,
                    expectedBookValues.get(i));
        }
    }
}