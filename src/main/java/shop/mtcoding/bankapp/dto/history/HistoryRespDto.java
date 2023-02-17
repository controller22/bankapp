package shop.mtcoding.bankapp.dto.history;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryRespDto {
    private Integer id;
    private Long amount;
    private Long balance;
    private String sender;
    private String receiver;
    private Timestamp createdAt;
}
