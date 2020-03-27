import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

/** 
 * The Main class of the fox hound program.
 * 
 * It contains the main game loop where main menu interactions
 * are processed and handler functions are called.
  */
public class FoxHoundGame {

    /**
     * This scanner can be used by the program to read from
     * the standard input.
     * <p>
     * Every scanner should be closed after its use, however, if you do
     * that for StdIn, it will close the underlying input stream as well
     * which makes it difficult to read from StdIn again later during the
     * program.
     * <p>
     * Therefore, it is advisable to create only one Scanner for StdIn
     * over the course of a program and only close it when the program
     * exits. Additionally, it reduces complexity.
     */
    private static final Scanner STDIN_SCAN = new Scanner(System.in);

    /**
     * Swap between fox and hounds to determine the next
     * figure to move.
     *
     * @param currentTurn last figure to be moved
     * @return next figure to be moved
     */
    private static char swapPlayers(char currentTurn) {
        if (currentTurn == FoxHoundUtils.FOX_FIELD) {
            return FoxHoundUtils.HOUND_FIELD;
        } else {
            return FoxHoundUtils.FOX_FIELD;
        }
    }

    /**
     * The main loop of the game. Interactions with the main
     * menu are interpreted and executed here.
     *
     * @param dim     the dimension of the game board
     * @param players current position of all figures on the board in board coordinates
     */
    private static void gameLoop(int dim, String[] players) {
        // start each game with the Fox
        char turn = FoxHoundUtils.FOX_FIELD;
        boolean exit = false;
        while (!exit) {
            System.out.println("\n#################################");
            FoxHoundUI.displayBoard(players, dim);
            System.out.println();
            System.out.println(Arrays.toString(players));
            System.out.println();

            // handle menu choice
            int choice = FoxHoundUI.mainMenuQuery(turn, STDIN_SCAN);
            switch (choice) {
                case FoxHoundUI.MENU_MOVE:
                    int n = 0;
                    while (n==0) {
                        String[] origin_and_dest = FoxHoundUI.positionQuery(dim, STDIN_SCAN);

                        // handle menu choice
                        if (FoxHoundUtils.isValidMove(dim, players, turn, origin_and_dest[0], origin_and_dest[1])) {
                            for (int i = 0; i < players.length; i++) {
                                if (players[i].equals(origin_and_dest[0])) {
                                    players[i] = origin_and_dest[1];
                                }
                            }
                            n = 1;
                        } else {
                            System.err.println("ERROR: Invalid move. Try again!");
                            System.out.println();
                        }
                    }

                    if (FoxHoundUtils.isFoxWin(players[players.length - 1])) {
                        System.out.println("The Fox wins!");
                        exit = true;
                    }

                    if (FoxHoundUtils.isHoundWin(players, dim)) {
                        System.out.println("The Hounds win!");
                        exit = true;
                    }

                    turn = swapPlayers(turn);
                    break;
                case FoxHoundUI.MENU_SAVE:
                    Path save_path = FoxHoundUI.fileQuery(STDIN_SCAN);
                    char nextMove = swapPlayers(turn);
                    boolean is_saved = FoxHoundIO.saveGame(players, nextMove, save_path);
                    if (!is_saved) {
                        System.err.println("ERROR: Saving file failed.");
                    }
                    break;
                case FoxHoundUI.MENU_LOAD:
                    Path load_path = FoxHoundUI.fileQuery(STDIN_SCAN);
                    char figure = FoxHoundIO.loadGame(players, load_path);
                    if (!(figure == FoxHoundUtils.FOX_FIELD || figure == FoxHoundUtils.HOUND_FIELD)) {
                        System.err.println("ERROR: Loading from file failed");
                    }
                    break;
                case FoxHoundUI.MENU_EXIT:
                    exit = true;
                    break;
                default:
                    System.err.println("ERROR: invalid menu choice: " + choice);
            }

        }
    }

    /**
     * Entry method for the Fox and Hound game.
     * <p>
     * The dimensions of the game board can be passed in as
     * optional command line argument.
     * <p>
     * If no argument is passed, a default dimension of
     * {@value FoxHoundUtils#DEFAULT_DIM} is used.
     * <p>
     * Dimensions must be between {@value FoxHoundUtils#MIN_DIM} and
     * {@value FoxHoundUtils#MAX_DIM}.
     *
     * @param args contain the command line arguments where the first can be
     *             board dimensions.
     */


    public static void main(String[] args) {

        int dimension = FoxHoundUtils.DEFAULT_DIM;
        if (args.length != 0) {
            if (args[0].matches("[0-9]{1,2}")) {
                if ((FoxHoundUtils.MIN_DIM <= Integer.parseInt(args[0])) && (Integer.parseInt(args[0]) <= FoxHoundUtils.MAX_DIM)) {
                    dimension = Integer.parseInt(args[0]);
                }
            }
        }

        String[] players = FoxHoundUtils.initialisePositions(dimension);

        gameLoop(dimension, players);
        // Close the scanner reading the standard input stream
        STDIN_SCAN.close();

    }
}
