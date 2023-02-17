package shop.mtcoding.bankapp.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransferReqDto {
    private Long amount;
    private String wAccountNumber;
    private String dAccountNumber;
    private String wAccountPassword;
}
