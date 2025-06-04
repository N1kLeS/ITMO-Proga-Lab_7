package commands.auth;

import authentication.User;
import service.UserService;
import ui.Command;
import ui.CommandType;
import ui.Request;
import ui.Response;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginCommand extends Command {
    private final UserService userService;

    public LoginCommand(UserService userService) {
        super("login", "Команда регистрации", true, CommandType.WITH_ARGUMENTS(2), false);
        this.userService = userService;
    }

    @Override
    public Response execute(Request request) {
        User user = new User(request.getArgument(0), request.getArgument(1));
        try {
            return Response.success(userService.login(user));
        } catch (SQLException | NoSuchAlgorithmException e) {
            return Response.error(e.getMessage());
        }
    }
}
