import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Objects;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 4;
    /** Main menu display string. */
    private static final String MAIN_MENU =
        "\n1. Move\n2. Save\n3. Load\n4. Exit\n\nEnter 1 - 2 - 3 - 4:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    /** Menu entry to save the program. */
    public static final int MENU_SAVE = 2;
    /** Menu entry to load a program. */
    public static final int MENU_LOAD = 3;
    /** Menu entry to terminate the program. */
    public static final int MENU_EXIT = 4;

    /**
     * Print a graphical representation of the game board and corresponding player positions to the console.
     *
     * @param players the corresponding player positions
     * @param dimension the dimension of the board
     * @throws IllegalArgumentException if the given dimension is not valid
     * @throws IllegalArgumentException if the array containing the player positions doesn't have the correct length
     * @throws IllegalArgumentException if one of the figures is not on a black square
     * @throws IllegalArgumentException if one of the figures is not on the grid
     * @throws NullPointerException if the array containing the player positions is empty
     */
    public static void displayBoard(String[] players, int dimension) {

        //checks that the dimension is valid
        if (dimension < FoxHoundUtils.MIN_DIM || dimension > FoxHoundUtils.MAX_DIM) {
            throw new IllegalArgumentException("Given dimension invalid: " + dimension);
        }

        //checks that the players array is not empty
        if (players == null) {
            throw new NullPointerException("Given players array is empty");
        }

        //checks that the players array has the correct length
        if (players.length != dimension / 2 + FoxHoundUtils.NUM_FOX) {
            throw new IllegalArgumentException("Given length of players array is incorrect: " + players.length);
        }

        //checks that the fox and the hounds are on a black square and that their coordinates are valid
        for (int i = 0; i < players.length; i++) {
            int x = FoxHoundUtils.convertToCoordinates(players[i])[0];
            int y = FoxHoundUtils.convertToCoordinates(players[i])[1];
            //checks that the figures are on a black square
            if (!FoxHoundUtils.isBlackSquare(x,y)) {
                if (i < players.length - 1) {
                    throw new IllegalArgumentException("Given position of one of the hounds not on a black square: " + players[i]);
                } else {
                    throw new IllegalArgumentException("Given position of the fox not on a black square: " + players[i]);
                }
            }
            //checks that the coordinates are valid
            if (x < 0 || x > dimension - 1 || y < 0 || y > dimension - 1) {
                if (i < players.length - 1) {
                    throw new IllegalArgumentException("Given position of one of the hounds not on the grid: " + players[i]);
                } else {
                    throw new IllegalArgumentException("Given position of the fox not on the grid: " + players[i]);
                }
            }
        }

        // Prints ABCDEF...
        System.out.print(" ".repeat(2));
        for (int i = 0; i < dimension; i++) {
            char letter = FoxHoundUtils.coordinatesToString(i, 3).charAt(0); //3 is just a random number as we don't care about it
            System.out.print(letter);
        }
        System.out.print("  ");
        System.out.println();
        System.out.println();

        //displays the board
        //initialises the board with dots everywhere
        String[][] display = new String[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                display[j][i] = ".";
            }
        }
        // displays the hounds and the fox
        for (int i = 0; i < players.length; i++) {
            int x = FoxHoundUtils.convertToCoordinates(players[i])[0];
            int y = FoxHoundUtils.convertToCoordinates(players[i])[1];
            if (i != players.length - 1) {display[y][x] = Character.toString(FoxHoundUtils.HOUND_FIELD);}
            else {display[y][x] = Character.toString(FoxHoundUtils.FOX_FIELD);}
        }
        //prints the board
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j==0 && dimension >= 10 && i < 9) {
                    System.out.print("0" + (i+1) + " " + display[i][j]);
                } else if (j==0) {
                    System.out.print((i+1) + " " + display[i][j]);
                } else if ((j == dimension - 1) && dimension >= 10 && i < 9) {
                    System.out.println(display[i][j] + " 0" + (i+1));
                } else if (j == dimension - 1) {
                    System.out.println(display[i][j] + " " + (i+1));
                } else {
                    System.out.print(display[i][j] + "");
                }
            }
        }
        System.out.println();

        // Prints ABCDEF...
        System.out.print(" ".repeat(2));
        for (int i = 0; i < dimension; i++) {
            char letter = FoxHoundUtils.coordinatesToString(i, 3).charAt(0); //number 3 is just a random number as we don't care about y
            System.out.print(letter);
        }
    }

    /**
     * Print the main menu and query the user for an entry selection.
     * 
     * @param figureToMove the figure type that has the next move
     * @param stdin a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD 
         && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure = 
            figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    /**
     * Query the user for origin and destination coordinates for the next move.
     *
     * @param dim the dimension of the board
     * @param test_in a Scanner object to read user input from
     * @return a String array containing the origin and destination coordinates for the next move
     * @throws IllegalArgumentException if the given dimension is invalid
     * @throws NullPointerException if the given Scanner is null
     */
    public static String[] positionQuery(int dim, Scanner test_in) {

        //checks that the given Scanner is non null
        Objects.requireNonNull(test_in, "Given Scanner must not be null");

        //checks the dimension is valid
        if (dim < FoxHoundUtils.MIN_DIM || dim > FoxHoundUtils.MAX_DIM) {
            throw new IllegalArgumentException("Given dimension invalid: " + dim);
        }

        char letter = (char) ('A' + dim -1);
        System.out.println("Provide origin and destination coordinates.\n" +
                "Enter two positions between A1-" + letter + dim + ":");

        int n = 0;
        String[] array_inputs = new String[2];

        while (n==0) {
            String input_line = test_in.nextLine();
            array_inputs = input_line.split(" ");

            if (!input_line.matches("[A-Z][0-9]{1,2} [A-Z][0-9]{1,2}")) {
                System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
                System.out.println();
                System.out.println("Provide origin and destination coordinates.\n" +
                        "Enter two positions between A1-" + letter + dim + ":");
                continue;
            }

            String origin = array_inputs[0];
            String dest = array_inputs[1];

            int origin_x = FoxHoundUtils.convertToCoordinates(origin)[0];
            int origin_y = FoxHoundUtils.convertToCoordinates(origin)[1];
            int dest_x = FoxHoundUtils.convertToCoordinates(dest)[0];
            int dest_y = FoxHoundUtils.convertToCoordinates(dest)[1];

            boolean are_valid_coordinates = (0 <= origin_x) && (origin_x <= dim - 1) &&
                    (0 <= origin_y) && (origin_y <= dim - 1) &&
                    (0 <= dest_x) && (dest_x <= dim - 1) &&
                    (0 <= dest_y) && (dest_y <= dim - 1);

            if (are_valid_coordinates) {
                n = 1;
            } else {
                System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
                System.out.println();
            }
        }
            return array_inputs;
    }

    /**
     * Query the user for the path of the file that is desired to be saved or loaded.
     *
     * @param test_in a Scanner object to read user input from
     * @return the path of the desired file in the Path format
     * @throws NullPointerException if the given Scanner is null
     */
    public static Path fileQuery(Scanner test_in) {

        //checks that the given Scanner is not null
        Objects.requireNonNull(test_in, "Given Scanner must not be null");

        System.out.println("Enter file path:");
        String input = test_in.nextLine();
        return Paths.get(input);
    }
}