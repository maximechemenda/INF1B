import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all file input / output
 * related operations such as saving and loading a game.
 */


public class FoxHoundIO {

    /** Load the content of the given file
     *
     * @param players the corresponding player positions
     * @param input the file name
     * @return a character indicating the next figure to be moved
     * @throws IllegalArgumentException if the length of the array containing the player positions is not 5
     * @throws IllegalArgumentException if a figure is not on a black square
     * @throws IllegalArgumentException if a figure is not on the grid
     * @throws NullPointerException if the given path is empty
     * @throws NullPointerException if the array containing the player positions is empty
     */
    public static char loadGame(String[] players, Path input) {

        //checks that the path is not empty
        if (input == null) {
            throw new NullPointerException("Given path empty");
        }

        //checks that the players array is not empty
        if (players == null) {
            throw new NullPointerException("Given players array empty");
        }

        //checks the length of the players is 5, as only 8x8 boards can be loaded, and the corresponding players array should have length 5
        if (players.length != 5) {
            throw new IllegalArgumentException("Given length of players array not equal to 5: " + players.length);
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
            if (x < 0 || x > 7 || y < 0 || y > 7) {
                if (i < players.length - 1) {
                    throw new IllegalArgumentException("Given position of one of the hounds not on the grid: " + players[i]);
                } else {
                    throw new IllegalArgumentException("Given position of the fox not on the grid: " + players[i]);
                }
            }
        }

        char d = 'b'; // b is just a random value
        File file = new File(String.valueOf(input));

        //checks that the file actually exists
        if (!file.exists()) {
            return '#';
        }

        try (BufferedReader br = Files.newBufferedReader(input, Charset.defaultCharset())) {

            String line = br.readLine();
            String[] parts;
            parts = line.split(" ");

            //checks that the line in the file matches the correct format
            //these values are chosen as only 8x8 grids can be saved or loaded, so it checks that the coordinates are within the right dimension
            if (!line.matches("[F|H] [A-J][0-8] [A-J][0-8] [A-J][0-8] [A-J][0-8] [A-J][0-8]")) {
                return '#';
            }

            //checks that the figures are on a black square
            for (int i = 1; i < parts.length; i++) {
                int x = FoxHoundUtils.convertToCoordinates(parts[i])[0];
                int y = FoxHoundUtils.convertToCoordinates(parts[i])[1];
                if (!FoxHoundUtils.isBlackSquare(x, y)) {
                    return '#';
                }
            }

            //checks that the figure is either a fox or a hound
            String figure = parts[0];
            if (!(figure.equals(Character.toString(FoxHoundUtils.FOX_FIELD)) || figure.equals(Character.toString(FoxHoundUtils.HOUND_FIELD)))) {
                return '#';
            } else {
                //updates the players array
                for (int i = 0; i < parts.length - 1; i++) {
                    players[i] = parts[i+1];
                }

                if (figure.equals(Character.toString(FoxHoundUtils.FOX_FIELD))) {
                    d = FoxHoundUtils.FOX_FIELD;
                } else {
                    d = FoxHoundUtils.HOUND_FIELD;
                }
            }

        } catch (IOException e) {
            System.err.print("ERROR: loading from file failed");
        }

        return d;
    }

    /** Save the game to a file with the given name
     *
     * @param players the corresponding player positions
     * @param nextMove the figure type that has the next move
     * @param saveFile the file name
     * @return a boolean value indicating whether the saving was successful or not
     * @throws IllegalArgumentException if the given file already exists
     * @throws IllegalArgumentException if the length of the array containing the player positions is not 5
     * @throws IllegalArgumentException if a figure is not on a black square
     * @throws IllegalArgumentException if a figure is not on the grid
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException if the given path is empty
     * @throws NullPointerException if the array containing the player positions is empty
     */
    public static boolean saveGame(String[] players, char nextMove, Path saveFile) {

        //checks that the path is not empty
        if (saveFile == null) {
            throw new NullPointerException("Given path empty");
        }

        //checks that no existing file is overwritten
        File file = new File(String.valueOf(saveFile));
        if (file.exists()) {
            throw new IllegalArgumentException("Given file already exists");
        }

        //checks that the players array is not empty
        if (players == null) {
            throw new NullPointerException("Given players array empty");
        }

        //checks the length of the players is 5, as only 8x8 boards can be loaded, and the corresponding players array should have length 5
        if (players.length != 5) {
            throw new IllegalArgumentException("Given length of players array not equal to 5: " + players.length);
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
            if (x < 0 || x > 7 || y < 0 || y > 7) {
                if (i < players.length - 1) {
                    throw new IllegalArgumentException("Given position of one of the hounds not on the grid: " + players[i]);
                } else {
                    throw new IllegalArgumentException("Given position of the fox not on the grid: " + players[i]);
                }
            }
        }

        //checks that the figure is either a fox or a hound
        if (nextMove != FoxHoundUtils.FOX_FIELD && nextMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + nextMove);
        }

        try (BufferedWriter bw = Files.newBufferedWriter(saveFile, Charset.defaultCharset())) {

            //writes the next move
            bw.write(nextMove);

            //writes the position of the players
            for (String value : players){
                bw.write(" " + value);
            }

            bw.flush();

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}







