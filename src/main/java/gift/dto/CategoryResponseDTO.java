package gift.dto;

import gift.model.Category;

import java.util.List;

public record CategoryResponseDTO(List<Category> categoryList) {
}
