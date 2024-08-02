package gift.administrator.product;

import gift.administrator.category.Category;
import gift.administrator.option.Option;
import gift.administrator.option.OptionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "상품 DTO")
public class ProductDTO {

    private Long id;
    @Schema(description = "상품 이름", example = "춘식이 잠옷")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎ ()\\[\\]+\\-&/_]*$",
        message = "'( ), [ ], +, -, &, /, _'외의 특수문자는 사용할 수 없습니다.")
    @Pattern(regexp = "^(?!.*카카오).*",
        message = "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    @Size(max = 15, message = "공백을 포함하여 15자 이내의 이름을 입력해주세요.")
    @NotBlank(message = "상품 이름을 입력하지 않았습니다.")
    private String name;
    @Schema(description = "상품 가격", example = "20000")
    @Min(value = 0, message = "유효한 값을 입력해주세요")
    @NotNull(message = "가격을 입력하지 않았습니다.")
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "정수만 입력 가능합니다.")
    private int price;
    @Schema(description = "상품 이미지 링크", example = "image.url")
    @NotBlank(message = "이미지 링크를 입력하지 않았습니다.")
    private String imageUrl;
    @Schema(description = "상품이 속한 카테고리 아이디", example = "1")
    @NotNull(message = "카테고리를 선택해야합니다.")
    private Long categoryId;
    @Schema(description = "상품의 옵션", example = "[{\"name\":\"한복 버전\",\"quantity\":\"4\"},{\"name\":\"XL\",\"quantity\":\"2\"}]")
    @Size(min = 1, message = "상품에는 적어도 하나의 옵션이 있어야 합니다.")
    private List<OptionDTO> options = new ArrayList<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, int price, String imageUrl, Long categoryId,
        List<OptionDTO> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

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

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public Product toProduct(ProductDTO productDTO, Category category) {
        Product product = new Product(productDTO.getId(), productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getImageUrl(), category, new ArrayList<>());
        List<Option> options = productDTO.getOptions().stream()
            .map(optionDTO -> optionDTO.toOption(product))
            .toList();
        product.setOption(options);
        return product;
    }

    public static ProductDTO fromProduct(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId(),
            product.getOptions().stream().map(OptionDTO::fromOption).toList());
    }
}
