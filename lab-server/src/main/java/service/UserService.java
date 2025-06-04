package service;

import DataBase.UserDAO;
import authentication.User;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final Map<String, User> authorizedUsers;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        authorizedUsers = new HashMap<>();
    }

    public String register(User user) throws SQLException, NoSuchAlgorithmException {
        userDAO.createUser(user);
        return login(user);
    }

    public String login(User user) throws SQLException, IllegalArgumentException, NoSuchAlgorithmException {
        User userFromDb = userDAO.getUserByUsername(user.getUsername());
        System.out.println(userFromDb);
        if (userFromDb != null && checkPassword(user, userFromDb)) {
            String token = UUID.randomUUID().toString();
            authorizedUsers.put(token, userFromDb);
            return token;
        }
        throw new IllegalArgumentException("Пользователя с таким логином и паролем не существует");
    }

    public User getUserByToken(String token) {
        return authorizedUsers.get(token);
    }

    private boolean checkPassword(User userFromRequest, User userFromDb) {
        String userPassword = userFromRequest.getPassword();
        return PasswordHasher.hash(userPassword).equals(userFromDb.getPassword());
    }
}
