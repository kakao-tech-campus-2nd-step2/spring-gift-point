package gift.model;

import java.util.List;

public record CategoryPageDTO(int pageNumber, int size, long totalElements, List<CategoryDTO> categories) {

}
