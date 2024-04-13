package server.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import config.ConfigConsts;
import dataAccess.ExecuteSQL;

import java.sql.SQLException;

public class UpdateChessDatabase {


    public static void updateGameMakeMove(int gameID, ChessGame game, ChessMove move) throws InvalidMoveException {
        game.makeMove(move);                        // Make the requested chess move
        String gameJson = new Gson().toJson(game);  // Serialize the updated game object
        String updateStmt = "UPDATE " + ConfigConsts.GAME_TABLE_NAME + " SET game = ? WHERE " +
                            " gameID = " + gameID + ";";
        try {
            ExecuteSQL.executeUpdate(updateStmt, gameJson);
        } catch (SQLException e) {
            System.out.println("Could not update SQL database.");
        }
    }

    public static void updateChessGame(int gameID, ChessGame game) {
        String gameJson = new Gson().toJson(game);  // Serialize the updated game
        String updateStmt = "UPDATE " + ConfigConsts.GAME_TABLE_NAME + " SET game = ? WHERE " +
                            " gameID = " + gameID;
        try {
            ExecuteSQL.executeUpdate(updateStmt, gameJson);
        } catch (SQLException e) {
            System.out.println("Could not update SQL database.");
        }
    }

    /**
     * This is used for updating the game name of an existing chess game.
     * @param gameID
     * @param gameName
     */
    public static void updateChessGameName(int gameID, String gameName) {
        // Prepare a SQL statement
        String updateStmt = "UPDATE " + ConfigConsts.GAME_TABLE_NAME + " SET gameName = ? WHERE " +
                " gameID = " + gameID;
        try {
            ExecuteSQL.executeUpdate(updateStmt, gameName);
        } catch (SQLException e) {
            System.out.println("Could not update SQL database.");
        }
    }


    //FIXME: may not need a retrieval after all?
//    public static ChessGame retrieveDatabaseChessGame(int gameID) {
//
//    }
}
