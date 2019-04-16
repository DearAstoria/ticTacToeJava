package client.raw_data;

public class GameState
{
    Board boardSpaces; // board spaces
    char currentTurn;  // who's turn it currently is (X or O)
    boolean gameOver;  // has the game reached an end?

    public void nextTurn()
    {
        if (currentTurn == 'X') {
            currentTurn = 'O';
        } else {
            currentTurn = 'X';
        }
    }

    public Board getBoardSpaces()
    {
        return boardSpaces;
    }

    public char getCurrentTurn()
    {
        return currentTurn;
    }

    public boolean isGameOver() { return gameOver; }

    public void toggleGameOver() { gameOver = !gameOver; }

    public GameState(Board boardSpaces, char currentTurn, boolean gameOver) {
        this.boardSpaces = boardSpaces;
        this.currentTurn = currentTurn;
        this.gameOver = gameOver;
    }

    public GameState(Board boardSpaces, char currentTurn)
    {
        this(boardSpaces, currentTurn, false);
    }

    public GameState(char currentTurn) { this(new Board(), currentTurn, false); }

    public GameState()
    {
        this(new Board(), 'X', false);
    }

    @Override
    public String toString()
    {
        StringBuilder gameState = new StringBuilder();

        gameState.append(boardSpaces.toString()).append("currentTurn: ").append(currentTurn).append(", gameOver: ").append(gameOver ? "true\n" : "false\n");

        return gameState.toString();
    }
}
