package gift.dto;

import java.util.List;

public class ProductResponse {
    private List<ProductDto> content;
    private PaginationInfo pagination;



    // Getters and Setters
    public List<ProductDto> getContent() {
        return content;
    }

    public void setContent(List<ProductDto> content) {
        this.content = content;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }
}