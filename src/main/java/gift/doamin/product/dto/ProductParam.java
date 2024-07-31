package gift.doamin.product.dto;

import gift.doamin.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "상품 정보")
public class ProductParam {

    @Schema(description = "상품 id")
    private Long id;

    @Schema(description = "등록한 사용자 id")
    private Long userId;

    @Schema(description = "상품의 카테고리 id")
    private Long categoryId;

    @Schema(description = "상품명")
    private String name;

    @Schema(description = "상품 가격")
    private Integer price;

    @Schema(description = "상품 이미지 url")
    private String imageUrl;

    @Schema(description = "상품의 옵션 목록")
    private List<OptionParam> options;

    public ProductParam(Product product) {
        this.id = product.getId();
        this.userId = product.getUser().getId();
        this.categoryId = product.getCategory().getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.options = product.getOptions().stream()
            .map(option -> new OptionParam(option.getId(), option.getName(), option.getQuantity()))
            .toList();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<OptionParam> getOptions() {
        return options;
    }
}
