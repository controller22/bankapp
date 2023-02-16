package shop.mtcoding.bankapp.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinReqDto {
    private String username;
    private String password;
    private String fullname;
}
