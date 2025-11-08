import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// a Piece has a color and can flip 
class Piece {
    // let true be white, false be black
    public boolean color;

    public Piece(boolean color) {
        this.color = color;
    }

    // negate the color's current value to "flip" it
    public void flip() {
        this.color = !color;
    }
}
// a board has 2d array of Pieces and can be printed
// the gameplay mechanics are played "on" a Board object
class Board {
    public Piece[][] board;
    
    public Board() {
        board = new Piece[8][8];
    }

    // a method to represent the board's current state
    public void printBoard() {
        System.out.println("  A   B   C   D   E   F   G   H");
        System.out.println("---------------------------------");
        for (int j = 0; j < board.length; j++) {
            for (int k = 0; k < board[j].length; k++) {
                System.out.print("| ");
                // not empty space
                if (board[j][k] != null) {
                    if (board[j][k].color) {
                        System.out.print("W ");
                    }
                    else {
                        System.out.print("B ");
                    }
                }
                // empy space (null piece)
                else {
                    System.out.print(". ");
                }
            }
            System.out.print("|");
            System.out.println(" " + (j + 1) + " ");
            System.out.println("---------------------------------");
        }
    }

    // reference: https://stackoverflow.com/questions/10241217/how-to-clear-console-in-java
    // a method to clear the screen (pls allow this is just something dumb)
    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // TODO: implement
    private boolean checkMoveLegality(int row, int col, Piece piece) {
        // check if the space is already occupied
        if (board[row][col] != null) {
            return false;   
        }
        // grab the color of the piece we want to place
        boolean color = piece.color;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // no need to check the spot we currently want to place at in this point in the sequencing
                if (i == 0 && j == 0) {
                    continue;
                }
                // if any of these conditions are true, we are not at a valid square on the board and need to ignore this one
                // continue keyword skips to the next j iteration 
                if ((row + i) < 0 || (row + i) >= board.length || (col + j) < 0 || (col + j) >= board.length) {
                    continue;
                }
                // check to make sure the current neighbor is occupied before we check color
                if (board[row + i][col + j] != null) {
                    // check this neighbor to see if it is an opponents color
                    if (board[row + i][col + j].color != color) {
                        // grab the indices so we can go further in whatever direction we need to
                        int adjRowIndex = row + i; 
                        int adjColIndex = col + j;
                        adjRowIndex += i;
                        adjColIndex += j;
                        List<Piece> list = new ArrayList<>();
                        // ensure we do not go out of bounds of the board. if we go out in any dimension, do not iterate further out
                        while (adjRowIndex >= 0 && adjColIndex >= 0 && adjRowIndex < board.length && adjColIndex < board.length) {
                            // go to the next neighbor space
                            Piece curPiece = board[adjRowIndex][adjColIndex];
                            // if null, space is empty and we dont care aboout going further down this path 
                            if (curPiece == null) {
                                break;
                            }
                            // we've found at least one path, so the move becomes legal
                            if (curPiece.color == color) {
                                return true;
                            }
                            // properly increment in the direction we are meant to walk in
                            // hitting this means we have only found opponent pieces thus far
                            adjRowIndex += i;
                            adjColIndex += j;
                        }
                    }
                }
            }
        }
        // never found a legal move
        return false;
    }

    // works under the assumption that the move is legal (ie there is a valid path and the space is empty)
    private void makeMove(int row, int col, Piece piece) {
        board[row][col] = piece;
        boolean color = piece.color;
        // list to hold all pieces we want to flip 
        List<Piece> list = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (row + i < 0 || row + i >= board.length || col + j < 0 || col + j >= board.length) {
                    continue;
                }
                if (board[row + i][col + j] == null) {
                    continue;
                }
                if (board[row + i][col + j].color != color) {
                    int adjRowIndex = row + i;
                    int adjColIndex = col + j;
                    // walk forward until we hit the end of the path
                    while (adjRowIndex >= 0 && adjColIndex >= 0 && adjRowIndex < board.length && adjColIndex < board.length) {
                        Piece curPiece = board[adjRowIndex][adjColIndex];
                        if (curPiece == null) {
                            break;
                        }
                        if (curPiece.color == color) {
                            // so we dont recheck the boundary piece we found
                            adjRowIndex -= i;
                            adjColIndex -= j;
                            // walk in reverse back down the path and accumulate all pieces in between our placed piece and our colored pieces
                            while (adjRowIndex != row || adjColIndex != col) {
                                list.add(board[adjRowIndex][adjColIndex]);
                                adjRowIndex -= i;
                                adjColIndex -=j;
                            }
                            break;
                        }
                        adjRowIndex += i;
                        adjColIndex += j;
                    }
                }
            }
        }
        // flip every piece we encountered that needed to be flipped (in all directions)
        for (Piece p : list) {
            p.flip();
        }
    }
    // checks to see if any legal moves exist for a specific player
    private boolean checkMovesExist(Piece piece) {
        int legalMoveCount = 0;
        for (int i = 0 ; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (checkMoveLegality(i, j, piece)) {
                    legalMoveCount++;
                }
            }
        }
        // if any legal move exists for our color, this will return true
        return legalMoveCount > 0;
    }
    
    // checks to see if either player can make a move (which signals the end of a game if not)
    private boolean isGameOver() {
        int legalWhiteCount = 0;
        int legalBlackCount = 0;
        // iterate across the entire board and check every square to see if at least one legal move exists at that space 
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (checkMoveLegality(i, j, new Piece(true))) {
                    legalWhiteCount++;
                }
                if (checkMoveLegality(i, j, new Piece(false))) {
                    legalBlackCount++;
                }
            }
        }
        // if any legal move exists, this will return false (which means the game should continue)
        // if no moves exist, the false will be negated and that means the game is over 
        return !(legalWhiteCount > 0 || legalBlackCount > 0);
    }

    // a method to find who won
    // 1 -> white winner
    // -1 -> black winner
    // 0 -> tie game
    private int findWinner() {
        int blackScore = 0;
        int whiteScore = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0 ; j < board.length; j++) {
                if (board[i][j] == null) {
                    continue;
                }
                if (board[i][j].color) {
                    whiteScore++;
                }
                else {
                    blackScore++;
                }
            }
        } 
        System.out.println("White score: " + whiteScore);
        System.out.println("Black score: " + blackScore);
        if (whiteScore > blackScore) {
            return 1;
        }
        else if (blackScore > whiteScore) {
            return -1;
        }
        else {
            return 0;
        }
    }

    // a function to copy over the contents of a Board to a new object
    public Board copy() {
        Board newBoard = new Board();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                newBoard.board[i][j] = new Piece(board[i][j].color);
            }
        }
        return newBoard;
    }

    // a function to find our heuristic value, which is that having more pieces of your color is good
    public int heuristic(boolean color) {
        int difference = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {
                    continue;
                }
                if (board[i][j].color == color) {
                    difference++;
                }
                else {
                    difference--;
                }
            }
        }
        return difference; 
    }

    // a method to perform minimax search. takes in a Board, a depth, a flag for whether to min or max, and a color to
    // perform our heuristic function with
    public int minimax(Board state, int depth, boolean maximizingPlayer, boolean color) {
        // if we can descend no further or the game has ended, evaluate this node we are at 
        if (depth == 0 || state.isGameOver()) {
            // static evaluation of the state
            return state.heuristic(color);
        }
        // maximizing ourself
        if (maximizingPlayer) {
            // extremely large negative value so we get replaced each time with a higher value later in the loop 
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < state.board.length; i++) {
                for (int j = 0; j < state.board.length; j++) {
                    // if we can make the current move, make it and recurse further down the tree
                    if (state.checkMoveLegality(i, j, new Piece(color))) {
                        // grab a copy so we dont change the original board state each recurse down the "tree"
                        Board copy = state.copy();
                        // make the legal move
                        copy.makeMove(i, j, new Piece(color));
                        // evaluate further down the tree to see how this move benefits us relative to the other choices thus far 
                        // pass false to simulate the other player's move which we would want to harm in our choices to get more pieces on the board
                        int eval = minimax(copy, depth - 1, false, color);
                        // take the max of the available options we have seen at this point
                        maxEval = Math.max(eval, maxEval);
                    }
                }
            }
            return maxEval;
        }
        // minimizing our opponent
        // extremely similar logic as above, just passing 
        else {
            // extremely large positive value so we get replaced each time with a higher value later in the loop 
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < state.board.length; i++) {
                for (int j = 0; j < state.board.length; j++) {
                    if (state.checkMoveLegality(i, j, new Piece(!color))) {
                        Board copy = state.copy();
                        copy.makeMove(i, j, new Piece(!color));
                        int eval = minimax(copy, depth - 1, true, color);
                        minEval = Math.min(eval, minEval);
                    }
                }
            }
            return minEval;
        }
    }

    
    public void mainLoop() {
        clear();
        int winner = 0;
        int MAX_DEPTH = 7;
        // clear the board (just in case)
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = null;
            }
        }
        // set the initial board state
        board[3][3] = new Piece(true);
        board[3][4] = new Piece(false);
        board[4][3] = new Piece(false);
        board[4][4] = new Piece(true);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Othello! Use the terminal to make a move");
        System.out.println("The board is an 8x8 grid. Please enter moves like \"A4\", where A is the column and 4 is the row");
        System.out.println("If you would like to adjust the search depth, please enter \"ADJUST\".");
        System.out.println("If you would like to quit the game, please enter \"QUIT\".");
        boolean running = true;
        boolean curPlayer = false; // true = white, false = black
        int whiteScore = 0;
        int blackScore = 0;
        // gameplay loop
        while (running) {
            printBoard();
            Piece piece = new Piece(curPlayer);
            // check at the beginning of each player's turn if they can play
            if (!checkMovesExist(piece)) {
                clear();
                System.out.println("No moves exist for current player: turn is skipped!");
                curPlayer = !curPlayer;
            }
            if (isGameOver()) {
                running = false;
                winner = findWinner();
                if (winner > 0) {
                System.out.println("White is the winner!");
                }
                else if (winner < 0) {
                    System.out.println("Black is the winner!");
                }
                else {
                    System.out.println("Draw game!");
                }
                continue;
            }
            if (curPlayer) {
                System.out.print("White's turn: where do you want to place? ");
            }
            else {
                System.out.print("Black's turn: where do you want to place? ");
            }
            String input = scanner.nextLine().trim();
            if (input.toUpperCase().equals("ADJUST")) {
                System.out.println("What would you like to adjust the search space to (Values between 0-10 only)? ");

                while (true) {
                    input = scanner.nextLine().trim();
                    if (input.length() < 1 || input.length() > 2) {
                        System.out.println("Invalid input! Search depth must be between 0-10. Invalid input: " + input);
                    }
                    else {
                        // try-catch in case a value is passed that is not an integer in string form 
                        try {
                            int newDepth = Integer.parseInt(input);
                            MAX_DEPTH = newDepth;
                            break;
                        }
                        catch (NumberFormatException e) {
                            System.out.println("Invalid input! Search depth must be an integer value (0-10, no decimal numbers or characters).");
                        }
                    }
                }
            } 
            // quitting the game properly 
            if (input.toUpperCase().equals("QUIT")) {
                running = false;
                continue;
            }
            // all valid inputs should be of length 2
            if (input.length() != 2) {
                clear();
                System.out.println("Please enter a valid input!\nCombinations of (A-H)(1-8) are valid inputs. Ex: A4, B7, ... Invalid move: " + input);
                continue;
            }
            // convert chars to ints
            int col = (int) (Character.toUpperCase(input.charAt(0)) - 'A');
            int row = (int) (input.charAt(1) - '1');
            // input validation
            if (row < 0 || row >= board.length || col < 0 || col >= board.length) {
                clear();
                System.out.println("Please enter a valid input! Combinations of (A-H)(1-8) are valid inputs. Ex: A4, B7, ... Invalid move: " + input);
                continue;
            }
            if (checkMoveLegality(row, col, piece)) {
                makeMove(row, col, piece);
            }
            else {
                clear();
                System.out.println("Illegal move! Moves must outflank at least ONE of your opponent's pieces! Invalid move: " + input);
                continue;
            }
            // flip player at the end of each turn
            // we only flip the player (and hit the end of the turn)
            // when a move is considered legal and is properly performed so that someone who CAN perform a turn MUST perform a legal move in that turn
            curPlayer = !curPlayer;
            clear();
        }
        scanner.close();
        System.out.println("\n\nThanks for playing!!!");
    }
}