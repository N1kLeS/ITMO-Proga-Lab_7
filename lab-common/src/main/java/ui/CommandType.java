package ui;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public enum CommandType implements Serializable {
    WITHOUT_ARGUMENTS(0, false, "Команда без аргументов"),
    WITH_ARGUMENT(0, false, "Команда с одним аргументом"),
    WITH_MANY_ARGUMENTS(1, false, "Команда с несколькими аргументами"),
    WITH_FORM(-1, true,"Команда с формой данных"),
    WITH_ARGUMENT_FORM(1, true, "Команда с аргументом и формой");

    private final int argumentCount;
    private final boolean needForm;
    private final String description;

    @Serial
    private static final long serialVersionUID = 1L;

    CommandType(int argumentCount, boolean needForm, String description) {
        this.argumentCount = argumentCount;
        this.needForm = needForm;
        this.description = description;
    }


    public boolean validateArguments(int actualArguments) {
        if (argumentCount == -1) return true;
        return actualArguments == argumentCount;
    }
}
