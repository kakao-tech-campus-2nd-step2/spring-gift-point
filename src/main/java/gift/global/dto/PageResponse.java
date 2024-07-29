package gift.global.dto;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content,
    Integer page,
    Integer size,
    Integer totalPage,
    Integer totalSize
) {

    public static <T, U> PageResponse<T> from(Page<U> page, Function<U, T> mapper) {
        return new PageResponse<>(
            page.map(mapper).getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalPages(),
            (int) page.getTotalElements()
        );

    }
}
