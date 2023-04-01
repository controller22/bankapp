package shop.mtcoding.bankapp.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Criteria {
     // 특정 페이지 조회를 위한 클래스
    private int page; // 현재 페이지 번호
    private int perPageNum=5; // 페이지당 보여줄 게시글의 개수
    
    public int getPageStart() {
        // 특정 페이지의 범위를 정하는 구간, 현재 페이지의 게시글 시작 번호
        // 0 ~ 5 , 5 ~ 10 이런식으로
        return (page -1) * perPageNum;
    }
 
    public Criteria(Integer nowPge) {
        // 기본 생성자 : 최초 게시판에 진입시 필요한 기본값
        this.page = nowPge;
    }
 
    // 현재 페이지 번호 page : getter, setter
    public int getPage() {
        return page;
    }
 
    public void setPage(int page) {
        if(page <= 0) {
            this.page = 1;
            
        } else {
            this.page = page;
        }    
    }
 
    
    // 페이지당 보여줄 게시글의 개수 perPageNum : getter, setter
    public int getPerPageNum() {
        return perPageNum;
    }
 
    public void setPerPageNum(int perPageNum) {
        int cnt = this.perPageNum;
        
        if(perPageNum != cnt) {
            this.perPageNum = cnt;    
        } else {
            this.perPageNum = perPageNum;
        }
        
    }
}
