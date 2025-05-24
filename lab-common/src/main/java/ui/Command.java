package ui;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.io.*;

@Getter
public abstract class Command extends CommandBase implements Serializable {

    public Command(String name, String info, int argumentCount, CommandType commandType) {
        this.name = name;
        this.info = info;
        this.argumentCount = argumentCount;
        this.commandType = commandType;
    }


    public boolean isNeedForm() {
        return commandType.isNeedForm();
    }

    public abstract Response execute(Request request);
}
