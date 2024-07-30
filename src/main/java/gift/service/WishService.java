package gift.service;

import gift.dto.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.WishException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;



    public WishService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;

    }

//    public List<WishlistResponseDto> getWishListByMemberId(Long memberId) {
//        List<Wish> wishList = wishRepository.findByMemberId(memberId);
//        return wishList.stream()
//            .map(wish -> new WishlistResponseDto(wish.getId(), wish.getProduct()))
//            .collect(Collectors.toList());
//    }

    public List<Wish> getWishlist(Long memberId) {
        return wishRepository.findByMemberId(memberId);
    }

    public void addWishlist(Long memberId, Long productId) throws WishException {
        Optional<Member> member = memberRepository.findById(memberId);
        Optional<Product> product = productRepository.findById(productId);

        if(member.isEmpty() || product.isEmpty()){
            throw new WishException("존재하지 않는 회원 또는 상품입니다.");
        }
        if (wishRepository.findByMemberIdAndProductId(memberId, productId).isPresent()) {
           throw new WishException("이미 위시리스트에 있는 상품입니다.");
        }
        Wish wish = new Wish(member.get(), product.get());
        wishRepository.save(wish);
    }

    public void deleteWishlist(Long memberId, Long productId) throws WishException {
        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId, productId);
        if (wish.isEmpty()){
            throw new WishException("위시리스트에 존재하지 않는 상품입니다.");
        }
        wishRepository.delete(wish.get());
    }

    public Page<Wish> findByMemberId(Long memberId, Pageable pageable) {
       return wishRepository.findByMemberId(memberId, pageable);
    }
}
