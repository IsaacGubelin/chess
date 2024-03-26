package config;

/**
 * This is a CONFIG class that can be used to tweak and adjust various parameters of the server handlers.
 */


public class ConfigConsts {

    // Numeric values
    public static final int GAME_ID_MIN = 1;    // Smallest value of game ID
    public static final int GAME_ID_MAX = 999;  // MAXIMUM ALLOWED GAMES (And highest game ID value)


    // Strings
    // Name of header that holds authToken in logout requests
    public static final String LOGOUT_REQ_HEADER = "authorization";
    public static final String BLACK_TEAM_REQ = "BLACK";
    public static final String WHITE_TEAM_REQ = "WHITE";

    // SQL NAMES FOR TABLES AND DATABASES
    public static final String AUTH_TABLE_NAME = "auths";
    public static final String AUTH_TABLE_KEY_COL = "authToken";
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_TABLE_KEY_COL = "username";
    public static final String GAME_TABLE_NAME = "games";
    public static final String GAME_TABLE_KEY_COL = "gameID";
    public static final String WHITE_TEAM_COL = "whiteUsername";
    public static final String BLACK_TEAM_COL = "blackUsername";
}
