package gift.dto;

import jakarta.validation.constraints.NotEmpty;

public record ProductDto(String categoryName,
                         @NotEmpty(message = "상품 이름은 필수 입력값입니다.")
                         String name,
                         int price,
                         String imageUrl) { }
