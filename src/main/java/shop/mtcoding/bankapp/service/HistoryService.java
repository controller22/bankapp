package shop.mtcoding.bankapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.mtcoding.bankapp.dto.search.DetailSearchRespDto;
import shop.mtcoding.bankapp.model.history.HistoryRepository;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    public List<DetailSearchRespDto> 거래내역검색(String gubun, int id, int PageStart,
    int PerPageNum,  String SearchString,Integer localpage) {
        System.out.println("디버깅22222");
        List<DetailSearchRespDto> detailList = historyRepository.findBySender(gubun, id, PageStart, PerPageNum,
        SearchString, localpage);
        System.out.println("디버깅22222");
return detailList;
}

}