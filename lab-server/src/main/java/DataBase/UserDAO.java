package DataBase;

import authentication.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.PasswordHasher;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public final Connection connection;
    private static final Logger log = LogManager.getLogger(UserDAO.class);

    private static final String CREATE_USER_SQL = """
            INSERT INTO users (
            username,
            password_hash
            ) VALUES (?, ?)
            RETURNING id;
            """;

    private static final String CREATE_USER_WITH_ID_SQL = """
            INSERT INTO users (
            username,
            password_hash,
            id
            ) VALUES (?, ?, ?)
            RETURNING id;
            """;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User createUser(User user) throws SQLException {
        String username = user.getUsername();
        String passwordHash = PasswordHasher.hash(user.getPassword());
        System.out.println(passwordHash);
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_USER_SQL)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long id = rs.getLong("id");
                connection.commit();
                log.info("Создан пользователь {} с ID {}", username, id);

                return new User(id, username, passwordHash);
            }
            throw new SQLException("Не удалось получить ID созданного пользователя");
        }
    }

    public User getUserByUsername(String name) throws SQLException {
        String query = "SELECT id, username, password_hash FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String userName = resultSet.getString("username");
                    String pwHash = resultSet.getString("password_hash");
                    return new User(id, userName, pwHash);
                } else {
                    return null;
                }
            }
        }
    }
}
