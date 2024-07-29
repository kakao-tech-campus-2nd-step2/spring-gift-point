package gift.util;

import java.util.List;
import org.springframework.data.domain.Sort.Direction;

public class PageUtil {

    private static final String DEFAULT_SORT_BY = "id";
    private static final int MAX_SIZE = 15;

    public static int validateSize(int size) {
        return Math.min(size, MAX_SIZE);
    }

    public static String validateSortBy(String sortBy, List<String> validSortBy) {
        return (!validSortBy.contains(sortBy)) ? DEFAULT_SORT_BY : sortBy;
    }

    public static Direction validateDirection(String sortDirection) {
        if (sortDirection.equals("내림차순")) {
            sortDirection = "desc";
        }
        SortDirection direction =
            (!sortDirection.equals("desc")) ? SortDirection.ASC : SortDirection.DESC;
        return (direction == SortDirection.ASC) ? Direction.ASC : Direction.DESC;
    }

    public enum SortDirection {
        ASC, DESC;
    }
}
