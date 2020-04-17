import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Add command used to add additional books to the library from a book data csv file.
 */
public class AddCmd extends LibraryCommand {

    /**
     * File extension that a file must have for the book data to be loaded.
     */
    private static final String FILE_EXTENSION = ".csv";

    /**
     * Path of the book data csv file currently being loaded.
     */
    private Path bookPath;

    /**
     * Create an add command.
     *
     * @param argumentInput Command argument
     * @throws IllegalArgumentException If given arguments are invalid.
     * @throws NullPointerException     If the given argumentInput is null.
     */
    public AddCmd(String argumentInput) {
        super(CommandType.ADD, argumentInput);
    }

    /**
     * Execute the add command. This method calls the given LibraryData instance’s loadData
     * in order to load the book data.
     *
     * @param data book data to be considered for command execution.
     * @throws NullPointerException If data is null.
     */
    @Override
    public void execute(LibraryData data) {
        Objects.requireNonNull(data, ExceptionMessage.NULL_DATA.getExceptionMessage());

        data.loadData(bookPath);
    }

    /**
     * Parse the given command argument and assigns it to the instance variable.
     *
     * @param argumentInput Path to a book data csv file, which could include subfolders or only a file name.
     * @return True if the argument is a valid path that indicates a file name which ends with ".csv".
     * @throws NullPointerException If the given argumentInput is null.
     */
    @Override
    protected boolean parseArguments(String argumentInput) {
        Objects.requireNonNull(argumentInput, ExceptionMessage.NULL_ARGUMENT.getExceptionMessage());

        if (!argumentInput.endsWith(FILE_EXTENSION)) {
            return false;
        }

        bookPath = Paths.get(argumentInput);
        return true;
    }
}
