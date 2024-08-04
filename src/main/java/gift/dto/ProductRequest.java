package gift.dto;

import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ProductRequest {

    private final int page;
    private final int size;
    private final String[] sort;
    private final Long categoryId;

    public ProductRequest(int page, int size, String[] sort, Long categoryId) {
        this.page = page;
        this.size = size;
        this.sort = sort != null ? sort : new String[]{"id", "asc"}; // 기본 정렬 값
        this.categoryId = categoryId;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String[] getSort() {
        return sort;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Sort getSortOrders() {
        return Sort.by(
                Arrays.stream(sort)
                        .map(s -> {
                            String[] parts = s.split(",");
                            return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                        })
                        .collect(Collectors.toList())
        );
    }
}
