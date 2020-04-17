import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/** 
 * Class responsible for loading
 * book data from file.
 */
public class LibraryFileLoader {

    /**
     * Separator between the data values on a single line of a book data file.
     */
    private static final String DATA_VALUES_SEPARATOR = ",";

    /**
     * Separator between the authors of a title in a book data file.
     */
    private static final String AUTHOR_SEPARATOR = "-";

    /**
     * Number of data values that are supposed to be present in a single line of a book data file.
     */
    private static final int DATA_VALUES_NUMBER = 5;

    /**
     * Contains all lines read from a book data file using
     * the loadFileContent method.
     * 
     * This field can be null if loadFileContent was not called
     * for a valid Path yet.
     * 
     * NOTE: Individual line entries do not include line breaks at the 
     * end of each line.
     */
    private List<String> fileContent;

    /** Create a new loader. No file content has been loaded yet. */
    public LibraryFileLoader() { 
        fileContent = null;
    }

    /**
     * Load all lines from the specified book data file and
     * save them for later parsing with the parseFileContent method.
     * 
     * This method has to be called before the parseFileContent method
     * can be executed successfully.
     * 
     * @param fileName file path with book data
     * @return true if book data could be loaded successfully, false otherwise
     * @throws NullPointerException if the given file name is null
     */
    public boolean loadFileContent(Path fileName) {
        Objects.requireNonNull(fileName, "Given filename must not be null.");
        boolean success = false;

        try {
            fileContent = Files.readAllLines(fileName);
            success = true;
        } catch (IOException | SecurityException e) {
            System.err.println("ERROR: Reading file content failed: " + e);
        }

        return success;
    }

    /**
     * Has file content been loaded already?
     * @return true if file content has been loaded already.
     */
    public boolean contentLoaded() {
        return fileContent != null;
    }

    /**
     * Parse file content loaded previously with the loadFileContent method.
     * 
     * @return books parsed from the previously loaded book data or an empty list
     * if no book data has been loaded yet.
     */
    public List<BookEntry> parseFileContent() {
        List<BookEntry> bookEntries = new ArrayList<>();

        if (contentLoaded()) {
            Iterator<String> lineIterator = fileContent.iterator();
            lineIterator.next(); // ignores first line of the file, as it is just a column header, not the actual data

            // iterates through each entry of fileContent and adds the corresponding BookEntry to bookEntries
            while (lineIterator.hasNext()) {
                try {
                    String line = lineIterator.next();
                    BookEntry book = createBookFromFile(line);
                    bookEntries.add(book);
                } catch (NullPointerException | IllegalArgumentException e) {
                    System.err.println("ERROR: a book wasn't loaded: " + e.getMessage());
                }
            }
        } else {
            System.err.println("ERROR: No content loaded before parsing.");
        }

        return bookEntries;
    }

    /**
     * Create a BookEntry from a line of the file being loaded.
     *
     * @param line Line in the file used to create the BookEntry.
     * @return BookEntry created from given line.
     * @throws NullPointerException if given line is null.
     * @throws IllegalArgumentException If the book entry line is not formatted as expected
     */
    private BookEntry createBookFromFile(String line) {
        Objects.requireNonNull(line, "Given line must not be null");

        String[] lineContent = line.split(DATA_VALUES_SEPARATOR);

        if (lineContent.length != DATA_VALUES_NUMBER) {
            throw new IllegalArgumentException(String.format("Given book entry line not correctly formed: %s", line));
        }

        String title = lineContent[0];
        String[] authors = lineContent[1].split(AUTHOR_SEPARATOR);
        float rating = Float.parseFloat(lineContent[2]);
        String ISBN = lineContent[3];
        int pages = Integer.parseInt(lineContent[4]);

        return new BookEntry(title, authors, rating, ISBN, pages);
    }
}
