package gift.dto;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateProductDto {

    @NotBlank
    @Size(min = 1, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9\\s()\\[\\]+\\-&/\\_]*$", message = "허용된 특수 문자는 (, ), [, ], +, -, &, /, _ 입니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "\"카카오\"가 포함된 상품명은 사용할 수 없습니다.")
    private String name;

    @NotNull
    @Min(0)
    private Integer price;

    @NotBlank
    private String imageUrl;

    @NotNull
    private Category category;

    private List<OptionDto> options;

    public CreateProductDto(String productionName, Integer productPrice, String productImageUrl, Category category) {
        this.name = productionName;
        this.price = productPrice;
        this.imageUrl = productImageUrl;
        this.category = category;
        this.options = new ArrayList<>();
    }

    public Product toProduct() {
        Product product = new Product(name, price, imageUrl, category);
        List<Option> optionList = options.stream()
                .map(optionDto -> new Option(optionDto.getName(), product))
                .collect(Collectors.toList());
        product.setOptions(optionList);
        return product;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public List<OptionDto> getOptions() { return options; }
    public void setOptions(List<OptionDto> options) { this.options = options; }
}
