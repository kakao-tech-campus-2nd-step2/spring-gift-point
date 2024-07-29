package gift.utils;

import gift.controller.dto.PaginationDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PaginationUtils {

    public static final int MAX_SIZE = 20;
    public static final String DEFAULT_SORT_FIELD = "id";

    private static final Map<String, List<String>> VALID_SORT_FIELDS = Map.of(
        "wishlist", Arrays.asList("id", "quantity"),
        "product", Arrays.asList("id", "name", "price", "imageUrl")
    );

    public static Pageable createPageable(PaginationDTO paginationDTO, String entityType) {
        int size = Math.min(paginationDTO.getSize(), MAX_SIZE);

        Sort.Direction direction = paginationDTO.getSortDirection().equalsIgnoreCase("asc")
            ? Sort.Direction.ASC : Sort.Direction.DESC;

        List<String> validFields = VALID_SORT_FIELDS.getOrDefault(entityType,
            Arrays.asList(DEFAULT_SORT_FIELD));
        String sortBy = validFields.contains(paginationDTO.getSortBy())
            ? paginationDTO.getSortBy() : DEFAULT_SORT_FIELD;

        return PageRequest.of(paginationDTO.getPage(), size, Sort.by(direction, sortBy));
    }
}
