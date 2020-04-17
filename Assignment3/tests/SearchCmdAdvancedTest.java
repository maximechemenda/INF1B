import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SearchCmdAdvancedTest extends SearchCmdTest {

    // ------------------------- parseArguments tests --------------------
    @Override
    protected CommandType getCmdType() {
        return CommandType.SEARCH;
    }

    @Before
    public void setup() {
        testCommand = new SearchCmd(SINGLE_WORD_TITLE);

        testLibrary = new LibraryData();
        List<BookEntry> bookData = new ArrayList<>();
        bookData.add(new BookEntry(MULTI_WORD_TITLE_A, new String[]{"AuthorA"}, 3.2f, "ISBNA", 500));
        bookData.add(new BookEntry(SINGLE_WORD_TITLE, new String[]{"AuthorB"}, 4.3f, "ISBNB", 400));
        bookData.add(new BookEntry(MULTI_WORD_TITLE_B, new String[]{"AuthorC"}, 1.3f, "ISBNC", 300));
        FieldTestUtils.setPrivateField(testLibrary, testLibrary.getClass(), "books", bookData);
    }
    @Test(expected = NullPointerException.class)
    public void testParseArgumentNull() { // non null
        CommandTestUtils.checkArgumentInput(testCommand,false,null);
    }
    @Test
    public void testParseArgumentIllegal() {

    }
    @Test
    public void testParseArgumentsIllegalArgument() {
        String blankArg = "";
        CommandTestUtils.checkArgumentInput(testCommand, false, blankArg);

        String argWithSpaces = "invalid search query";
        CommandTestUtils.checkArgumentInput(testCommand, false, argWithSpaces);
    }

    @Test
    public void testParseArgumentsLegalArgument() {
        CommandTestUtils.checkArgumentInput(testCommand, true, SINGLE_WORD_TITLE);

        String argWithHyphen = "Hundred-Dollar";
        CommandTestUtils.checkArgumentInput(testCommand, true, argWithHyphen);
    }

    // ------------------------- execute tests --------------------

    @Test(expected = NullPointerException.class)
    public void testExecuteNull() {
        CommandTestUtils.checkExecuteConsoleOutput(testCommand,null,"hello world");
    }
    @Test
    public void testExecuteFindExactMatch() {
        String expectedConsoleOutput = SINGLE_WORD_TITLE;
        CommandTestUtils.checkExecuteConsoleOutput(testCommand, testLibrary, expectedConsoleOutput);
    }

    @Test
    public void testExecuteFindWordMatch() {
        String expectedConsoleOutput = MULTI_WORD_TITLE_A;
        testCommand = new SearchCmd(MULTI_WORD_SEARCH_TERM_SINGLE_HIT);
        CommandTestUtils.checkExecuteConsoleOutput(testCommand, testLibrary, expectedConsoleOutput);
    }

    @Test
    public void testExecuteFindNoMatch() {
        String searchTerm = "Unknown";
        String expectedConsoleOutput = NO_HITS_FOUND_MESSAGE + searchTerm;
        testCommand = new SearchCmd(searchTerm);
        CommandTestUtils.checkExecuteConsoleOutput(testCommand, testLibrary, expectedConsoleOutput);
    }
}