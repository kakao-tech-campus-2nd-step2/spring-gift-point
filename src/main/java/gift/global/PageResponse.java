package gift.global;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import org.springframework.data.domain.Pageable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageResponse<T>(
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages,
    List<T> contents
) {

    public static <T> PageResponse<T> of(Pageable pageable, Long totalElements, Integer totalPages,
        List<T> contents) {
        return new PageResponse<>(pageable.getPageNumber(), pageable.getPageSize(),
            totalElements, totalPages, contents);
    }
}
