public class HumanPlayer implements Human,Player{

    SwingGUI gui;
    int gameDimension;
    GameLogic gameLogic;


    //helper method to get opponents move in order for it to be communicated to gui
    //for display purposes
    public void opponentDisplay(int col){
        gui.lastMove(col);
    }

    @Override
    public void setAnswer(int col) {
        System.out.println("Human played in col: "+col+"\n");
        lastMove(col);
    }

    @Override
    public void lastMove(int lastCol) {

        //change the player to ai
        //so now games next player is ai
        gameLogic.changePlayer(this);
        //tell set answer in game to update the board using the column
        gameLogic.setAnswer(lastCol);
    }

    //will print out if winner is human
    //and will tell gui to print results for human or ai
    @Override
    public void gameOver(Status winner) {
        if(winner == Status.ONE){
            System.out.println("Player ONE is the winner");
        }
        gui.gameOver(winner);
    }

    //will communicate info with both gui and logic
    @Override
    public void setInfo(int size, GameLogic gl) {
        gameDimension = size;
        gameLogic = gl;
        gui = new SwingGUI();
        gui.setInfo(this,gameDimension);
    }
}
