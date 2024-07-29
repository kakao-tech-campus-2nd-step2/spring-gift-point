package gift.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<E>(
    List<E> responses,
    int page,
    int size,
    int totalPage
) {

    public static <E, T> PageResponse<E> from(List<E> response, Page<T> page) {
        return new PageResponse<>(
            response,
            page.getNumber(),
            page.getSize(),
            page.getTotalPages()
        );
    }
}
