package shop.mtcoding.bankapp.dto.search;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailSearchAllRespDto {
    private  List<DetailSearchRespDto> dsDto;
    private double lastpage;

    
    public DetailSearchAllRespDto(List<DetailSearchRespDto> dsDto, double lastpage) {
        this.dsDto = dsDto;
        this.lastpage = lastpage;
    }

}
