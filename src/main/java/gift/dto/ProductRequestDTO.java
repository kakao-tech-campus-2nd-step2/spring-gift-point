package gift.dto;

import gift.model.Category;
import gift.model.Product;
import jakarta.validation.constraints.*;

public class ProductRequestDTO {
    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]\\+\\-\\&/_]*$", message = "이름에 허용되지 않은 특수문자가 포함되어 있습니다.(가능한 특수문자: ( ), [ ], +, -, &, /, _)")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'라는 문구를 사용하시려면 담당 MD에게 문의 부탁드립니다.")
    private String name;

    @NotBlank(message = "Image URL을 입력해주세요.")
    private String imageUrl;

    @NotNull(message = "가격을 입력해주세요.")
    @Min(value = 1, message = "가격은 1 미만이 될 수 없습니다.")
    private Integer price;

    @Min(value = 1, message = "카테고리 ID는 1 이상이어야 합니다.")
    private Long categoryId;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Product toEntity(Category category) {
        return new Product(this.name, this.price, this.imageUrl, category);
    }
}
