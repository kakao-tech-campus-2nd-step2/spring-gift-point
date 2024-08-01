package gift.dto;

import gift.validation.ValidProductName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public class ProductDTO {

    private Long id;

    @NotEmpty(message = "이름을 1자 이상 입력하세요")
    @Size(max = 15, message = "이름은 최대 15자까지 입력 가능합니다")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_\\s]*$", message = "사용 불가능한 특수 문자가 포함되어 있습니다")
    @ValidProductName
    private String name;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private int price;

    @NotNull
    @URL(message = "유효한 URL 형식이 아닙니다")
    private String imageUrl;

    @NotNull
    private Long categoryId;

    private String categoryName;

    @NotNull
    private List<OptionDTO> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }
}
