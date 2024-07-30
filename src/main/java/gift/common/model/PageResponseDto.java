package gift.common.model;

import java.util.List;
import org.springframework.data.domain.Pageable;

public record PageResponseDto<T>(List<T> response, Integer pageNumber, Integer pageSize) {

    public static <T> PageResponseDto<T> of(List<T> response, Pageable pageable) {
        return new PageResponseDto<>(response, pageable.getPageNumber(), pageable.getPageSize());
    }
}
