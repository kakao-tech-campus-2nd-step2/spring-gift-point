package gift.service;

import gift.constants.ErrorMessage;
import gift.dto.MemberDto;
import gift.dto.ProductResponse;
import gift.dto.WishlistResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wishlist;
import gift.jwt.JwtUtil;
import gift.repository.MemberJpaDao;
import gift.repository.ProductJpaDao;
import gift.repository.WishlistJpaDao;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberJpaDao memberJpaDao;
    private final WishlistJpaDao wishlistJpaDao;
    private final ProductJpaDao productJpaDao;
    private final JwtUtil jwtUtil;

    public MemberService(MemberJpaDao memberJpaDao, WishlistJpaDao wishlistJpaDao,
        ProductJpaDao productJpaDao, JwtUtil jwtUtil) {
        this.memberJpaDao = memberJpaDao;
        this.wishlistJpaDao = wishlistJpaDao;
        this.productJpaDao = productJpaDao;
        this.jwtUtil = jwtUtil;
    }

    public String registerMember(MemberDto memberDto) {
        Member member = new Member(memberDto);
        assertUserEmailNotDuplicate(member.getEmail());
        memberJpaDao.save(member);

        return jwtUtil.createJwt(member.getEmail(), 1000 * 60 * 30);
    }

    public String login(MemberDto memberDto) {
        Member queriedMember = findMemberByEmail(memberDto.getEmail());

        if (!queriedMember.isCorrectPassword(memberDto.getPassword())) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_PASSWORD_MSG);
        }
        return jwtUtil.createJwt(queriedMember.getEmail(), 1000 * 60 * 30);
    }

    public long getPoint(String email) {
        return findMemberByEmail(email)
            .getPoint();
    }

    public Page<WishlistResponse> getAllWishlist(String email, Pageable pageable) {
        return wishlistJpaDao.findAllByMember_Email(email, pageable)
            .map(wishlist ->
                new WishlistResponse(wishlist.getId(), new ProductResponse(wishlist.getProduct()))
            );
    }

    public void addWishlist(String email, Long productId) {
        Member member = findMemberByEmail(email);
        Product product = findProductById(productId);

        assertWishlistNotDuplicate(email, productId);
        Wishlist wishlist = new Wishlist(member, product);

        wishlistJpaDao.save(wishlist);
    }

    public void deleteWishlist(String email, Long wishId) {
        wishlistJpaDao.findById(wishId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.WISHLIST_NOT_EXISTS_MSG));

        wishlistJpaDao.deleteById(wishId);
    }

    private Member findMemberByEmail(String member) {
        return memberJpaDao.findByEmail(member)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.MEMBER_NOT_EXISTS_MSG));
    }

    private Product findProductById(Long productId) {
        return productJpaDao.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));
    }

    private void assertWishlistNotDuplicate(String email, Long productId) {
        wishlistJpaDao.findByMember_EmailAndProduct_Id(email, productId)
            .ifPresent(v -> {
                throw new IllegalArgumentException(ErrorMessage.WISHLIST_ALREADY_EXISTS_MSG);
            });
    }

    private void assertUserEmailNotDuplicate(String memberEmail) {
        memberJpaDao.findByEmail(memberEmail)
            .ifPresent(user -> {
                throw new IllegalArgumentException(ErrorMessage.EMAIL_ALREADY_EXISTS_MSG);
            });
    }
}
