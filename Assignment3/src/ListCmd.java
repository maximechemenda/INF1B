import java.util.List;
import java.util.Objects;

public class ListCmd extends LibraryCommand {

    /**
     * All available LIST command arguments.
     */
    private enum ListCommandArgument {
        LONG("long"),
        SHORT("short"),
        BLANK("");

        /**
         * List command argument as the user should input it.
         */
        private String listCommandArgument;

        /**
         * Create a List Command Argument.
         *
         * @param listCommandArgument List command argument.
         */
        private ListCommandArgument(String listCommandArgument) {
            this.listCommandArgument = listCommandArgument;
        }

        /**
         * Get the list command argument.
         *
         * @return List command argument.
         */
        public String getListCommandArgument() {
            return listCommandArgument;
        }
    }

    /**
     * Command argument that follows the LIST command which is currently executed.
     */
    private ListCommandArgument commandArgument;

    /**
     * Create a list command.
     *
     * @param argumentInput Command argument
     * @throws IllegalArgumentException If given arguments are invalid
     * @throws NullPointerException     If the given argumentInput is null.
     */
    public ListCmd(String argumentInput) {
        super(CommandType.LIST, argumentInput);
    }

    /**
     * Execute the list command. This method iterates through the list of currently loaded books
     * from the LibraryData parameter, and prints each book entry to the console. If the user selected
     * short print (or default by giving a blank argument), only book titles will be printed. If the user
     * selected long print, all information in the format specified for BookEntries toString method will be printed.
     *
     * @param data Book data to be considered for command execution.
     * @throws NullPointerException     If data is null.
     * @throws NullPointerException     If bookEntries (the loaded books) is null.
     * @throws IllegalArgumentException If the command argument is not as expected.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, ExceptionMessage.NULL_DATA.getExceptionMessage());

        List<BookEntry> bookEntries = data.getBookData();
        Objects.requireNonNull(bookEntries, ExceptionMessage.NULL_BOOK_ENTRIES.getExceptionMessage());

        StringBuilder consoleOutput;

        if (bookEntries.isEmpty()) {
            System.out.println("The library has no book entries.");
        } else {
            // appends to consoleOutput the information to print depending on how the user chose to list the books
            switch (commandArgument) {
                case SHORT:
                case BLANK:
                    consoleOutput = createShortOutput(bookEntries);
                    break;
                case LONG:
                    consoleOutput = createLongOutput(bookEntries);
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Given command argument" +
                            " should be either long, short, or blank: %s", commandArgument));
            }

            // deletes the extra empty line at the end of consoleOutput
            int lastLineIndex = consoleOutput.lastIndexOf("\n");
            if (lastLineIndex >= 0) {
                consoleOutput.delete(lastLineIndex, consoleOutput.length());
            }

            System.out.println(consoleOutput);
        }
    }

    /**
     * Create a StringBuilder containing all the book titles in books.
     *
     * @param books List of books used to create the output.
     * @return StringBuilder containing the short output.
     * @throws NullPointerException If books is null.
     */
    private StringBuilder createShortOutput(List<BookEntry> books) {
        Objects.requireNonNull(books, ExceptionMessage.NULL_BOOKS.getExceptionMessage());

        StringBuilder output = new StringBuilder();
        output.append(String.format("%d books in library:\n", books.size()));

        // appends to consoleOutput the book title of all the books in libraryBooks
        for (BookEntry book : books) {
            if (book == null) {
                continue;
            }
            output.append(book.getTitle()).append("\n");
        }

        return output;
    }


    /**
     * Create a StringBuilder containing all the information about all the
     * books in the format specified for BookEntries toString method.
     *
     * @param books of books used to create the output.
     * @return StringBuilder containing the long output.
     * @throws NullPointerException If books is null.
     */
    private StringBuilder createLongOutput(List<BookEntry> books) {
        Objects.requireNonNull(books, ExceptionMessage.NULL_BOOKS.getExceptionMessage());

        StringBuilder output = new StringBuilder();
        output.append(String.format("%d books in library:\n", books.size()));

        // appends to output all the information about all the books in books
        for (BookEntry book : books) {
            if (book == null) {
                continue;
            }
            output.append(book).append("\n\n");
        }

        return output;
    }

    /**
     * Parse the given command argument and assigns it to the instance variable.
     *
     * @param argumentInput Command argument that follows the LIST command
     * @return true if the argument is either short, long or blank.
     * @throws NullPointerException If the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        Objects.requireNonNull(argumentInput, ExceptionMessage.NULL_ARGUMENT.getExceptionMessage());

        ListCommandArgument commandArgument = parseListCommandArgument(argumentInput);

        if (commandArgument == null) {
            return false;
        }

        // checks if commandArgument is either BLANK, SHORT, or LONG, and returns false if not
        switch (commandArgument) {
            case BLANK:
            case SHORT:
            case LONG:
                break;
            default:
                return false;
        }

        this.commandArgument = commandArgument;

        return true;
    }

    /**
     * Translate given command keyword to corresponding ListCommandArgument.
     *
     * @param inputArgument Command keyword.
     * @return ListCommandArgument associated with given keyword or null if no
     * association was found.
     */
    private ListCommandArgument parseListCommandArgument(String inputArgument) {
        if (inputArgument.isBlank()) {
            return ListCommandArgument.BLANK;
        }

        // iterates through the values of the enum, and returns the one which is associated with inputArgument
        for (ListCommandArgument argument : ListCommandArgument.values()) {
            String displayCommandArgument = argument.getListCommandArgument();
            if (displayCommandArgument.equals(inputArgument)) {
                return argument;
            }
        }

        return null; // returns null if no association has been found
    }
}
