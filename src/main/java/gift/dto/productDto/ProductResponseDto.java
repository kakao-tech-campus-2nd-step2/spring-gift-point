package gift.dto.productDto;

public record ProductResponseDto(Long id,
                                 String name,
                                 int price,
                                 String imageUrl,
                                 Long categoryId) { }

