/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */
public class FoxHoundUtils {

    // ATTENTION: Changing the following given constants can 
    // negatively affect the outcome of the auto grading!

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 8;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    // HINT Write your own constants here to improve code readability ...

    /** Number of foxes in a game */
    public static final int NUM_FOX = 1;

    /** Check if a figure is on a black square or not
     *
     * @param line the x coordinate of the given figure
     * @param column the y coordinate of the given figure
     * @return a boolean value indicating if the figure is on a black square or not
     */
    public static boolean isBlackSquare(int line, int column) {
        return (line % 2) != (column % 2);
    }

    /**  Convert a String position of a figure to cartesian coordinates
     *
     * @param position the string corresponding to the given position
     * @return an array of Integers containing the cartesian coordinates of the given position
     * @throws IllegalArgumentException if the given position is invalid
     */
    public static int[] convertToCoordinates(String position) {

        //checks that the given string is in the correct format
        if (!(position.matches("[A-Z][0-9]{1,2}"))) {
            throw new IllegalArgumentException("Given position invalid: " + position);
        }

        char letter_x = position.charAt(0);
        int x = letter_x - 'A';

        String number_y = position.replaceAll("\\D+", "");
        int y = Integer.parseInt(number_y) - 1;

        return new int[] {x, y};
    }

    /** Convert cartesian coordinates of a figure to a String position
     *
     * @param x the corresponding x coordinate
     * @param y the corresponding y coordinate
     * @return a String corresponding to the position of the figure
     */
    public static String coordinatesToString(int x, int y) {

        String letter_x = Character.toString((char)((int)'A' + x));
        int new_y = y+1;

        return letter_x + new_y;
    }

    /** Create a single String array for all figures and fills it with their starting positions
     *
     * @param dimension the dimension of the board
     * @return a String array filled with the starting positions of all the figures
     * @throws IllegalArgumentException if the given dimension is invalid
     */
    public static String[] initialisePositions(int dimension) {

        //checks that the dimension is valid
        if (dimension < MIN_DIM || dimension > MAX_DIM) {
            throw new IllegalArgumentException("Given dimension invalid: " + dimension);
        }

        int num_hounds = dimension / 2;
        String[] players = new String[num_hounds + NUM_FOX];

        //initialises the position of the hounds
        String value_hound;
        int n = 0;
        int i = 1;
        while (n < players.length - 1) {
            value_hound = FoxHoundUtils.coordinatesToString(i, 0);
            players[n] = value_hound;
            n++;
            i += 2;
        }

        //initialises the position of the fox
        int fox_column;
        if (dimension % 2 != 0) {
            fox_column = dimension / 2 + 1;
        } else {
            fox_column = dimension / 2;
        }

        if (!(isBlackSquare(fox_column, dimension))) {
            fox_column ++;
        }

        String fox_position = coordinatesToString(fox_column - 1, dimension - 1);
        players[players.length - 1] = fox_position;

        return players;

    }

    /** Check win conditions of the fox
     *
     * @param foxPos the fox position
     * @return a boolean value indicating if the fox has won or not
     * @throws IllegalArgumentException if the position of the fox is invalid
     */
    public static boolean isFoxWin(String foxPos) {

        //checks that the position of the fox is in the correct format
        if (!(foxPos.matches("[A-Z][0-9]{1,2}"))) {
            throw new IllegalArgumentException("Given position of the fox invalid: " + foxPos);
        }

        int fox_y = convertToCoordinates(foxPos)[1];
        return fox_y == 0;
    }

