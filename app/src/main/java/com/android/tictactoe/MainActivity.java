package com.android.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tile_1)
    TextView tile1;

    @BindView(R.id.tile_2)
    TextView tile2;

    @BindView(R.id.tile_3)
    TextView tile3;

    @BindView(R.id.tile_4)
    TextView tile4;

    @BindView(R.id.tile_5)
    TextView tile5;

    @BindView(R.id.tile_6)
    TextView tile6;

    @BindView(R.id.tile_7)
    TextView tile7;

    @BindView(R.id.tile_8)
    TextView tile8;

    @BindView(R.id.tile_9)
    TextView tile9;

    @BindView(R.id.play_again)
    Button playAgain;

    @BindView(R.id.x_wins)
    TextView xWinsTextView;

    @BindView(R.id.y_wins)
    TextView yWinsTextView;

    @BindView(R.id.draw)
    TextView drawsTextView;


    private TextView[][] tiles = new TextView[3][3];
    private User currentUser;
    private User playerO = new User(Player.PLAYERO);
    private User playerX = new User(Player.PLAYERX);
    private int markedTiles = 0;
    private Board board;
    private int playerXwins;
    private int playerOwins;
    private int drawGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currentUser = new Random().nextBoolean() ? playerO : playerX;

        loadGameScores();

        tiles[0][0] = tile1;
        tiles[0][1] = tile2;
        tiles[0][2] = tile3;
        tiles[1][0] = tile4;
        tiles[1][1] = tile5;
        tiles[1][2] = tile6;
        tiles[2][0] = tile7;
        tiles[2][1] = tile8;
        tiles[2][2] = tile9;

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].setOnClickListener(this);
            }
        }

        // fetch and update tiles mark from board

        // if no state is saved on the server or there is any error in fetching state
        String[][] tilesMark = new String[3][3];

        // check previous state from the local prefs
        String boardJson = getSharedPreferences(SharedPrefUtils.GAME_STATE_PREFS, Context.MODE_PRIVATE).getString(SharedPrefUtils.GAME_STATE, null);

        // if no previous state in local prefs then load new game
        if (boardJson == null) {
            for (int i = 0; i < tilesMark.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    tilesMark[i][j] = "";
                }
            }
            board = new Board(tilesMark, markedTiles);
        } else {

            // restore previous state
            board = new Gson().fromJson(boardJson, Board.class);
            markedTiles = board.getMarkedTiles();
            currentUser = board.getLastMarkedTileMark().equals(User.PLAYER_O_MARK) ? playerX : playerO;

            String[][] savedTilesMark = board.getTiles();

            for (int i = 0; i < savedTilesMark.length; i++) {
                for (int j = 0; j < savedTilesMark[i].length; j++) {
                    tiles[i][j].setText(savedTilesMark[i][j]);
                }
            }

        }

        // if no previous state available then load new game

        // if state is retreived from the server then init board with saved state


        playAgain.setOnClickListener(v -> {
            playAgain.setVisibility(View.INVISIBLE);
            updateTiles(true);
        });
    }

    @Override
    public void onClick(View v) {

        TextView tile = (TextView) v;

        if (tile.getText() != null && !tile.getText().toString().isEmpty()) {
            return;
        }

        tile.setText(currentUser.getUserMark());
        switch (v.getId()) {
            case R.id.tile_1:
                board.updateTileMark(0, 0, currentUser.getUserMark());
                break;
            case R.id.tile_2:
                board.updateTileMark(0, 1, currentUser.getUserMark());
                break;
            case R.id.tile_3:
                board.updateTileMark(0, 2, currentUser.getUserMark());
                break;
            case R.id.tile_4:
                board.updateTileMark(1, 0, currentUser.getUserMark());
                break;
            case R.id.tile_5:
                board.updateTileMark(1, 1, currentUser.getUserMark());
                break;
            case R.id.tile_6:
                board.updateTileMark(1, 2, currentUser.getUserMark());
                break;
            case R.id.tile_7:
                board.updateTileMark(2, 0, currentUser.getUserMark());
                break;
            case R.id.tile_8:
                board.updateTileMark(2, 1, currentUser.getUserMark());
                break;
            case R.id.tile_9:
                board.updateTileMark(2, 2, currentUser.getUserMark());
                break;
            default:
                break;

        }
        // saving the last marked tile
        board.setLastMarkedTileMark(currentUser.getUserMark());

        markedTiles++;

        checkResult(currentUser);
        toggleUser();

    }

    private void checkResult(User currentUser) {

        if (markedTiles < 5) return;

        String currentUserMark = currentUser.getUserMark();


        // horizontal tiles
        for (int i = 0; i < tiles.length; ++i) {

            if (tiles[i][0].getText().toString().equals(currentUserMark) &&
                    tiles[i][1].getText().toString().equals(currentUserMark) &&
                    tiles[i][2].getText().toString().equals(currentUserMark)) {
                showWinningToast(currentUser);
                return;
            }

        }

        // vertical tiles
        for (int i = 0; i < tiles.length; ++i) {
            if (tiles[0][i].getText().toString().equals(currentUserMark) &&
                    tiles[1][i].getText().toString().equals(currentUserMark) &&
                    tiles[2][i].getText().toString().equals(currentUserMark)) {
                showWinningToast(currentUser);
                return;
            }
        }

        // diagonal tiles
        if ((tiles[0][0].getText().toString().equals(currentUserMark) &&
                tiles[1][1].getText().toString().equals(currentUserMark) &&
                tiles[2][2].getText().toString().equals(currentUserMark))

                || (tiles[2][0].getText().toString().equals(currentUserMark) &&
                tiles[1][1].getText().toString().equals(currentUserMark) &&
                tiles[0][2].getText().toString().equals(currentUserMark))) {

            showWinningToast(currentUser);
            return;

        }


        if ((tiles[0][0].getText().toString().equals(currentUserMark) &&
                tiles[1][1].getText().toString().equals(currentUserMark) &&
                tiles[2][2].getText().toString().equals(currentUserMark))

                || (tiles[2][0].getText().toString().equals(currentUserMark) &&
                tiles[1][1].getText().toString().equals(currentUserMark) &&
                tiles[0][2].getText().toString().equals(currentUserMark))) {

            showWinningToast(currentUser);
            return;
        }


        if (markedTiles == 9) {
            Toast.makeText(MainActivity.this, "Draw ", Toast.LENGTH_SHORT).show();
            playAgain.setVisibility(View.VISIBLE);
            updateTiles(false);

            drawGames++;

            saveGameScores();
            updateGameScoresUI();
        }
    }

    private void loadGameScores() {

        SharedPreferences gamePrefs = getSharedPreferences(SharedPrefUtils.SCORES_PREFS, Context.MODE_PRIVATE);
        playerXwins = gamePrefs.getInt(SharedPrefUtils.SCORES.PLAYERX, 0);
        playerOwins = gamePrefs.getInt(SharedPrefUtils.SCORES.PLAYERO, 0);
        drawGames = gamePrefs.getInt(SharedPrefUtils.SCORES.DRAW, 0);

        updateGameScoresUI();
    }

    private void updateGameScoresUI() {
        xWinsTextView.setText(" X wins - " + playerXwins);
        yWinsTextView.setText(" O wins - " + playerOwins);
        drawsTextView.setText(" Draws - " + drawGames);
    }

    private void saveGameScores() {
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefUtils.SCORES_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharedPrefUtils.SCORES.PLAYERO, playerOwins).commit();
        editor.putInt(SharedPrefUtils.SCORES.PLAYERX, playerXwins).commit();
        editor.putInt(SharedPrefUtils.SCORES.DRAW, drawGames).commit();
    }

    private void showWinningToast(User player) {

        playAgain.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, " Player " + player.getUserMark() + " wins ", Toast.LENGTH_LONG).show();
        updateTiles(false);

        switch (player.getUserMark()) {
            case User.PLAYER_O_MARK:
                playerOwins++;
                break;
            case User.PLAYER_X_MARK:
                playerXwins++;
                break;
            default:
                break;
        }
        saveGameScores();
        updateGameScoresUI();
    }

    private void toggleUser() {
        currentUser = currentUser.getPlayer() == Player.PLAYERO ? playerX : playerO;
    }

    private void updateTiles(boolean enable) {
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles[i].length; ++j) {
                if (enable) {
                    tiles[i][j].setText("");
                }
                tiles[i][j].setEnabled(enable);
            }
        }

        markedTiles = 0;
    }


    private void saveGameState() {
        board.setMarkedTiles(markedTiles);
        SharedPreferences sharedPrefs = getSharedPreferences(SharedPrefUtils.GAME_STATE_PREFS, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(SharedPrefUtils.GAME_STATE, board.getJsonRepresentation()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveGameScores();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveGameState();
    }
}
