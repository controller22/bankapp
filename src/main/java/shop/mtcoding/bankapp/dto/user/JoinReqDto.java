package shop.mtcoding.bankapp.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bankapp.model.user.User;

@Setter
@Getter
public class JoinReqDto {
    private String username;
    private String password;
    private String fullname;

    public User toModel() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);
        return user;
    }
}
