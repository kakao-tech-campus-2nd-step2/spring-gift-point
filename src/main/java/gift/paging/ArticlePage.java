package gift.paging;

import org.springframework.data.domain.Page;

public class ArticlePage {

    private int totalDocs; //전체 문서 수
    private int docsPerPage; //페이지 당 문서 수
    private int totalPages; //전체 페이지 수
    private int currentPage; //현재 페이지 번호
    private int startPage; //현재 페이징에서 시작 페이지 번호
    private int endPage; //현재 페이징에서 종료 페이지 번호
    private int showingPageCounts; //페이징 개수, 한번에 보여줄 페이지 수
    private boolean hasNext; // 다음 페이지가 있는가?
    private boolean hasPrevious; // 이전 페이지가 있는가?

    public ArticlePage(Page<?> paging, int page, int docsPerPage, int showingPageCounts) {
        this.totalDocs = (int) paging.getTotalElements();
        this.currentPage = page;
        this.docsPerPage = 10;
        this.showingPageCounts = 10;
        this.hasNext = paging.hasNext();
        this.hasPrevious = paging.hasPrevious();

        if (totalDocs == 0) {
            this.totalPages = 0;
            this.startPage = 0;
            this.endPage = 0;
            this.hasNext = false;
            this.hasPrevious = false;
        } else {
            this.totalPages = isExistRest(totalDocs, docsPerPage);
            this.startPage = setStartPage(currentPage, showingPageCounts);
            this.endPage = setEndPage(this.startPage);
        }
    }

    private int isExistRest(int totalDocs, int docsPerPage) {
        return totalDocs % docsPerPage == 0 ? totalDocs / docsPerPage : totalDocs / docsPerPage + 1;
    }

    private int setStartPage(int currentPage, int pagingCount) {
        return currentPage % pagingCount == 0 ? (currentPage / pagingCount) * pagingCount + 1
            - pagingCount : (currentPage / pagingCount) * pagingCount + 1;
    }

    private int setEndPage(int startPage) {
        return Math.min((startPage + showingPageCounts - 1), totalPages);
    }

    public long getTotalDocs() {
        return totalDocs;
    }

    public int getDocsPerPage() {
        return docsPerPage;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getStartPage() {
        return startPage;
    }

    public long getEndPage() {
        return endPage;
    }

    public int getShowingPageCounts() {
        return showingPageCounts;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }
}
