package ui;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class CommandBase implements Serializable {
    protected String name;
    protected String info;
    protected int argumentCount;
    protected CommandType commandType;
}