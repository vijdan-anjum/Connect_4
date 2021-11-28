import java.util.Random;

public class AIPlayer implements Player {

    //private linked list class to hold all the columns where the AI can possibly make a move
    //given multiple options
    private static class LinkedList {

        private class Node {

            int col;
            Node next;

            public Node(int col, Node next) {
                this.col = col;
                this.next = next;
            }
        }

        private Node head;
        private int size;

        public LinkedList() {
            this.head = null;
        }

        public void add(int col) {
            head = new Node(col, head);
            size++;
        }

        public void addList(LinkedList otherList) {
            for (int i = 0; i < otherList.size; i++) {
                add(otherList.getIndex(i));
            }
        }

        public int getIndex(int index) {
            Node curr = head;
            int count = 0;
            while (curr != null && count != index) {
                curr = curr.next;
                count++;
            }
            return curr.col;
        }

        public void printList() {
            int curr;
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    curr = getIndex(i);
                    System.out.print("{" + curr + "}");
                }
                System.out.println("");
            } else {
                System.out.println("{EMPTY}");
            }
        }
    }

    int gameDimensions;
    Game gameLogic;
    Status[][] board;

    @Override
    //the lastCol will either be -1 since its the start of the game
    //or itll be where the human made a move
    //so if its not -1 then we find a col
    public void lastMove(int lastCol) {
        int col;
        if (lastCol == -1) {
            col = new Random().nextInt(gameDimensions);
        } else {
            col = execute();
        }
        System.out.println("AI chose to play in col: " + col);
        //player is now already changed so nextplayer is HUMAN
        gameLogic.changePlayer(this);
        gameLogic.setAnswer(col);
    }

    //method that will print out the winner
    @Override
    public void gameOver(Status winner) {
        System.out.println("Player TWO is the winner");
    }

    //this method will communicate with game logic
    @Override
    public void setInfo(int size, GameLogic gl) {
        gameDimensions = size;
//        gameLogic = gl;
        if (gl instanceof Game) {
            gameLogic = (Game) gl;
        }
    }


    private int execute() {
        LinkedList list = executions();
        int col;
        int index;
        if (list.size == 0) {
            System.out.println("Playing randomly at col");
            col = new Random().nextInt(gameDimensions);
            System.out.println("COL: " + col);
        } else {
            index = new Random().nextInt(list.size);
            col = list.getIndex(index);
            System.out.println("COL: " + col+"\n");
        }
        return col;
    }


    private LinkedList executions() {
        board = gameLogic.getBoard();
        LinkedList mainlist = new LinkedList();
        LinkedList tempList;


        for (int row = board.length - 1; row >= 0; row--) {
            for (int col = 0; col < board[0].length; col++) {
                //first we will play defensively
                if (board[row][col] == Status.ONE) {
                    tempList = execList(board[row][col], row, col);
                    mainlist.addList(tempList);
                }
            }
        }
        //if no possibilities to defend
        if (mainlist.size == 0) {
            for (int row = board.length - 1; row >= 0; row--) {
                for (int col = 0; col < board[0].length; col++) {
                    //then we will play offensively to secure win
                    if (board[row][col] == Status.TWO) {
                        tempList = execList(board[row][col], row, col);
                        mainlist.addList(tempList);
                    }
                }
            }
            if (mainlist.size != 0) {
                System.out.println("Playing offensively at col");
            }

        } else {
            System.out.println("Playing defensively at col");
        }
        return mainlist;
    }

    //this method will check whether a given direction can be checked for 3 of the same statuses
    //note here left side will be checked as well regardless of the same traversal method as the Ai must think in
    // real time
    //and if so whether the AI can place itself there to attack or defend
    private LinkedList execList(Status status, int row, int col) {
        LinkedList list = new LinkedList();

        if (gameLogic.canCheck(gameLogic.UP, row, col)) {
            if (gameLogic.checkUp3(status, row, col)) {
                if (board[row - 3][col] == Status.NEITHER) {
                    list.add(col);
                }
            }
        }

        if (gameLogic.canCheck(gameLogic.RIGHT, row, col)) {
            if (gameLogic.checkRight3(status, row, col)) {
                if (board[row][col + 3] == Status.NEITHER) {
                    list.add(col + 3);
                }
            }
        }

        if (gameLogic.canCheck(gameLogic.LEFT, row, col)) {
            if (gameLogic.checkLeft3(status, row, col)) {
                if (board[row][col - 3] == Status.NEITHER) {
                    list.add(col - 3);
                }
            }
        }

        if (gameLogic.canCheck(gameLogic.UP, row, col) && gameLogic.canCheck(gameLogic.RIGHT, row, col)) {
            if (gameLogic.checkUpRight3(status, row, col)) {
                if (board[row - 2][col + 3] != Status.NEITHER) {
                    if (board[row - 3][col + 3] == Status.NEITHER) {
                        list.add(col + 3);
                    }
                }
            }
        }

        if (gameLogic.canCheck(gameLogic.UP, row, col) && gameLogic.canCheck(gameLogic.LEFT, row, col)) {
            if (gameLogic.checkUpLeft3(status, row, col)) {
                if (board[row - 2][col - 3] != Status.NEITHER) {
                    if (board[row - 3][col - 3] == Status.NEITHER) {
                        list.add(col - 3);
                    }
                }
            }
        }

        return list;
    }
}
