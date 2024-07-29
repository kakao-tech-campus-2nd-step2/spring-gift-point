package gift.dto.request;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;

import java.time.LocalDateTime;

public record OrderRequest(Long optionId, int quantity, String message) {
    public Order toEntity(LocalDateTime orderDateTime, Member member, Option option) {
        return new Order(this.quantity,orderDateTime, this.message, member, option);
    }
}
