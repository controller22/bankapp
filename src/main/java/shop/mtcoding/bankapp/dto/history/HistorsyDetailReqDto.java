package shop.mtcoding.bankapp.dto.history;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistorsyDetailReqDto {
    private Long amount;
    private String balance;
    private String username;
    private String wAccountNumber;
    private String dAccountNumber;
    private String wAccountPassword;
    private Timestamp createdAt;
}
