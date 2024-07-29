package gift.controller.dto;

public class PaginationDTO {

    private String sortBy = "id";
    private String sortDirection = "desc";
    private int size = 10;
    private int page = 0;

    public String getSortBy() {
        return sortBy;
    }


    public String getSortDirection() {
        return sortDirection;
    }


    public int getSize() {
        return size;
    }


    public int getPage() {
        return page;
    }

}
