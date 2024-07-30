package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProductDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 15, message = "이름은 최대 15자까지 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_()+&/ ]*$",
            message = "이름에는 특수 문자는 (, ), [, ], +, -, &, /, _ 만 사용 가능합니다.")
    private String name;
    private int price;
    private String imageUrl;
    @NotNull(message = "카테고리 ID는 필수 입력 값입니다.")
    private Long categoryId;
    private List<OptionDto> options;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<OptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDto> options) {
        this.options = options;
    }
}
