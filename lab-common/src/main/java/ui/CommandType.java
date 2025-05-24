package ui;

import lombok.Getter;
import models.AbstractModel;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class CommandType implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int argumentCount;
    private final Class<? extends AbstractModel> formClass;


    private CommandType(int argumentCount, Class<? extends AbstractModel> formClass) {
        this.argumentCount = argumentCount;
        this.formClass = formClass;
    }

    public static CommandType WITHOUT_DATA() {
        return new CommandType(0, null);
    }

    public static CommandType WITH_ARGUMENTS(int argumentCount) {
        return new CommandType(argumentCount, null);
    }

    public static CommandType WITH_FORM(Class<? extends AbstractModel> formClass) {
        return new CommandType(0, formClass);
    }

    public static CommandType WITH_ARGUMENTS_AND_FORM(int argumentCount, Class<? extends AbstractModel> formClass) {
        return new CommandType(argumentCount, formClass);
    }


    public boolean hasForm() {
        return formClass != null;
    }
}