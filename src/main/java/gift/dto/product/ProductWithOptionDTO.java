package gift.dto.product;

public record ProductWithOptionDTO(Integer id, String name, Integer price, String imageUrl, String option,
                                   String categoryName) {
}