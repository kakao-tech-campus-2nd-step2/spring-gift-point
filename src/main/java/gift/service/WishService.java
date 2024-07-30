package gift.service;

import gift.dto.ProductResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.exception.NotFoundException;
import gift.exception.auth.UnauthorizedException;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public Wish makeWish(WishRequest request, Member member) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException("해당 productId의 상품을 찾을 수 없습니다."));
        Wish wish = new Wish(product, member);
        wishRepository.save(wish);
        return wish;
    }

    public Page<WishResponse> getWishesByMember(Member member, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMember(member, pageable);
        Page<WishResponse> wishResponses = wishes.map(wish -> new WishResponse(
                wish.getId(),
                wish.getProduct().getId(),
                wish.getProduct().getName(),
                wish.getProduct().getPrice(),
                wish.getProduct().getImageUrl(),
                wish.getProduct().getCategory()
        ));
        return wishResponses;
    }

    @Transactional
    public void deleteWish(Long id, Member member) {
        Wish wish = wishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 wish가 없습니다."));
        if (wish.getMember().getId() == member.getId()) {
            wishRepository.delete(wish);
            return;
        }
        throw new UnauthorizedException("해당 id의 wish는 본인의 wish가 아닙니다.");
    }

    public void deleteWishByOrder(Member member, Product product) {
        wishRepository.deleteByMemberAndProduct(member, product);
    }
}
