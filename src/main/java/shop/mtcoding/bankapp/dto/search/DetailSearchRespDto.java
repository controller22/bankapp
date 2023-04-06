package shop.mtcoding.bankapp.dto.search;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailSearchRespDto {
    private Integer id;
    private Long amount;
    private Long balance;
    private String sender;
    private String receiver;
    private String searchString;
    private String gubun;
    private Integer localPage;
    private Timestamp createdAt;

    
}

