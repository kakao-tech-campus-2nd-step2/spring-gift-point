package gift.service;

import gift.dto.MemberRequestDTO;
import gift.dto.MemberResponseDTO;
import gift.model.*;
import gift.repository.WishListRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public WishListService(WishListRepository wishListRepository, MemberService memberService,
        ProductService productService) {
        this.wishListRepository = wishListRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    // 사용자의 위시 리스트를 조회하는 메서드
    public List<WishList> getWishlist(MemberRequestDTO memberRequestDTO, int page, int size) {
        Member member = memberService.findMemberEntityByEmail(memberRequestDTO.getEmail());
        return wishListRepository.findByMember(member, PageRequest.of(page, size));
    }

    // 위시 리스트에 상품을 추가하는 메서드
    public void addProductToWishlist(MemberRequestDTO memberRequestDTO, Long productId) {
        Member member = memberService.findMemberEntityByEmail(memberRequestDTO.getEmail());
        Product product = productService.findProductEntityById(productId);
        WishList wishList = new WishList(member, product);
        wishListRepository.save(wishList);
    }

    // 위시 리스트에서 상품을 삭제하는 메서드
    public void removeProductFromWishlist(MemberRequestDTO memberRequestDTO, Long wishListId) {
        WishList wishList = wishListRepository.findById(wishListId)
            .orElseThrow(() -> new RuntimeException("위시리스트를 찾을 수 없습니다."));
        wishListRepository.deleteById(wishListId);
    }
}
