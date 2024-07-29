package gift.core;

import java.util.List;

public class PagedDto<T> {
    private final Integer page;
    private final Integer size;
    private final Long totalElements;
    private final Integer totalPages;
    private final List<T> contents;

    public PagedDto(
            Integer page,
            Integer size,
            Long totalElements,
            Integer totalPages,
            List<T> contents
    ) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.contents = contents;
        this.totalPages = totalPages;
    }

    public Integer page() {
        return page;
    }

    public Integer size() {
        return size;
    }

    public Long totalElements() {
        return totalElements;
    }

    public List<T> contents() {
        return contents;
    }

    public Integer totalPages() {
        return totalPages;
    }
}
