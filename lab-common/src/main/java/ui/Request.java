package ui;

import authentication.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Request implements Serializable {
    private final String commandName;
    private final String[] commandArgs;
    private Object data;

    private String userToken;
    private User user;

    public Request(String commandName, String[] commandArgs) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
    }

    public Request(String commandName, String[] commandArgs, Object data) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
        this.data = data;
    }

    public Request(Request request, User user) {
        this.commandName = request.commandName;
        this.commandArgs = request.commandArgs;
        this.data = request.data;
        this.user = user;
    }

    public String getArgument(int index) {
        if (index >= 0 && index < commandArgs.length) {
            return commandArgs[index];
        }
        return null;
    }


    public boolean hasData() {
        return data != null;
    }
}