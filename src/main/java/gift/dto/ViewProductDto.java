package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public class ViewProductDto {
    @Length(min = 1, max = 15, message = "상품의 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_]*$",
            message = "이름에는 ( ), [ ], +, -, &, /, _ 외의 특수문자는 입력할 수 없습니다!"
    )
    @NotBlank(message = "상품의 이름은 필수항목입니다.")
    private String name;

    @NotBlank(message = "상품 사진은 필수항목입니다.")
    private String imgUrl;

    @NotNull(message = "상품 가격은 필수항목입니다.")
    @Min(value = 0, message = "상품의 가격은 0원 이상이어야합니다.")
    private Integer price;

    @NotNull(message = "상품 카테고리는 필수항목입니다.")
    private Long category;

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}
