package gift.application.wish.dto;

import gift.model.member.Member;
import gift.model.product.Product;
import gift.model.wish.Wish;

public class WishCommand {

    public record Register(
        Long productId,
        Long count
    ) {

        public Wish toEntity(Member member, Product product) {
            return new Wish(null, member, product, count);
        }
    }

    public record Update(
        Long productId,
        Long count
    ) {

    }
}
