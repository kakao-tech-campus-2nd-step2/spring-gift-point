package gift.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequest {

    private Long id;

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-&/_]+$", message = "잘못된 이름입니다.")
    @Size(min = 1, max = 15, message = "잘못된 이름입니다.")
    private String name;

    @NotNull(message = "가격을 입력해주세요")
    private Integer price;

    @NotBlank(message = "이미지 주소를 입력해주세요")
    private String imageUrl;

    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId;

    @NotNull(message = "선물 옵션을 입력해주세요")
    private String giftOptionName;

    @NotNull(message = "선물 옵션 수량을 입력해주세요")
    private Integer giftOptionQuantity;

    public ProductRequest(Long id, String name, Integer price, String imageUrl, Long categoryId,
        String giftOptionName, Integer giftOptionQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.giftOptionName = giftOptionName;
        this.giftOptionQuantity = giftOptionQuantity;
    }

    public Long getId() {
        return id;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public String getGiftOptionName() {
        return giftOptionName;
    }

    public Integer getGiftOptionQuantity() {
        return giftOptionQuantity;
    }
}
