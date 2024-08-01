package gift.service;

import gift.dto.WishListRequestDTO;
import gift.dto.WishListResponseDTO;
import gift.model.Member;
import gift.model.Product;
import gift.model.WishList;
import gift.repository.WishListRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository,
        ProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    // 사용자의 위시 리스트를 조회하는 메서드
    public Page<WishListResponseDTO> getWishlist(String token, PageRequest pageRequest) {
        String email = JwtUtil.extractEmail(JwtUtil.getBearerToken(token));
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Page<WishList> wishLists = wishListRepository.findByMember(member, pageRequest);
        return wishLists.map(WishListResponseDTO::fromEntity);
    }

    // 위시 리스트에 상품을 추가하는 메서드
    public void addProductToWishlist(String token, WishListRequestDTO wishListRequestDTO) {
        String email = JwtUtil.extractEmail(JwtUtil.getBearerToken(token));
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(wishListRequestDTO.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        WishList wishList = new WishList(member, product);
        wishListRepository.save(wishList);
    }

    // 위시 리스트에서 상품을 삭제하는 메서드
    public void removeProductFromWishlist(String token, Long wishListId) {
        String email = JwtUtil.extractEmail(JwtUtil.getBearerToken(token));
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        WishList wishList = wishListRepository.findById(wishListId)
            .orElseThrow(() -> new RuntimeException("위시리스트를 찾을 수 없습니다."));
        if (!wishList.getMember().getEmail().equals(email)) {
            throw new RuntimeException("위시리스트 항목을 삭제할 권한이 없습니다.");
        }
        wishListRepository.deleteById(wishListId);
    }
}
