package gift.mapper;

import gift.domain.member.Member;
import gift.domain.product.Product;
import gift.domain.wish.Wish;
import gift.web.dto.WishDto;
import org.springframework.stereotype.Component;

@Component
public class WishMapper {

    public WishDto toDto(Wish wish) {
        return new WishDto(wish.getProduct().getId(),
            wish.getCount());
    }

    public Wish toEntity(WishDto wishDto, Member member, Product product) {
        return new Wish(member, product, wishDto.count());
    }
}