    /** Check win conditions of the hounds
     *
     * @param players the corresponding player positions
     * @param dimension the dimension of the board
     * @return a boolean value indicating if the hounds have won or not
     * @throws IllegalArgumentException if the given dimension is not valid
     * @throws IllegalArgumentException if the array containing the player positions doesn't have the correct length
     * @throws IllegalArgumentException if one of the figures is not on a black square
     * @throws IllegalArgumentException if one of the figures is not on the grid
     * @throws NullPointerException if the array containing the player positions is empty
     */
    public static boolean isHoundWin(String[] players, int dimension) {

        //checks that the dimension is valid
        if (dimension < MIN_DIM || dimension > MAX_DIM) {
            throw new IllegalArgumentException("Given dimension invalid: " + dimension);
        }

        //checks that the players array is not empty
        if (players == null) {
            throw new NullPointerException("Given players array is empty");
        }

        //checks that the players array has the correct length
        if (players.length != dimension / 2 + NUM_FOX) {
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

        String fox_pos = players[players.length - 1];

        int fox_pos_x = convertToCoordinates(fox_pos)[0];
        int fox_pos_y = convertToCoordinates(fox_pos)[1];

        String dest1 = coordinatesToString(fox_pos_x - 1, fox_pos_y - 1);
        String dest2 = coordinatesToString(fox_pos_x + 1, fox_pos_y - 1);
        String dest3 = coordinatesToString(fox_pos_x - 1, fox_pos_y + 1);
        String dest4 = coordinatesToString(fox_pos_x + 1, fox_pos_y + 1);

        if (fox_pos_x > 0 && fox_pos_y > 0) {
            if (isValidMove(dimension, players, FOX_FIELD, fox_pos, dest1)) {return false;}
        }
        if (fox_pos_x < dimension - 1 && fox_pos_y > 0) {
            if (isValidMove(dimension, players, FOX_FIELD, fox_pos, dest2)) {return false;}
        }
        if (fox_pos_x > 0 && fox_pos_y < dimension - 1) {
            if (isValidMove(dimension, players, FOX_FIELD, fox_pos, dest3)) {return false;}
        }
        if (fox_pos_x < dimension - 1  && fox_pos_y < dimension - 1) {
            if (isValidMove(dimension, players, FOX_FIELD, fox_pos, dest4)) {return false;}
        }

        return true;
    }

    /** Check if given coordinates constitute a valid move
     *
     * @param dim the dimension of the board
     * @param players the corresponding player positions
     * @param figure the figure type that is moving
     * @param origin the origin position of the moving figure
     * @param dest the destination position of the moving figure
     * @return a boolean value indicating if the corresponding move is valid or not
     * @throws IllegalArgumentException if the given dimension is not valid
     * @throws IllegalArgumentException if the array containing the player positions doesn't have the correct length
     * @throws IllegalArgumentException if one of the figures is not on a black square
     * @throws IllegalArgumentException if one of the figures is not on the grid
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws IllegalArgumentException if the given origin position is invalid
     * @throws IllegalArgumentException if the given destination position is invalid
     * @throws NullPointerException if the array containing the player positions is empty
     */
    public static boolean isValidMove(int dim, String[] players, char figure, String origin, String dest) {

        //checks that the dimension is valid
        if (dim < MIN_DIM || dim > MAX_DIM) {
            throw new IllegalArgumentException("Given dimension invalid: " + dim);
        }

        //checks that the players array is not empty
        if (players == null) {
            throw new NullPointerException("Given players array is empty");
        }

        //checks that the players array has the correct length
        if (players.length != dim / 2 + NUM_FOX) {
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
            if (x < 0 || x > dim - 1 || y < 0 || y > dim - 1) {
                if (i < players.length - 1) {
                    throw new IllegalArgumentException("Given position of one of the hounds not on the grid: " + players[i]);
                } else {
                    throw new IllegalArgumentException("Given position of the fox not on the grid: " + players[i]);
                }
            }
        }

        //checks that the given figure is either a fox or a hound
        if (figure != FoxHoundUtils.FOX_FIELD && figure != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figure);
        }

        //checks that the origin is a valid position
        if (!(origin.matches("[A-Z][0-9]{1,2}"))) {
            throw new IllegalArgumentException("Given origin position is invalid: " + origin);
        }

        //checks that the destination is a valid position
        if (!(dest.matches("[A-Z][0-9]{1,2}"))) {
            throw new IllegalArgumentException("Given destination is invalid: " + dest);
        }

        int origin_x = convertToCoordinates(origin)[0];
        int origin_y = convertToCoordinates(origin)[1];
        int dest_x = convertToCoordinates(dest)[0];
        int dest_y = convertToCoordinates(dest)[1];

        // checks that it is within 1 diagonal from the origin, and that the hounds only move forward
        if (figure == HOUND_FIELD) {
            if (!(((dest_x == origin_x + 1) || (dest_x == origin_x - 1)) && (dest_y == origin_y + 1))) {return false;}
        } else {
            if (!(((dest_x == origin_x + 1) || (dest_x == origin_x -1)) && ((dest_y == origin_y - 1) || (dest_y == origin_y + 1)))) {return false;}
        }

        //checks that the origin and destination are on a black square
        if (!isBlackSquare(origin_x, origin_y)) {return false;}
        if (!isBlackSquare(dest_x, dest_y)) {return false;}

        //checks that the origin and destination coordinates are actually in the grid
        if ((origin_x < 0) || (origin_x > dim - 1)) {return false;}
        if ((origin_y < 0) || (origin_y > dim - 1)) {return false;}
        if ((dest_x < 0) || (dest_x > dim - 1)) {return false;}
        if ((dest_y < 0) || (dest_y > dim - 1)) {return false;}

        // checks that the destination field is not taken by another figure
        for (String player : players) {
            if (player.equals(dest)) {
                return false;
            }
        }

        // check that the origin is actually occupied by the given char
        if (figure == HOUND_FIELD) {
            for (int i = 0; i < players.length - 1; i++) {
                if (players[i].equals(origin)) {return true;}
            }
        } else {
            if (players[players.length - 1].equals(origin)) {return true;}
        }

        return false;
    }

}
