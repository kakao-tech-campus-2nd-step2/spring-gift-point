package gift.dto;

public class PageInfoDto {

    private int currentPage;
    private long totalData;
    private int totalPages;

    public PageInfoDto() {
    }

    public PageInfoDto(int currentPage, long totalData, int totalPages) {
        this.currentPage = currentPage;
        this.totalData = totalData;
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getTotalData() {
        return totalData;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
