package gift.service;

import gift.dto.product.ProductBasicInformation;
import gift.dto.wishproduct.WishProductAddRequest;
import gift.dto.wishproduct.WishProductResponse;
import gift.dto.wishproduct.WishProductUpdateRequest;
import gift.exception.NotFoundElementException;
import gift.model.Member;
import gift.model.Product;
import gift.model.WishProduct;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WishProductService {

    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishProductService(WishProductRepository wishProductRepository, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishProductRepository = wishProductRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public WishProductResponse addWishProduct(WishProductAddRequest wishProductAddRequest, Long memberId) {
        var product = productRepository.findById(wishProductAddRequest.productId())
                .orElseThrow(() -> new NotFoundElementException(wishProductAddRequest.productId() + "를 가진 상품이 존재하지 않습니다."));
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 멤버가 존재하지 않습니다."));
        if (wishProductRepository.existsByProductAndMember(product, member)) {
            return updateWishProductWithProductAndMember(product, member, wishProductAddRequest.quantity());
        }
        var wishProduct = saveWishProductWithWishProductRequest(product, member, wishProductAddRequest.quantity());
        return getWishProductResponseFromWishProduct(wishProduct);
    }

    public void updateWishProduct(Long id, WishProductUpdateRequest wishProductUpdateRequest) {
        var wishProduct = findWishProductById(id);
        if (wishProductUpdateRequest.quantity() == 0) {
            deleteWishProduct(id);
            return;
        }
        updateWishProductWithQuantity(wishProduct, wishProductUpdateRequest.quantity());
    }

    @Transactional(readOnly = true)
    public List<WishProductResponse> getWishProducts(Long memberId, Pageable pageable) {
        return wishProductRepository.findAllByMemberId(memberId, pageable)
                .stream()
                .map(this::getWishProductResponseFromWishProduct)
                .toList();
    }

    public void deleteWishProduct(Long wishProductId) {
        if (!wishProductRepository.existsById(wishProductId)) {
            throw new NotFoundElementException("존재하지 않는 위시 리스트의 ID 입니다.");
        }
        wishProductRepository.deleteById(wishProductId);
    }

    public void deleteAllByProductId(Long productId) {
        wishProductRepository.deleteAllByProductId(productId);
    }

    public void deleteAllByMemberId(Long memberId) {
        wishProductRepository.deleteAllByMemberId(memberId);
    }

    public void deleteAllByMemberIdAndProductId(Long memberId, Long productId) {
        wishProductRepository.deleteAllByMemberIdAndProductId(memberId, productId);
    }

    private WishProduct saveWishProductWithWishProductRequest(Product product, Member member, Integer quantity) {
        var wishProduct = new WishProduct(product, member, quantity);
        return wishProductRepository.save(wishProduct);
    }

    private WishProductResponse updateWishProductWithProductAndMember(Product product, Member member, Integer quantity) {
        var wishProduct = wishProductRepository.findByProductAndMember(product, member)
                .orElseThrow(() -> new NotFoundElementException(product.getId() + "를 가진 상품과, " + member.getId() + "를 가진 멤버를 가진 위시 리스트가 존재하지 않습니다."));
        var updateQuantity = wishProduct.getQuantity() + quantity;
        var updatedWishProduct = updateWishProductWithQuantity(wishProduct, updateQuantity);
        return getWishProductResponseFromWishProduct(updatedWishProduct);
    }

    private WishProduct updateWishProductWithQuantity(WishProduct wishProduct, Integer updateQuantity) {
        wishProduct.updateQuantity(updateQuantity);
        wishProductRepository.save(wishProduct);
        return wishProduct;
    }

    private WishProductResponse getWishProductResponseFromWishProduct(WishProduct wishProduct) {
        var product = wishProduct.getProduct();
        var productBasicInformation = ProductBasicInformation.of(product.getId(), product.getName(), product.getPrice());
        return WishProductResponse.of(wishProduct.getId(), productBasicInformation, wishProduct.getQuantity());
    }

    private WishProduct findWishProductById(Long id) {
        return wishProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 위시 리스트가 존재하지 않습니다."));
    }
}
