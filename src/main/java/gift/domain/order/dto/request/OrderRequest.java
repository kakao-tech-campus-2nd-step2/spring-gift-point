package gift.domain.order.dto.request;

import gift.domain.member.Member;
import gift.domain.option.Option;
import gift.domain.order.Order;
import gift.domain.order.ReceiptType;
import gift.domain.product.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OrderRequest(

    @NotNull
    Long productId,
    @NotNull
    Long optionId,
    @NotNull
    @Min(value = 1, message = "차감할 수량의 값은 1 이상이어야 합니다.")
    Long quantity,
    Boolean hasCashReceipt,
    @NotNull
    ReceiptType cashReceiptType,
    @NotNull
    String cashReceiptNumber,
    @NotBlank
    String message,
    @NotNull
    Integer point // 사용 포인트
) {

    public Order toOrder(Member member, Product product, Option option) {
        return new Order(
            member, // 주문자
            product, // 주문 상품
            option.getName(), // 상품 옵션 이름
            this.quantity, // 상품 수량
            LocalDateTime.now(), // 주문 시각
            product.getPrice() * this.quantity.intValue(), // 총 주문 금액
            product.getImageUrl(), // 상품 이미지 주소
            this.hasCashReceipt, // 현금 영수증 발행 유무
            this.cashReceiptType, // 현금 영수증 타입
            this.cashReceiptNumber, // 핸드폰 번호
            this.message // 주문 메시지
        );
    }
}
