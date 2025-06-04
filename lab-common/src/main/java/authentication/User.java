package authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Data
@ToString
public class User implements Serializable {
    private final Long id;
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.id = null;
    }
}
