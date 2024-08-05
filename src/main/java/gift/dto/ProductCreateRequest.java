package gift.dto;

import gift.validation.UniqueOptionNames;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Request DTO for creating a Product")
public class ProductCreateRequest {

    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "(),[],+,-,&,/,_를 제외한 특수 문자 사용은 불가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    @Schema(description = "Name of the product", example = "운동복")
    private String name;
    @Schema(description = "Price of the product", example = "18000")
    private int price;
    @Schema(description = "Image URL of the product", example = "testimage.jpg")
    private String imageUrl;
    @NotNull(message = "상품에는 항상 하나의 카테고리가 있어야 합니다.")
    @Schema(description = "ID of the category", example = "7")
    private Long categoryId;
    @NotEmpty(message = "상품의 옵션은 최소 1개가 있어야 합니다.")
    @Valid
    @UniqueOptionNames(message = "옵션 이름이 중복될 수 없습니다.")
    @Schema(description = "List of options for the product")
    private List<OptionRequest> options = new ArrayList<>();

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, int price, String imageUrl, Long categoryId,
        List<OptionRequest> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }
}
