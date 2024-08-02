package gift.mapper;

import gift.domain.member.Member;
import gift.domain.product.Product;
import gift.domain.wish.Wish;
import gift.web.dto.wish.WishRequestDto;
import gift.web.dto.wish.WishResponseDto;
import org.springframework.stereotype.Component;

@Component
public class WishMapper {

    public WishResponseDto toDto(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getProduct().getId());
    }

    public Wish toEntity(Member member, Product product) {
        return new Wish(member, product);
    }
}
