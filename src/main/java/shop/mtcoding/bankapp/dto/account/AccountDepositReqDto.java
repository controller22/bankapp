package shop.mtcoding.bankapp.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDepositReqDto {
    // DTO는 똑같은게 존재해도 공유X
    // 이유는 DTO 화면에 나타나는 데이터(자주 변경될 수 있음)
    private Long amount;
    private String dAccountNumber;
}
