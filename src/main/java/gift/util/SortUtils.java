package gift.util;

import org.springframework.data.domain.Sort;

public class SortUtils {

    public static Sort.Order parseSortParameter(String sort) {
        String[] sortParts = sort.split(",");
        if (sortParts.length != 2) {
            throw new IllegalArgumentException("잘못된 sort parameter");
        }
        return new Sort.Order(Sort.Direction.fromString(sortParts[1]), sortParts[0]);
    }
}
