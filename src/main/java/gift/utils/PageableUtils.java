package gift.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {
    public static Pageable createPageable(int page, int size, String sortBy) {
        String[] splitSortBy = sortBy.split(",");

        String sortKey = splitSortBy[0];
        String sortDirection = splitSortBy[1];

        Sort sort = Sort.by(Sort.Direction.DESC, sortKey);

        if (sortDirection.equalsIgnoreCase("asc")) {
            sort = Sort.by(Sort.Direction.ASC, splitSortBy[0]);
        }

        return PageRequest.of(page, size, sort);
    }
}
