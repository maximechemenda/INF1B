import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListCmdAdvancedTest extends ListCmdTest {

    // ------------------------- parseArguments tests --------------------
    @Before
    public void setup() {
        testCommand = new ListCmd(SHORT_ARGUMENT);

        testLibrary = new LibraryData();
        List<BookEntry> data = new ArrayList<>();
        FieldTestUtils.setPrivateField(testLibrary,testLibrary.getClass(),"books",data);
        testLibrary = new LibraryData();
        List<BookEntry> bookData = new ArrayList<>();
        bookData.add(new BookEntry("Title A", new String[]{"Author A, Author B, Author C"}, 3.200f, "10101010100", 500)); // there should be no duplications though!
        bookData.add(new BookEntry("Title A", new String[]{"Author A, Author B, Author C"}, 3.200f, "10101010100", 500));
        FieldTestUtils.setPrivateField(testLibrary,testLibrary.getClass(),"books",bookData);
    }
    @Test(expected = NullPointerException.class)
    public void testParseArgumentsIllegalArgument() {
        CommandTestUtils.checkArgumentInput(testCommand, false, null);
        CommandTestUtils.checkArgumentInput(testCommand,false,"medium");
        CommandTestUtils.checkArgumentInput(testCommand,false,"sh ort");
    }

    @Test
    public void testParseArgumentsLegalArgument() {
        CommandTestUtils.checkArgumentInput(testCommand, true, SHORT_ARGUMENT);
        CommandTestUtils.checkArgumentInput(testCommand, true, LONG_ARGUMENT);
        CommandTestUtils.checkArgumentInput(testCommand, true, BLANK_ARGUMENT);
        CommandTestUtils.checkArgumentInput(testCommand,true,"    ");
        CommandTestUtils.checkArgumentInput(testCommand,false,SHORT_ARGUMENT.toUpperCase());
        CommandTestUtils.checkArgumentInput(testCommand,false,"LoNg");
    }
    // ------------------------- execute tests --------------------

    @Test
    public void testExecuteShortList() {
        String expectedConsoleOutput = "2 books in library:\nTitle A\nTitle A";
        CommandTestUtils.checkExecuteConsoleOutput(testCommand, testLibrary, expectedConsoleOutput);
    }

    @Test
    public void testExecuteLongList() {
        testCommand = new ListCmd(LONG_ARGUMENT);

        String expectedConsoleOutput =
                "2 books in library:\n" +
                        "Title A\n" +
                        "by Author A, Author B, Author C\n" +
                        "Rating: 3.20\n" +
                        "ISBN: 10101010100\n" +
                        "500 pages\n\n" +
                        "Title A\n" +
                        "by Author A, Author B, Author C\n" +
                        "Rating: 3.20\n" +
                        "ISBN: 10101010100\n" +
                        "500 pages\n\n";

        CommandTestUtils.checkExecuteConsoleOutput(testCommand, testLibrary, expectedConsoleOutput);
    }
}