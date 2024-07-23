package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class ProductRegisterRequestDto {
    @Size(min = 1, max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$",
        message = "허용 가능한 특수문자는 ( ), [ ], +, -, &, /, _ 입니다.")
    @Pattern(regexp = "^(?!.*카카오).*$",
        message = "\"카카오\"가 포함된 문구는 담당 MD와 협의가 필요합니다.")
    private String name;
    private int price;
    private String imageUrl;
    private String categoryName;
    private Set<OptionDto> options = new HashSet<>();

    public ProductRegisterRequestDto() {
    }

    public ProductRegisterRequestDto(String name, int price, String imageUrl, String categoryName) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<OptionDto> getOptions() {
        return options;
    }
}