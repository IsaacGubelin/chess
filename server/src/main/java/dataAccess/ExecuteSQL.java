package dataAccess;

public class ExecuteSQL {

    /**
     * Execute SQL statements with given string
     */
    public static void executeSqlLine(String stmt) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(stmt)) {
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        }
    }
}
