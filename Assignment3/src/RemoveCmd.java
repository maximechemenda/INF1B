import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RemoveCmd extends LibraryCommand {

    /**
     * All available REMOVE command arguments.
     */
    private enum RemoveCommandArgument {
        TITLE,
        AUTHOR
    }

    /**
     * Separator between the two parts of the command argument.
     */
    private static final String ARGUMENT_SEPARATOR = " ";

    /**
     * First command argument that follows the REMOVE command which is currently being
     * executed, either TITLE or AUTHOR.
     */
    private RemoveCommandArgument firstCommandArgument;

    /**
     * Second command argument that follows the REMOVE command which is currently being
     * executed, either a title or an author.
     */
    private String secondCommandArgument;

    /**
     * create a remove command.
     *
     * @param argumentInput Command argument
     * @throws IllegalArgumentException if given arguments are invalid
     * @throws NullPointerException     if the given argumentInput is null.
     */
    public RemoveCmd(String argumentInput) {
        super(CommandType.REMOVE, argumentInput);
    }

    /**
     * Execute the remove command. This method evaluates if the user is removing by title or author,
     * goes through all book entries in the library and removes any matches with the given remove
     * value by the user. It prints a confirming message.
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

        // removes the required books depending on whether the user chose to remove by title or author
        switch (firstCommandArgument) {
            case TITLE:
                removeByTitle(bookEntries);
                break;
            case AUTHOR:
                removeByAuthor(bookEntries);
                break;
            default:
                throw new IllegalArgumentException(String.format("Corresponding first part of command" +
                        " argument should be either TITLE or AUTHOR: %s", firstCommandArgument));
        }
    }

    /**
     * Go through the given books and removes any book which title is equal to the input value of the user.
     *
     * @param books List of books from which a possible book can be removed.
     */
    private void removeByTitle(List<BookEntry> books) {
        Objects.requireNonNull(books, ExceptionMessage.NULL_BOOKS.getExceptionMessage());

        int removedBooks = 0;
        Iterator<BookEntry> titleIterator = books.iterator();

        // iterates through the books, and removes a book if its title is equal to secondArgument
        while (titleIterator.hasNext()) {
            BookEntry book = titleIterator.next();
            if (book == null) {
                continue;
            }
            String title = book.getTitle();
            if (title.equals(secondCommandArgument)) {
                titleIterator.remove();
                removedBooks++;
                System.out.printf("%s: removed successfully.\n", title);
                break;
            }
        }
        if (removedBooks == 0) {
            System.out.printf("%s: not found.\n", secondCommandArgument);
        }
    }

    /**
     * Go through the given books and removes any book that has the same author as the input value of the user.
     *
     * @param books list of books from which possible books can be removed.
     */
    private void removeByAuthor(List<BookEntry> books) {
        Objects.requireNonNull(books, ExceptionMessage.NULL_BOOKS.getExceptionMessage());

        int removedBooks = 0;
        Iterator<BookEntry> authorIterator = books.iterator();

        // iterates through the books, and removes a book if one of its authors is equal to secondArgument
        while (authorIterator.hasNext()) {
            BookEntry book = authorIterator.next();
            if (book == null) {
                continue;
            }
            String[] authors = book.getAuthors();
            if (Arrays.asList(authors).contains(secondCommandArgument)) {
                authorIterator.remove();
                removedBooks++;
            }
        }

        System.out.printf("%d books removed for author: %s\n", removedBooks, secondCommandArgument);
    }

    /**
     * Parse the given command argument and assigns it to the instance variables.
     *
     * @param argumentInput Two part command argument that follows the remove command. The first part
     *                      indicates which parameter should be considered for removal. This can
     *                      either be AUTHOR or TITLE. The second parameter indicates a value
     *                      which is either a full title or full author name.
     * @return True if the argument is either AUTHOR or TITLE followed by a string value, and if this
     * value is not entirely blank.
     * @throws NullPointerException If the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        Objects.requireNonNull(argumentInput, ExceptionMessage.NULL_ARGUMENT.getExceptionMessage());

        if (!argumentInput.contains(ARGUMENT_SEPARATOR)) {
            return false;
        }

        // first part of the argument input
        String firstPart = argumentInput.substring(0, argumentInput.indexOf(ARGUMENT_SEPARATOR));
        RemoveCommandArgument firstCommandArgument = parseRemoveCommandArgument(firstPart);

        // second part of the argument input
        String secondCommandArgument = argumentInput.substring((argumentInput.indexOf(ARGUMENT_SEPARATOR) + 1));

        if (firstCommandArgument == null) {
            return false;
        }

        if (secondCommandArgument.isBlank()) {
            return false;
        }

        // checks if firstCommandArgument is either AUTHOR or TITLE, and returns false if not
        switch (firstCommandArgument) {
            case AUTHOR:
            case TITLE:
                break;
            default:
                return false;
        }

        this.firstCommandArgument = firstCommandArgument;
        this.secondCommandArgument = secondCommandArgument;

        return true;
    }

    /**
     * Translate given command keyword to corresponding RemoveCommandArgument.
     *
     * @param inputArgument command keyword
     * @return RemoveCommandArgument associated with given keyword or null if no
     * association was found.
     */
    private RemoveCommandArgument parseRemoveCommandArgument(String inputArgument) {
        // iterates through the values of the enum, and returns the one which is associated with inputArgument
        for (RemoveCommandArgument argument : RemoveCommandArgument.values()) {
            if (argument.name().equals(inputArgument)) {
                return argument;
            }
        }

        return null; // returns null if no association has been found
    }
}
