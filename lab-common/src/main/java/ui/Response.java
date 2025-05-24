package ui;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
public class Response implements Serializable {
    private final Status status;
    private final String message;
    private final Object data;

    @Serial
    private static final long serialVersionUID = 1L;

    public enum Status {
        SUCCESS,
        WARNING,
        ERROR
    }

    private Response(Status status, String message, Object data) {
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.message = message != null ? message : "";
        this.data = data;
    }

    public static Response success(String message) {
        return new Response(Status.SUCCESS, message, null);
    }

    public static Response success(Object object) {
        return new Response(Status.SUCCESS, null, object);
    }

    public static Response success(String message, Object data) {
        return new Response(Status.SUCCESS, message, data);
    }

    public static Response warning(String message) {
        return new Response(Status.WARNING, message, null);
    }

    public static Response error(String message) {
        return new Response(Status.ERROR, message, null);
    }

    public static Response error(String message, Throwable exception) {
        return new Response(Status.ERROR, message + ": " + exception.getMessage(), null);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(String.format("%s:\n%s", status, message));

        if (data != null) {
            if (data instanceof java.util.List<?> list) {
                for (Object o : list) {
                    result.append("\n");
                    result.append(o != null ? o.toString() : "null");
                }
            } else {
                result.append("\n").append(data);
            }
        }

        return result.toString();
    }
}

