package gift.wish.dto;

import gift.member.domain.Member;
import gift.product.domain.Product;
import gift.wish.domain.ProductCount;
import gift.wish.domain.Wish;

public record WishServiceDto(Long id, Long memberId, Long productId, ProductCount productCount) {
    public Wish toWish(Member member, Product product) {
        return new Wish(id, member, product, productCount);
    }
}
