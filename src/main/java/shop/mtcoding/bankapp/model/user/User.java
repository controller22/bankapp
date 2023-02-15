package shop.mtcoding.bankapp.model.user;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private Timestamp createdAt;
}
