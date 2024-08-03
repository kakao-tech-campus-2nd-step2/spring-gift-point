package gift.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class SortUtil {

    public static Sort createSort(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.unsorted();
        }

        String[] parts = sort.split(",");
        String property = parts[0];
        Sort.Direction direction = Direction.DESC;

        if (parts.length > 1) {
            String directionStr = parts[1].toUpperCase();
            if ("ASC".equals(directionStr)) {
                direction = Sort.Direction.ASC;
            }
        }

        return Sort.by(direction, property);
    }
}