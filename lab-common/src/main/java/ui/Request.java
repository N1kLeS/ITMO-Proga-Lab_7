package ui;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class Request implements Serializable {
    private final String commandName;
    private final String[] commandArgs;
    private Object data;

    @Serial
    private static final long serialVersionUID = 1L;

    public Request(String commandName, String[] commandArgs) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
    }

    public Request(String commandName, String[] commandArgs, Object data) {
        this.commandName = commandName;
        this.commandArgs = commandArgs;
        this.data = data;
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