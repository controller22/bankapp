package shop.mtcoding.bankapp.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountWithdrawReqDto {
    // DTO는 wrapper 클래스로 만들어야 null 체크가 가능해진다.
    private Long amount;
    private String wAccountNumber;
    private String wAccountPassword;
}
