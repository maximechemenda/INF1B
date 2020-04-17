import java.util.List;
import java.util.Objects;

public class SearchCmd extends LibraryCommand {

    /**
     * Separator between the arguments from user input.
     */
    private static final String COMMAND_ARGUMENT_DELIMITER = " ";

    /**
     * Command argument that follows the SEARCH command which is currently being executed.
     */
    private String commandArgument;

    /**
     * Create a search command.
     *
     * @param argumentInput Command argument.
     * @throws IllegalArgumentException If given arguments are invalid.
     * @throws NullPointerException     If the given argumentInput is null.
     */
    public SearchCmd(String argumentInput) {
        super(CommandType.SEARCH, argumentInput);
    }

    /**
     * Execute the search command. It prints the titles of all books that
     * contain the search value to the command line in the order they are found.
     *
     * @param data Book data to be considered for command execution.
     * @throws NullPointerException If data is null.
     * @throws NullPointerException If bookEntries (the loaded books) is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, ExceptionMessage.NULL_DATA.getExceptionMessage());

        List<BookEntry> bookEntries = data.getBookData();
        Objects.requireNonNull(bookEntries, ExceptionMessage.NULL_BOOK_ENTRIES.getExceptionMessage());

        StringBuilder searchOutput = createSearchOutput(bookEntries);

        if (searchOutput.length() == 0) {
            System.out.printf("No hits found for search term: %s\n", commandArgument);
        } else {
            System.out.println(searchOutput);
        }
    }

    /**
     * Create the search output by searching through the titles of all books and checking
     * if any of the titles match the search parameter ignoring the case.
     *
     * @param books List of books from which the search output is created.
     * @return StringBuilder containing the search output.
     */
    private StringBuilder createSearchOutput(List<BookEntry> books) {
        Objects.requireNonNull(books, ExceptionMessage.NULL_BOOKS.getExceptionMessage());

        StringBuilder output = new StringBuilder();

        // iterates through the books, and if a book title contains the commandArgument (ignoring the case),
        // then the title gets appended to consoleOutput
        for (BookEntry book : books) {
            if (book == null) {
                continue;
            }
            String titleLowerCase = book.getTitle().toLowerCase();
            String argumentLowerCase = commandArgument.toLowerCase();
            if (titleLowerCase.contains(argumentLowerCase)) {
                String title = book.getTitle();
                output.append(title).append("\n");
            }
        }

        return output;
    }

    /**
     * Parse the given command argument and assigns it to the instance variable.
     *
     * @param argumentInput Command argument that follows the search command, which is a specific search value.
     * @return True if the argument is a single word (no white spaces in between) and is not entirely blank.
     * @throws NullPointerException If the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        Objects.requireNonNull(argumentInput, ExceptionMessage.NULL_ARGUMENT.getExceptionMessage());

        // checks that the search value is a single word and is not entirely blank
        if ((argumentInput.trim().contains(COMMAND_ARGUMENT_DELIMITER) || (argumentInput.isBlank()))) {
            return false;
        }

        commandArgument = argumentInput;

        return true;
    }
}
