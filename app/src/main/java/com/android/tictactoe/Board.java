package com.android.tictactoe;

import com.google.gson.Gson;

/**
 * Created by vishalarora on 13/02/17.
 */

public class Board {

    private int markedTiles;

    private String lastMarkedTileMark;

    public String getLastMarkedTileMark() {
        return lastMarkedTileMark;
    }

    public void setLastMarkedTileMark(String lastMarkedTileMark) {
        this.lastMarkedTileMark = lastMarkedTileMark;
    }

    public int getMarkedTiles() {
        return markedTiles;
    }

    public void setMarkedTiles(int markedTiles) {
        this.markedTiles = markedTiles;
    }

    private String[][] tiles = new String[3][3];

    public Board(String[][] tiles, int markedTiles) {
        this.tiles = tiles;
        this.markedTiles = markedTiles;
    }

    public String[][] getTiles() {
        return tiles;
    }

    public void setTiles(String[][] tiles) {
        this.tiles = tiles;
    }

    public void updateTileMark(int i, int j, String mark) {
        tiles[i][j] = mark;
    }

    public String getJsonRepresentation() {
        return new Gson().toJson(this, Board.class);
    }
}
