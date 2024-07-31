package gift.service;

import gift.domain.WishProduct;
import gift.domain.WishProduct.Builder;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishProductRepository;
import gift.web.dto.request.wishproduct.CreateWishProductRequest;
import gift.web.dto.request.wishproduct.UpdateWishProductRequest;
import gift.web.dto.response.wishproduct.CreateWishProductResponse;
import gift.web.dto.response.wishproduct.ReadAllWishProductsResponse;
import gift.web.dto.response.wishproduct.ReadWishProductResponse;
import gift.web.dto.response.wishproduct.UpdateWishProductResponse;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final WishProductRepository wishProductRepository;

    public WishProductService(ProductRepository productRepository,
        MemberRepository memberRepository,
        WishProductRepository wishProductRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.wishProductRepository = wishProductRepository;
    }

    /**
     * 위시 리스트에 상품을 추가합니다.
     * 이미 존재하는 WishProduct인 경우 수량만 추가합니다.
     * @param memberId 회원 ID
     * @param request 상품 정보
     * @return 생성된 WishProduct ID
     */
    @Transactional
    public CreateWishProductResponse createWishProduct(Long memberId, CreateWishProductRequest request) {

        // 이미 존재하는 WishProduct인 경우 수량만 추가
        Optional<WishProduct> existingWishProduct = wishProductRepository.findByMemberIdAndProductId(
            memberId, request.getProductId());
        if (existingWishProduct.isPresent()) {
            WishProduct wishProduct = existingWishProduct.get();
            wishProduct.addQuantity(request.getQuantity());

            return CreateWishProductResponse.fromEntity(wishProduct);
        }

        //새로운 위시 상품을 추가
        WishProduct wishProduct = new Builder()
            .member(memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found")))
            .product(productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found")))
            .quantity(request.getQuantity()).build();

        return CreateWishProductResponse.fromEntity(wishProductRepository.save(wishProduct));
    }

    public ReadAllWishProductsResponse readAllWishProducts(Long memberId, Pageable pageable) {
        return new ReadAllWishProductsResponse(
            wishProductRepository.findByMemberId(memberId, pageable)
                .stream()
                .map(ReadWishProductResponse::fromEntity)
                .toList()
        );
    }

    @Transactional
    public UpdateWishProductResponse updateWishProduct(Long wishProductId, UpdateWishProductRequest request) {
        WishProduct wishProduct = wishProductRepository.findById(wishProductId)
            .orElseThrow(NoSuchElementException::new);
        wishProduct.updateQuantity(request.getQuantity());

        return UpdateWishProductResponse.fromEntity(wishProduct);
    }

    @Transactional
    public void deleteWishProduct(Long wishProductId) {
        WishProduct wishProduct = wishProductRepository.findById(wishProductId)
            .orElseThrow(NoSuchElementException::new);
        wishProductRepository.delete(wishProduct);
    }
}
