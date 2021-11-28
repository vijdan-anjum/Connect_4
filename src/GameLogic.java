public interface GameLogic {

    void changePlayer(Player player);
    void setAnswer (int col);
    Status[][] getBoard();
    public boolean checkRight3(Status status, int row, int col);
    public boolean checkUp3(Status status, int row, int col);
    public boolean checkUpRight3(Status status, int row, int col);
    public boolean checkUpLeft3(Status status, int row, int col);
}