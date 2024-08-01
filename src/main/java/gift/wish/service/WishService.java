package gift.wish.service;

import gift.member.model.Member;
import gift.wish.domain.Wish;
import gift.wish.dto.WishDTO;
import gift.wish.dto.WishCreateResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishService {
    WishCreateResponse createWish(String token, Long productId);
    List<WishDTO> getWishlistByMemberId(Member member);
    void deleteWish(Long wishId);
    Page<WishDTO> getWishlistByPage(int page, int size, String sortBy, String direction);

    Wish getWishByProductId(Long productId);
}
