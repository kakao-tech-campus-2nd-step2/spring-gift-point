package gift.service.wish;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.domain.wish.Wish;
import gift.domain.wish.WishRepository;
import gift.mapper.WishMapper;
import gift.web.dto.WishDto;
import gift.web.exception.MemberNotFoundException;
import gift.web.exception.ProductNotFoundException;
import gift.web.exception.WishProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final WishMapper wishMapper;

    public WishService(WishRepository wishRepository, WishMapper wishMapper, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.wishMapper = wishMapper;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }


    public boolean existsByMemberEmailAndProductId(String memberEmail, Long productId) {
        return wishRepository.existsByMemberEmailAndProductId(memberEmail, productId);
    }


    public Page<WishDto> getWishes(Pageable pageable) {
        return wishRepository.findAll(pageable)
            .map(wishMapper::toDto);
    }

    public Page<WishDto> getWishesByEmail(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 엄슴다"));

        return wishRepository.findAllByMemberId(member.getId(), pageable)
            .map(wishMapper::toDto);
    }

    @Transactional
    public WishDto createWish(String email, WishDto wishDto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 엄슴다"));

        Product product = productRepository.findById(wishDto.productId())
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));

        Wish wish = wishRepository.save(wishMapper.toEntity(wishDto, member, product));
        return wishMapper.toDto(wish);
    }

    @Transactional
    public WishDto updateWish(String email, WishDto wishDto) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 엄슴다"));

        Product product = productRepository.findById(wishDto.productId())
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));

        Wish wish = wishRepository.findByMemberIdAndProductId(member.getId(), wishDto.productId())
                .orElseThrow(() -> new WishProductNotFoundException("위시 제품이 없슴다."));

        wish.updateWish(wishDto.count());

        return wishMapper.toDto(wish);
    }

    public void deleteWish(String email, Long productId) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("멤버가 엄슴다"));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("제품이 없슴다."));

        Wish wish = wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .orElseThrow(() -> new WishProductNotFoundException("위시 제품이 없슴다."));

        wishRepository.delete(wish);
    }
}
