package gift.service;

import gift.dto.WishResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final BasicTokenService basicTokenService;

    public WishService(WishRepository wishRepository,
                       ProductRepository productRepository,
                       MemberRepository memberRepository,
                       BasicTokenService basicTokenService) {

        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.basicTokenService = basicTokenService;

    }

    public Wish findByProductIdAndMemberId(Long productId, Long memberId) {
        Product product = productRepository.findById(productId).get();
        Member member =memberRepository.findById(memberId).get();
        Wish wish = wishRepository.findByProductAndMember(product, member);
        return wish;
    }

    public WishResponseDto save(Long productId, String tokenValue) {

        Long userId = translateIdFrom(tokenValue);
        Member member = memberRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();
        Wish newWish = new Wish(product, member);

        return fromEntity(wishRepository.save(newWish));
    }

    public List<WishResponseDto> getAll(String tokenValue) {
        Long memberId = translateIdFrom(tokenValue);
        Member member = memberRepository.findById(memberId).get();
        List<Wish> wishes = wishRepository.findAllByMember(member);
        List<WishResponseDto> wishResponseDtos = wishes.stream().map(this::fromEntity).toList();
        return wishResponseDtos;
    }

    public WishResponseDto fromEntity(Wish wish) {
        String token = makeTokenFrom(wish.getMemberId());
        return new WishResponseDto(wish.getProductId(), token);
    }

    private String makeTokenFrom(Long userId) {
        return Base64.getEncoder().encodeToString(userId.toString().getBytes());
    }

    public void delete(Long id) throws IllegalAccessException {
        Wish candidateWish = wishRepository.findById(id).get();
        wishRepository.delete(candidateWish);
    }

    private Long translateIdFrom(String tokenValue) {
        return basicTokenService.getUserIdByDecodeTokenValue(tokenValue);
    }

    public Page<WishResponseDto> getWishes(Pageable pageable) {
        return wishRepository.findAll(pageable).map(this::fromEntity);
    }
}
