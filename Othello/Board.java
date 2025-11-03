// a Piece has a color and can flip 
class Piece {
    // let true be white, false be black
    private boolean color;

    public Piece(boolean color) {
        this.color = color;
    }

    public boolean getColor() {
        return color;
    }

    // negate the color's current value to "flip" it
    public void flip() {
        this.color = !color;
    }
}

// a board has 2d array of Pieces and can be printed
class Board {
    public Piece[][] board;
    
    public Board() {
        board = new Piece[8][8];
    }

    // a method to represent the board's current state
    public void printBoard() {
        System.out.println("---------------------------------");
        for (int j = 0; j < board.length; j++) {
            for (int k = 0; k < board[j].length; k++) {
                System.out.print("| ");
                if (board[j][k] != null) {
                    if (board[j][k].getColor()) {
                        System.out.print("W ");
                    }
                    else {
                        System.out.print("B ");
                    }
                }
                else {
                    System.out.print("  ");
                }
            }
            System.out.print("|");
            System.out.println();
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
    private boolean checkMoveLegality(int col, int row, Piece piece) {

    }

    public void mainLoop() {
        clear();
        System.out.println("Welcome to Othello! Use the terminal to make a move");
        boolean running = true;
        while (running) {
            System.out.print("Black's turn: where do you want to place?");
        }
    }
}