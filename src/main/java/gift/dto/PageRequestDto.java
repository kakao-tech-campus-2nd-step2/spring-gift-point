package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestDto {
    @Min(value = 0, message = "page number는 0이상만 가능합니다.")
    protected int page = 0;

    @Min(value = 1, message = "page size의 최소는 1 입니다.")
    @Max(value = 100, message = "page size의 최대는 100 입니다.")
    protected int size = 10;

    // product, wish 분리하기
    @Pattern(regexp = "^(id|name|price|product_price),(asc|desc)$",
        message = "형식은 '(id|name|price|product_price),(asc|desc)' 만 가능합니다.")
    private String sort = "name,asc";


    public Pageable toPageable() {
        String[] parts = sort.split(",");
        String sortProperty = parts[0];
        String sortDirection = parts[1];
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(page, size, Sort.by(direction, sortProperty));
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSort() {
        return sort;
    }
}
