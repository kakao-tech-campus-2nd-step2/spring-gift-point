package gift.service;

import gift.domain.member.Member;
import gift.domain.option.Option;
import gift.domain.wishlist.WishList;
import gift.domain.wishlist.WishListRequest;
import gift.domain.wishlist.WishListResponse;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public List<Option> findByMemberId(Long memberId) {
        return wishListRepository.findByMember(new Member(memberId))
            .stream()
            .map(WishList::getOption)
            .toList();
    }

    public List<WishListResponse> findByMemberId(Long memberId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return wishListRepository.findByMember(new Member(memberId), pageable)
            .getContent()
            .stream()
            .map(WishListResponse::new)
            .toList();
    }

    @Transactional
    public WishList save(Long memberId, WishListRequest wishListRequest) {
        return wishListRepository.save(wishListRequest.toWishList(memberId));
    }

    @Transactional
    public void delete(Long memberId, WishListRequest wishListRequest) {
        WishList wishList = wishListRepository.findByMemberAndOption(new Member(memberId),
            new Option(wishListRequest.optionId())).orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "해당 위시 리스트를 찾을 수 없습니다"));
        wishListRepository.delete(wishList);
    }
}
