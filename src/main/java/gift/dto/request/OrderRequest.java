package gift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotNull(message = "옵션 ID를 입력하세요.")
    private Long optionId;

    @NotNull(message = "수량을 입력하세요")
    @Positive(message = "수량은 양의 정수여야 합니다.")
    private Integer quantity;

    @NotBlank(message = "메시지를 입력하세요.")
    private String message;

    @NotNull(message = "상품 ID를 입력하세요.")
    private Long productId;

    @NotNull(message = "포인트를 입력하세요.")
    private Integer point;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식은 000-0000-0000이어야 합니다.")
    @NotNull(message = "전화번호를 입력하세요.")
    private String phone;

    @NotNull(message = "영수증 여부를 입력하세요.")
    private boolean receipt;

    public OrderRequest(Long optionId, int quantity, String message, Long productId, Integer point, String phone, boolean receipt) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.productId = productId;
        this.point = point;
        this.phone = phone;
        this.receipt = receipt;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPoint() {
        return point;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isReceipt() {
        return receipt;
    }

}
