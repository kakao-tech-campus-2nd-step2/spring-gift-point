package gift.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateProductRequestDTO {

    private static final int MAX_INPUT_LENGTH = 255;

    @NotBlank(message = "이름은 필수 입력 값입니다")
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Double price;

    @Size(max = MAX_INPUT_LENGTH, message = "이미지 URL은 255자를 넘을 수 없습니다.")
    private String imageUrl;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;


    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }
}
