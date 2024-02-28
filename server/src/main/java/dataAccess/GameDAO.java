package dataAccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {

    void clearGamesDataBase();

    int createGame(String gameName);

//    Collection<ChessGame> listGames();
}