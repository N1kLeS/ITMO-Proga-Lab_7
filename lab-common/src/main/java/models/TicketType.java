package models;

import java.io.Serial;
import java.io.Serializable;

public enum TicketType implements Serializable {
    CHEAP,
    BUDGETARY,
    USUAL;

    @Serial
    private static final long serialVersionUID = 1L;
}
