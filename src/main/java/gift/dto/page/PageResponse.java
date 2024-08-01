package gift.dto.page;

import java.util.List;

public record PageResponse<T>(Integer page, Integer size, Long totalElements, Integer totalPages, List<T> content) {
}
