package shop.mtcoding.bankapp.paging;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private Criteria cri; // 현재 페이지 정보
    private int totalCount; // 전체 게시글 수
    private int startPage; // 시작 페이지 번호
    private int endPage; // 끝 페이지 번호
    private boolean prev; // 이전 페이지 여부
    private boolean next; // 다음 페이지 여부

    public Paging(Integer page) {
        this.cri = new Criteria(page);
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        calcData();
    }

    public Criteria getCri() {
        return cri;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getStartPage() {
        return startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public boolean isPrev() {
        return prev;
    }

    public boolean isNext() {
        return next;
    }

    private void calcData() {
        endPage = (int) (Math.ceil(cri.getPageStart() / 10.0)) * 10;
        startPage = endPage - 9;
        int tempEndPage = (int) (Math.ceil(totalCount * 1.0 / cri.getPerPageNum()));
        if (endPage > tempEndPage) {
            endPage = tempEndPage;
        }
        prev = startPage == 1 ? false : true;
        next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
    }
}
