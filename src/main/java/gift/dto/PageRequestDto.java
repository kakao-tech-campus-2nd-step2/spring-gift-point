package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestDto {
    @Min(value = 0, message = "page number는 0이상만 가능합니다.")
    protected int pageNumber = 0;

    @Min(value = 1, message = "page size의 최소는 1 입니다.")
    @Max(value = 100, message = "page size의 최대는 100 입니다.")
    protected int pageSize = 10;

    // product, wish 분리하기
    @Pattern(regexp = "id|name|price|product_price", message = "정렬 방식은 id, name, price만 가능합니다.")
    private String sortBy = "id";

    public Pageable toPageable() {
        return PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
