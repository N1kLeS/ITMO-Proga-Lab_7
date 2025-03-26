package ui;

public class Response {
    private final boolean success;
    private final String message;
    private final Object data;

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Response(boolean success, String message) {
        this(success, message, null);
    }

    public Response(String message) {
        this(false, message, null);
    }

    public static Response success(String message, Object data) {
        return new Response(true, message, data);
    }

    public static Response failure(String message) {
        return new Response(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        if (success) {
            return "Успех: " + message + "\n";
        } else {
            return "Ошибка: " + message + "\n";
        }
    }
}

