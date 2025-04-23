import java.io.Serializable;

public class SaveCommand implements Command {
    private static final long serialVersionUID = 1L;

    @Override
    public String getName() {
        return "save";
    }
}
