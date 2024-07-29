package gift.dto.paging;

public class PagingRequest {
    private int page = 1;
    private int size = 5;

    public PagingRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PagingRequest() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

