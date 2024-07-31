package gift.controller.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagingResponse <T> (
        List<T> content,
        long totalElements,
        int totalPages,
        int size,
        int number
){
    public static <T> PagingResponse<T> from(Page<T> page) {
        return new PagingResponse<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber()
        );
    }
}
