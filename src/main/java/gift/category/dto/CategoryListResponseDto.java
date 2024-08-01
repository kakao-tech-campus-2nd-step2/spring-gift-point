package gift.category.dto;

import gift.category.domain.*;

import java.util.List;

public record CategoryListResponseDto(List<Category> categories) {
}
