package gift.dto;

import gift.validation.NameValidator;
import gift.validation.ValidName;
import gift.vo.Category;
import gift.vo.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record ProductRequestDto(
        Long id,

        @NotNull(message = "카테고리를 지정해주세요.")
        Long categoryId,

        @ValidName
        @NotBlank(message = "상품명을 입력해 주세요.")
        String name,

        @NotNull
        @PositiveOrZero(message = "가격은 0원 이상이여야 합니다.")
        int price,

        @NotBlank(message = "상품 이미지를 등록해주세요. ")
        String imageUrl,

        List<@Valid OptionRequestDto> options
) {
    public Product toProduct(Category category) {
        validateProductName();
        return new Product(id, category, name, price, imageUrl);
    }

    private void validateProductName() {
        String nameKakaoErrorMessage = NameValidator.isValidKakaoName(name);
        if(nameKakaoErrorMessage != null) {
            throw new IllegalArgumentException(nameKakaoErrorMessage);
        }
    }
}
