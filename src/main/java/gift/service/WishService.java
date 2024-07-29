package gift.service;

import gift.exception.ErrorCode;
import gift.domain.Product;
import gift.domain.Wish;
import gift.domain.member.Member;
import gift.dto.ProductDto;
import gift.exception.GiftException;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public List<ProductDto> getProducts(Member member, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMember(member, pageable);

        return wishes.stream()
                .map(Wish::getProduct)
                .map(Product::toDto)
                .toList();
    }

    public void addProduct(Member member, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GiftException(ErrorCode.PRODUCT_NOT_FOUND));

        if (wishRepository.existsByMemberIdAndProductId(member.getId(), productId)) {
            throw new GiftException(ErrorCode.PRODUCT_ALREADY_IN_WISHLIST);
        }

        Wish wish = new Wish(member, product);

        wishRepository.save(wish);
    }

    public void removeProduct(Member member, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GiftException(ErrorCode.PRODUCT_NOT_FOUND));

        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new GiftException(ErrorCode.PRODUCT_NOT_IN_WISHLIST));

        wishRepository.delete(wish);
    }

}
