package gift.global.pagination.dto;

import org.springframework.data.domain.Page;

public record PageResponse(
        int currentPageNum,
        int startPageNum,
        int endPageNum) {
    private final static int FIRST_PAGE_NUM = 1;
    private final static int PAGE_MAX_CAPACITY = 10;

    public PageResponse(Page<?> page) {
        this(
                FIRST_PAGE_NUM + page.getNumber(),
                Math.max(FIRST_PAGE_NUM, FIRST_PAGE_NUM + page.getNumber() - PAGE_MAX_CAPACITY),
                Math.min(page.getTotalPages(), FIRST_PAGE_NUM + page.getNumber() + PAGE_MAX_CAPACITY)
        );
    }

}
