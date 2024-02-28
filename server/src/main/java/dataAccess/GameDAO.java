package dataAccess;

public interface GameDAO {

    void clearGamesDataBase();
    int createGame(String gameName);
}