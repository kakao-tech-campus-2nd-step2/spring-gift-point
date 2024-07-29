package gift.service;

import gift.domain.Wish;
import gift.domain.Member;
import gift.domain.Product;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private WishRepository wishRepository;
    private MemberRepository memberRepository;
    private ProductRepository productRepository;

    public WishService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<WishResponse> getWishes(Long memberId, PageRequest pageRequest) {
        return wishRepository.findByMemberId(memberId, pageRequest)
                .map(wish -> new WishResponse(wish.getId(), wish.getProduct().getName(), wish.getMember().getId()));
    }

    public WishResponse addWish(WishRequest wishRequest, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        Product product = productRepository.findByName(wishRequest.getProductName()).orElseThrow(() -> new IllegalArgumentException("Invalid product name"));

        Wish wish = new Wish(product, member);
        wishRepository.save(wish);
        return new WishResponse(wish.getId(), wish.getProduct().getName(), wish.getMember().getId());
    }

    public void removeWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }
}
