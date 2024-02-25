package config;

/**
 * This is a CONFIG class that can be used to tweak and adjust various parameters of the server.
 */


public class Config {

    public static final int GAME_ID_MIN = 1;    // Smallest value of game ID
    public static final int GAME_ID_MAX = 999;  // MAXIMUM ALLOWED GAMES (And highest game ID value)


    // Strings

    // Name of header that holds authToken in logout requests
    public static final String LOGOUT_REQ_HEADER = "authorization";
}
