package shop.mtcoding.bankapp.dto.account;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bankapp.dto.history.HistoryRespDto;

@Getter
@Setter
public class AccountDetailPageRespDto {
    private List<HistoryRespDto> hDtoList;
    private AccountDetailRespDto aDto;
    private double lastpage;
   
    public AccountDetailPageRespDto(List<HistoryRespDto> hDtoList, AccountDetailRespDto aDto,
    double lastpage) {
        this.hDtoList = hDtoList;
        this.aDto = aDto;
        this.lastpage = lastpage;
    }   
}
