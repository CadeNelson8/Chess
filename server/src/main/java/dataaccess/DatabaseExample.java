package dataaccess;
import java.sql.DriverManager;

public class DatabaseExample {
    public static void main(String[] args) throws Exception {
        // Use try-with-resources to ensure the connection is closed automatically
        try (var conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "monkeypie")) {
            conn.setCatalog("pets");

            try (var preparedStatement = conn.prepareStatement("SELECT id, name, type FROM pet")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt("id");
                        var name = rs.getString("name");
                        var type = rs.getString("type");

                        System.out.printf("id: %d, name: %s, type: %s%n", id, name, type);
                    }
                }
            }
        }
    }
}

