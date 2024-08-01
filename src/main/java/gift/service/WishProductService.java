package gift.service;

import gift.dto.product.ProductBasicInformation;
import gift.dto.wishproduct.WishProductPageResponse;
import gift.dto.wishproduct.WishProductRequest;
import gift.dto.wishproduct.WishProductResponse;
import gift.exception.AlreadyExistsException;
import gift.exception.BadRequestException;
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

    public WishProductResponse addWishProduct(WishProductRequest wishProductRequest, Long memberId) {
        var product = productRepository.findById(wishProductRequest.productId())
                .orElseThrow(() -> new NotFoundElementException(wishProductRequest.productId() + "를 가진 상품이 존재하지 않습니다."));
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 멤버가 존재하지 않습니다."));
        if (wishProductRepository.existsByProductAndMember(product, member)) {
            throw new AlreadyExistsException("이미 위시리스트에 존재하는 상품입니다.");
        }
        var wishProduct = saveWishProductWithWishProductRequest(product, member);
        return getWishProductResponseFromWishProduct(wishProduct);
    }

    @Transactional(readOnly = true)
    public WishProductResponse getWishProduct(Long memberId, Long id) {
        var wishProduct = findWishProductById(id);
        if (!wishProduct.getMember().getId().equals(memberId)) {
            throw new BadRequestException("다른 사람의 위시 리스트는 접근할 수 없습니다.");
        }
        return getWishProductResponseFromWishProduct(wishProduct);
    }

    @Transactional(readOnly = true)
    public WishProductPageResponse getWishProducts(Long memberId, Pageable pageable) {
        var pageResult = wishProductRepository.findAllByMemberId(memberId, pageable);
        var wishProducts = pageResult
                .getContent()
                .stream()
                .map(this::getWishProductResponseFromWishProduct)
                .toList();

        return new WishProductPageResponse(pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements(), pageResult.getTotalPages(), wishProducts);
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

    private WishProduct saveWishProductWithWishProductRequest(Product product, Member member) {
        var wishProduct = new WishProduct(product, member);
        return wishProductRepository.save(wishProduct);
    }

    private WishProductResponse getWishProductResponseFromWishProduct(WishProduct wishProduct) {
        var product = wishProduct.getProduct();
        var productBasicInformation = ProductBasicInformation.of(product.getId(), product.getName(), product.getPrice());
        return WishProductResponse.of(wishProduct.getId(), productBasicInformation);
    }

    private WishProduct findWishProductById(Long id) {
        return wishProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 위시 리스트가 존재하지 않습니다."));
    }
}
