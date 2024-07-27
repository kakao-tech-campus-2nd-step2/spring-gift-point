package gift.wish.service;

import gift.member.model.Member;
import gift.wish.model.WishDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WishService {
    void createWish(Member member, Long productId);
    List<WishDTO> getWishlistByMemberId(Member member);
    void deleteWish(Long wishId);
    Page<WishDTO> getWishlistByPage(int page, int size, String sortBy, String direction);
}
