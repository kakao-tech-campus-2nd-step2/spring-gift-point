package gift.product.dto;

import gift.option.dto.GiftOptionRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ProductRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-&/_]+$", message = "잘못된 이름입니다.")
    @Size(min = 1, max = 15, message = "잘못된 이름입니다.")
    private String name;

    @NotNull(message = "가격을 입력해주세요")
    private Integer price;

    @NotBlank(message = "이미지 주소를 입력해주세요")
    private String imageUrl;

    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId;

    private List<GiftOptionRequest> giftOptions;

    public ProductRequest(String name, Integer price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public ProductRequest(String name, Integer price, String imageUrl, Long categoryId, List<GiftOptionRequest> giftOptions) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.giftOptions = giftOptions;
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

    public List<GiftOptionRequest> getGiftOptions() {
        return giftOptions;
    }

    public void setGiftOptions(List<GiftOptionRequest> giftOptions) {
        this.giftOptions = giftOptions;
    }

}
