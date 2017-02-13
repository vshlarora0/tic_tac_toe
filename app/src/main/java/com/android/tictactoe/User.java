package com.android.tictactoe;

/**
 * Created by vishalarora on 13/02/17.
 */

public class User {

    private String userMark;
    public static final String PLAYER_X_MARK = "X";
    public static final String PLAYER_O_MARK = "O";

    public String getUserMark() {
        return userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark;
    }

    public User(Player player) {
        this.player = player;
        userMark = player == Player.PLAYERX ? PLAYER_X_MARK : PLAYER_O_MARK;
    }

    private Player player;
    private int score;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
