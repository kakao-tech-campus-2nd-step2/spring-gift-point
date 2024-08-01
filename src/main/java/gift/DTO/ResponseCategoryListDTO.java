package gift.DTO;

import java.util.List;

public record ResponseCategoryListDTO (
        List<ResponseCategoryDTO> categories
){ }
