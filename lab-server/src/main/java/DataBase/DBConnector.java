package DataBase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final Logger logger = LogManager.getLogger(DBConnector.class);
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;

    public DBConnector(String dbHost, String dbPort, String dbUser, String dbPassword, String dbSchema) throws SQLException {
        this.url = String.format("jdbc:postgresql://%s:%s/studs?currentSchema=%s", dbHost, dbPort, dbSchema);
        this.user = dbUser;
        this.password = dbPassword;
        init();
    }

    public DBConnector(String URL, String user, String password) throws SQLException {
        this.url = URL;
        this.user = user;
        this.password = password;
        init();
    }

    private void init() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

            logger.info("Подключение к PostgreSQL успешно установлено. URL: {}", url);
        } catch (ClassNotFoundException e) {
            logger.error("PostgreSQL JDBC Driver не найден!", e);
            throw new SQLException("Драйвер БД не найден", e);
        } catch (SQLException e) {
            logger.error("Ошибка подключения к PostgreSQL: {}", e.getMessage());
            throw e;
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.warn("Соединение было закрыто. Переподключаемся...");
            init();
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                if (!connection.getAutoCommit()) {
                    connection.rollback();
                }
                connection.close();
                logger.info("Соединение с PostgreSQL закрыто");
            } catch (SQLException e) {
                logger.error("Ошибка при закрытии соединения", e);
            }
        }
    }

    public void commit() throws SQLException {
        connection.commit();
        logger.debug("Транзакция зафиксирована");
    }

    public void rollback() {
        try {
            connection.rollback();
            logger.debug("Транзакция откачена");
        } catch (SQLException e) {
            logger.error("Ошибка при откате транзакции", e);
        }
    }
}
