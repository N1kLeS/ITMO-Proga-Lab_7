package ui;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public abstract class CommandBase implements Serializable {
    protected String name;
    protected String info;
    protected CommandType commandType;
    protected boolean isServerCommand;
    protected boolean requiredAuth;
}