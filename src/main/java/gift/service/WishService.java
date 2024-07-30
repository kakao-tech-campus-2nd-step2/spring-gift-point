package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishDto;
import gift.dto.WishResponseDto;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void addWish(Long memberId, WishDto wishDto) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Product product = productRepository.findById(wishDto.productId()).orElseThrow();
        Wish newWish = new Wish(member, product);
        wishRepository.save(newWish);
    }

    @Transactional
    public void deleteWish(Long wishId){
        wishRepository.deleteById(wishId);
    }

    @Transactional(readOnly = true)
    public List<Wish> findByMemberId(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return wishRepository.findByMember(member);
    }

    @Transactional(readOnly = true)
    public Page<WishResponseDto> findByMemberId(Long id, Pageable pageable) {
        Member member = memberRepository.findById(id).orElseThrow();
        Page<Wish> wishPage= wishRepository.findByMember(member, pageable);
        return wishPage.map(WishResponseDto::convertToDto);
    }

    @Transactional
    public void deleteProductFromWishList(Long memberId, Long productId) {
        Optional<Wish> optionalWishList = wishRepository.findByMemberIdAndProductId(memberId, productId);

        if (optionalWishList.isPresent()) {
            Wish wishList = optionalWishList.get();
            wishRepository.delete(wishList);
        }
    }

}
