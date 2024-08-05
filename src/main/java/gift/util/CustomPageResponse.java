package gift.util;


import java.util.List;

public class CustomPageResponse<T> {

    private List<T> resultData;
    private int size;
    private int page;
    private int pages;
    private boolean hasNext;
    private long total;

    public CustomPageResponse(List<T> resultData, int page, int pages, boolean hasNext, long total) {
        this.resultData = resultData;
        this.size = resultData.size();
        this.page = page;
        this.pages = pages;
        this.hasNext = hasNext;
        this.total = total;
    }


    public List<T> getResultData() {
        return resultData;
    }

    public void setResultData(List<T> resultData) {
        this.resultData = resultData;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
