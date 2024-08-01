package gift.service;

import gift.constants.Messages;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishRequestDto;
import gift.dto.WishResponse;
import gift.dto.WishResponseDto;
import gift.exception.MemberNotFoundException;
import gift.exception.WishNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, MemberRepository memberRepository,
        ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public WishResponseDto save(String memberEmail, WishRequestDto wishRequestDto) {
        Member member = memberRepository.findByEmail(memberEmail).get();
        Product product = productRepository.findById(wishRequestDto.getProductId()).get();
        Wish newWish = wishRepository.save(new Wish(member, product));
        return new WishResponseDto(newWish.getId(),newWish.getProduct().getId());
    }

    // 모든 wish 반환
    public List<WishResponseDto> findByEmail(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
            .orElseThrow(
                () -> new MemberNotFoundException(Messages.NOT_FOUND_MEMBER_BY_EMAIL_MESSAGE));

        List<Wish> wishes = wishRepository.findByMemberId(member.getId())
            .orElseThrow(() -> new WishNotFoundException(Messages.NOT_FOUND_WISH_MESSAGE));

        return wishes.stream()
            .map(this::convertToWishDto)
            .collect(Collectors.toList());
    }

    // 페이지네이션을 이용하여 wish 반환
    public Page<WishResponseDto> findByEmailPage(String memberEmail, Pageable pageable) {
        Member member = memberRepository.findByEmail(memberEmail)
            .orElseThrow(
                () -> new MemberNotFoundException(Messages.NOT_FOUND_MEMBER_BY_EMAIL_MESSAGE));
        return wishRepository.findAllByMemberIdOrderByCreatedDate(member.getId(), pageable)
            .map(this::convertToWishDto);
    }

    // api 통일
    @Transactional(readOnly = true)
    public Page<WishResponse> getPagedMemberWishesByMemberId(Long memberId, Pageable pageable) {
        Page<Wish> wishPage = wishRepository.findByMemberId(memberId, pageable);
        return wishPage.map(WishResponse::from);
    }

    @Transactional
    public void deleteByUser(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).get();
        wishRepository.deleteById(member.getId());
    }

    public void updateWish(String memberEmail, Long productId, int quantity) {
        Member member = memberRepository.findByEmail(memberEmail).get();
        Wish updateWish = wishRepository.findByMemberIdAndProductId(member.getId(), productId)
            .get();
        updateWish.setQuantity(quantity);
        wishRepository.save(updateWish);
    }

    private WishResponseDto convertToWishDto(Wish wish) {
        return new WishResponseDto(
            wish.getId(),
            wish.getMember().getId(),
            wish.getProduct().getId(),
            wish.getQuantity()
        );
    }

    public Optional<Wish> findByEmailAndProductId(String memberEmail, Long productId) {
        Member member = memberRepository.findByEmail(memberEmail)
            .orElseThrow(
                () -> new MemberNotFoundException(Messages.NOT_FOUND_MEMBER_BY_EMAIL_MESSAGE));

        return wishRepository.findByMemberIdAndProductId(member.getId(), productId);

    }

    public void deleteById(Long id) {
        wishRepository.deleteById(id);
    }
}
