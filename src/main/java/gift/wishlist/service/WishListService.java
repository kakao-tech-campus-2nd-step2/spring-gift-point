package gift.wishlist.service;

import gift.global.dto.PageInfoDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.service.ProductService;
import gift.wishlist.dto.WishListResponseDto;
import gift.wishlist.entity.WishList;
import gift.wishlist.repository.WishListRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductService productService;

    @Autowired
    public WishListService(WishListRepository wishListRepository, ProductService productService) {
        this.wishListRepository = wishListRepository;
        this.productService = productService;
    }

    // 위시리스트 추가하는 핸들러
    @Transactional
    public void insertWishProduct(long productId, long userId) {
        var product = productService.selectFullOptionProduct(productId).toProduct();

        // 검증하기 (내부 id로 검증하면 쓸 데 없는 조인이 일어나서 객체로 확인)
        verifyWishProductAlreadyExistence(product, userId);
        var wishList = new WishList(userId, product);

        // 이미 찜한 경우, 이미 찜된 상품이라고 안내하기
        wishListRepository.save(wishList);
    }

    // 위시리스트를 읽어오는 핸들러
    @Transactional(readOnly = true)
    public List<WishListResponseDto> readWishProducts(long userId, PageInfoDto pageInfoDto) {
        Pageable pageable = pageInfoDto.toPageInfo().toPageRequest();

        // 특정 userId를 갖는 위시리스트 페이지 불러오기
        var wishList = wishListRepository.findByUserId(userId, pageable);
        return wishList.stream().map(WishListResponseDto::fromWishList).toList();
    }

    // 위시리스트에서 제품을 삭제하는 핸들러
    // 자신의 위시리스트인지 검증하는 로직을 추가하였습니다.
    @Transactional
    public void deleteWishProduct(long id, long userId) {
        WishList actualWishList = wishListRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("위시 리스트에 존재하지 않는 제품입니다."));
        verifyDeleteOwnWishProduct(actualWishList, userId);

        wishListRepository.deleteById(id);
    }

    // 주문 후 제품을 삭제하는 핸들러 (주문에서 사용)
    // 이렇게 사용하는 것이 맞는지, 아니면 existence만 만들어서 호출하고 delete를 호출하는 것이 맞는지 궁금합니다.
    @Transactional
    public void orderWishProduct(ProductResponseDto productResponseDto, long userId) {
        var wishList = wishListRepository.findByUserIdAndProduct(userId,
            productResponseDto.toProduct());

        // 위시리스트에 없다면 바로 반환
        if (wishList.isEmpty()) {
            return;
        }

        // 있으면 삭제하기
        deleteWishProduct(wishList.get().getId(), userId);
    }

    // 위시리스트에 제품이 존재하는지 검증
    private void verifyWishProductAlreadyExistence(Product product, long userId) {
        if (wishListRepository.existsByUserIdAndProduct(userId, product)) {
            throw new IllegalArgumentException("이미 찜한 상품입니다.");
        }
    }

    // 삭제하려는 제품이 자신의 것이 맞는지 확인
    private void verifyDeleteOwnWishProduct(WishList wishProduct, long userId) {
        if (wishProduct.getUserId() != userId) {
            throw new IllegalArgumentException("타인의 위시 리스트는 조작할 수 없습니다.");
        }
    }
}
