package gift.dto;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UpdateProductDto {
    @NotBlank
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9\\s()\\[\\]+\\-&/\\_]*$", message = "허용된 특수 문자는 (, ), [, ], +, -, &, /, _ 입니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "\"카카오\"가 포함된 상품명은 사용할 수 없습니다.")
    String name;
    @Min(0)
    Integer price;
    @NotBlank
    String imageUrl;
    @NotBlank
    Category category;

    List<Option> options;

    public UpdateProductDto(String updatedProduct, int i, String url, Category category) {
        this.name = updatedProduct;
        this.price = i;
        this.imageUrl = url;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public List<Option> getOptions() {
        return this.options;
    }

    public void setOptions(List<Option> list) {
        this.options = list;
    }

    public  Integer getPrice() {
        return this.price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Category getCategory() {
        return this.category;
    }

    public void update(Product product) {
        product.setName(this.name);
        product.setPrice(this.price);
        product.setImageUrl(this.imageUrl);
        product.setCategory(this.category);
    }
}
