package gift.service;


import gift.database.JpaMemberRepository;
import gift.database.JpaProductRepository;
import gift.database.JpaWishRepository;
import gift.dto.WishListResponse;
import gift.exceptionAdvisor.exceptions.MemberNoSuchException;
import gift.exceptionAdvisor.exceptions.ProductNoSuchException;
import gift.exceptionAdvisor.exceptions.WishNoSuchException;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishListServiceImpl implements WishListService {

    private final JpaMemberRepository jpaMemberRepository;
    private final JpaWishRepository jpaWishRepository;
    private final JpaProductRepository jpaProductRepository;

    public WishListServiceImpl(JpaMemberRepository jpaMemberRepository,
        JpaWishRepository jpaWishRepository, JpaProductRepository jpaProductRepository) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaWishRepository = jpaWishRepository;
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public void addProduct(long memberId, long productId) {
        Member member = jpaMemberRepository.findById(memberId)
            .orElseThrow(MemberNoSuchException::new);
        Product product = jpaProductRepository.findById(productId).orElseThrow();
        Wish wish = new Wish(member, product);
        member.addWish(wish);
        jpaWishRepository.save(wish);
    }

    @Override
    public void deleteProduct(long memberId, long productId) {
        Member member = jpaMemberRepository.findById(memberId)
            .orElseThrow(MemberNoSuchException::new);
        Product product = jpaProductRepository.findById(productId)
            .orElseThrow(ProductNoSuchException::new);
        Wish wish = jpaWishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(WishNoSuchException::new);
        member.delWish(wish);
        jpaWishRepository.delete(wish);
    }

    @Override
    public void updateProduct(long memberId, long productId, int productValue) {
        Wish wish = jpaWishRepository.findByMemberIdAndProductId(memberId, productId)
            .orElseThrow(WishNoSuchException::new);

        wish.productCountUpdate(productValue);

    }

    @Override
    public WishListResponse getWishList(long memberId) {
        Member member = jpaMemberRepository.findById(memberId)
            .orElseThrow(MemberNoSuchException::new);
        Map<String, Integer> wishList = member.getWishList().stream()
            .collect(Collectors.toMap(Wish::getProductName, Wish::getProductCount));
        return new WishListResponse(member.getId(), wishList);
    }

    @Override
    public WishListResponse getWishListPage(long memberId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Map<String, Integer> wishlist = jpaWishRepository.findByMemberId(memberId, pageable)
            .stream().collect(Collectors.toMap(Wish::getProductName, Wish::getProductCount));
        return new WishListResponse(memberId, wishlist);
    }
}
