package dataAccess;

import chess.ChessGame;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {

    void clearGamesDataBase();

//    int createGame(String gameName) throws DataAccessException;

    ChessGame getGame(String id) throws DataAccessException;

//    Collection<ChessGame> listGames();
}