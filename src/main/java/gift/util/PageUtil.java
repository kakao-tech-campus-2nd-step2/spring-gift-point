package gift.util;

import java.util.List;
import org.springframework.data.domain.Sort.Direction;

public class PageUtil {

    private static final String DEFAULT_SORT_BY = "id";
    private static final String DEFAULT_SORT_DIRECTION = "asc";
    private static final int MAX_SIZE = 15;

    public static int validateSize(int size) {
        return Math.min(size, MAX_SIZE);
    }

    public static String[] validateSort(String sort, List<String> validSortBy) {
        String[] sortParams = sort.split(",");
        if (sortParams.length != 2 || !validSortBy.contains(sortParams[0])) {
            return new String[]{DEFAULT_SORT_BY, DEFAULT_SORT_DIRECTION};
        }
        return sortParams;
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
