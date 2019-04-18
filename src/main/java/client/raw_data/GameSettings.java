package client.raw_data;

public class GameSettings {
    String player;    // name of the player
    boolean playingX; // does the player play as X (true) or O (false)
    boolean easy;     // easy mode (true) or hard mode (false)
    boolean xFirst;   // does the X player move first (true) or O player (false)

    // custom game settings
    public GameSettings(String player, boolean playingX, boolean easy, boolean xFirst) {
        this.player = player;
        this.playingX = playingX;
        this.easy = easy;
        this.xFirst = xFirst;
    }

    public GameSettings(String player) {
        this.player = player;
    }

    // default game settings: "player 1" is x, x goes first, game is on easy mode
    public GameSettings() {
        this("Player 1", true, true, true);
    }

    public String getPlayer() { return player; }

    public char getPlayerLetter() {
        if(playingX) {
            return 'X';
        } else {
            return 'O';
        }
    }

    public void setPlayer(String player) { this.player = player; }

    public boolean isPlayingX() {
        return playingX;
    }

    public void setPlayingX(boolean playingX) {
        this.playingX = playingX;
    }

    public boolean isEasy() {
        return easy;
    }

    public void setEasy(boolean easy) {
        this.easy = easy;
    }

    public boolean isxFirst() {
        return xFirst;
    }

    public void setxFirst(boolean xFirst) {
        this.xFirst = xFirst;
    }

    @Override
    public String toString() {
        return "GameSettings{\n" +
                ", player='" + player + "\n" +
                ", playingX=" + playingX + "\n"+
                ", easy=" + easy + "\n"+
                ", xFirst=" + xFirst + "\n"+
                '}';
    }
}
