import java.util.Random;

public class Game implements GameLogic {

    //These variables will help with looking into
    // the direction being looked into the board
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int LEFT = 4;

    private int size;
    private HumanPlayer hp;
    private AIPlayer ai;
    private Player nextPlayer;
    private Status[][] board;

    //constructor that will create the game dimensions,
    // initiate the players and convey all the information to all the classes
    public Game(){

        size = new Random().nextInt(13-6)+6;
        hp = new HumanPlayer();
        ai = new AIPlayer();
        board = makeBoard(size);
        hp.setInfo(size,this);
        ai.setInfo(size,this);

        if(playerOne() == 0){
            nextPlayer = hp;
        }else{
            nextPlayer = ai;
        }
    }

    //helper method to make 2-D board for constructor
    private Status[][] makeBoard(int size){

        Status[][] board = new Status[size][size];
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                board[i][j] = Status.NEITHER;
            }
        }
        return board;
    }

    //helper method for constructor to choose a player at random
    private int playerOne(){
        return new Random().nextInt(2);
    }

    //method that will be used to flip player to the other by checking current player
    @Override
    public void changePlayer(Player player){
        if(player instanceof HumanPlayer){
            System.out.println("\nIt is now the AI's turn");
            nextPlayer = ai;
        }else if(player instanceof AIPlayer){
            System.out.println("\nIt is now the Human's turn");
            nextPlayer = hp;
        }
    }

    //method that will start the game
    //if first player is AI then set column as -1
    public void start(){
        if(nextPlayer == ai){
            System.out.println("\nPlayer one is: AI");
            ai.lastMove(-1);
        }else{
            System.out.println("\nPlayer one is: HUMAN");
        }
    }

    //this method will be the main communicator that will help swicth turns and let the
    //game play out until we hit gameover
    @Override
    public void setAnswer(int col) {

        //the board must be updated
        updateBoard(col,nextPlayer);
        //if the board has space to place for a player
        if(!noMoreSpace()) {
            //if no player has won yet
            if (!foundWinner()) {
                //if the nextplayer is human
                if (nextPlayer instanceof HumanPlayer) {
                    //then opponent played before the switch so human needs
                    //the visulization of the enemys move
                    hp.opponentDisplay(col);

                } else if (nextPlayer instanceof AIPlayer) {
                    //if human just played then prompt the ai to make a move
                    ai.lastMove(col);
                }
            } else {
                //if we reach a gameover and the enemy had just played before this then their
                //visulization is still needed for the game to look complete
                //since the next player is human that would imply the previous player
                //ai has won
                if (nextPlayer instanceof HumanPlayer) {
                    hp.opponentDisplay(col);
                    ai.gameOver(Status.TWO);
                    hp.gameOver(Status.TWO);
                } else if (nextPlayer instanceof AIPlayer) {
                    hp.gameOver(Status.ONE);
                }
                System.out.println("\n\nGame Over\n\n");
            }
        }else{
            //if no more space left
            if (nextPlayer instanceof HumanPlayer) {
                hp.opponentDisplay(col);
            }
            //therefore no one is the winner
            hp.gameOver(Status.NEITHER);
            System.out.println("\n\nNo space left on board. DRAW");
            System.out.println("Game Over\n\n");
        }
    }

    //helper method that will update the board every time a mode is made by either player
    private void updateBoard(int col,Player otherPlayer){
        int row = board.length-1;
        while (board[row][col] != Status.NEITHER){
            row--;
        }

        if(otherPlayer instanceof AIPlayer) {
            board[row][col] = Status.ONE;
        }else if(otherPlayer instanceof HumanPlayer){
            board[row][col]=Status.TWO;
        }
    }

    //helper method that will check if we are out of space
    private boolean noMoreSpace(){
        boolean noSpace = true;
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                if(board[i][j] == Status.NEITHER){
                    noSpace = false;
                }
            }
        }
        return noSpace;
    }


    //method that will check for a winner
    private boolean foundWinner(){
        boolean won = false;
        Status status;

        //since we are traversing in the right direction and then we move up each step
        //we will only be checking right up and the upward diagonals
        //as all other directions will be redundant
        for (int row = board.length - 1; row >= 0; row--) {
            for (int col = 0; col < board[row].length; col++) {

                status = board[row][col];
                if(status != Status.NEITHER){

                    if(!won && canCheck(UP,row,col)) {
                        won = checkUp4(status,row, col);
                    }

                    if(!won && canCheck(RIGHT,row,col)) {
                        won = checkRight4(status,row, col);
                    }

                    if(!won && canCheck(UP,row,col) && canCheck(RIGHT,row,col)) {
                        won = checkUpRight4(status,row, col);
                    }

                    if(!won && canCheck(UP,row,col) && canCheck(LEFT,row,col)){
                        won = checkUpLeft4(status,row,col);

                    }
                }
            }
        }
        return won;
    }





    //Method that will help whether the direction can be checked
    //that is it does not goes out of the 2-D array
    public boolean canCheck(int direction, int row, int col) {
        boolean check = false;

        if((board.length -col >= 4) && direction == RIGHT){
            check = true;
        }else if((col >= 3) && (direction == LEFT)){
            check = true;
        }

        if((board.length -row >= 4) && direction == DOWN){
            check = true;
        }else if((row >= 3) && (direction == UP)){
            check = true;
        }
        return check;
    }





    //Check methods to check for directions UP, RIGHT, LEFT, UPRIGHT AND UPLEFT
    //this will check whether 4 of the objects match in the directions stated
    private boolean checkRight4(Status status, int row, int col) {
        return (checkRight3(status,row,col) && status == board[row][col + 3]);
    }
    private boolean checkUp4(Status status, int row, int col) {
        return (checkUp3(status,row,col) && status == board[row - 3][col]);
    }
    private boolean checkUpRight4(Status status, int row, int col) {
        return (checkUpRight3(status,row,col) && status == board[row - 3][col + 3]);
    }
    private boolean checkUpLeft4(Status status, int row, int col) {
        return (checkUpLeft3(status,row,col) && status == board[row - 3][col - 3]);
    }
    private boolean checkLeft4(Status status, int row, int col) {
        return (checkLeft3(status,row,col) && status == board[row][col - 3]);
    }

    //helper methods for above to check 3 objects
    //also used for the ai
    public boolean checkRight3(Status status, int row, int col) {
        return (status == board[row][col + 1]
                && status == board[row][col + 2]);
    }
    public boolean checkUp3(Status status, int row, int col) {
        return (status == board[row - 1][col]
                && status == board[row - 2][col]);
    }
    public boolean checkUpRight3(Status status, int row, int col) {
        return (status == board[row - 1][col + 1]
                && status == board[row - 2][col + 2]);
    }
    public boolean checkUpLeft3(Status status, int row, int col) {
        return (status == board[row - 1][col - 1]
                && status == board[row - 2][col - 2]);
    }
    public boolean checkLeft3(Status status, int row, int col) {
        return (status == board[row][col - 1]
                && status == board[row][col - 2]);
    }
    //CHECKERS FOR UP RIGHT UPRIGHT AND UPLEFT 3


    //a getter to get board for AI
    public Status[][] getBoard() {
        return board;
    }
}
