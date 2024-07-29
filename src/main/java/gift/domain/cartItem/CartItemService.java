package gift.domain.cartItem;

import gift.domain.Member.Member;
import gift.domain.cartItem.dto.CartItemDTO;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.Member.JpaMemberRepository;
import gift.global.exception.cartItem.CartItemNotFoundException;
import gift.global.exception.product.ProductNotFoundException;
import gift.global.exception.user.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartItemService {

    private final JpaProductRepository productRepository;
    private final JpaCartItemRepository cartItemRepository;
    private final JpaMemberRepository userRepository;
    private final JpaOptionRepository optionRepository;

    public CartItemService(
        JpaProductRepository jpaProductRepository,
        JpaCartItemRepository jpaCartItemRepository,
        JpaMemberRepository jpaMemberRepository,
        JpaOptionRepository jpaOptionRepository
    ) {
        this.userRepository = jpaMemberRepository;
        this.cartItemRepository = jpaCartItemRepository;
        this.productRepository = jpaProductRepository;
        this.optionRepository = jpaOptionRepository;
    }

    /**
     * 장바구니에 상품 ID 추가
     */
    @Transactional
    public int addCartItem(Long memberId, Long productId) {
        Member member = userRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException(memberId));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        // 기존에 존재하면 update
        Optional<CartItem> findCartItem = cartItemRepository.findByMemberIdAndProductId(memberId,
            productId);
        if (findCartItem.isPresent()) {
            return findCartItem.get().addOneMore();
        }
        // 기존에 없었으면 new
        CartItem newCartItem = new CartItem(member, product);
        cartItemRepository.save(newCartItem);
        return newCartItem.getCount();
    }

    /**
     * 장바구니 상품 조회 - 페이징(매개변수별)
     */
    public List<CartItemDTO> getProductsInCartByMemberIdAndPageAndSort(Long memberId, int page,
        int size, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<CartItem> cartItemsPage = cartItemRepository.findAllByMemberId(memberId,
            pageRequest);

        List<CartItemDTO> cartItemDTOS = cartItemsPage.getContent().stream()
            .map(cartItem -> {
                return new CartItemDTO(cartItem);
            })
            .toList();

        // 새 Page 객체 생성
        return cartItemDTOS;
    }

    /**
     * 장바구니 상품 삭제`
     */
    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * 장바구니 상품 수량 수정
     */
    @Transactional
    public int updateCartItem(Long cartItemId, int count) {
        CartItem findCartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new CartItemNotFoundException(cartItemId));

        findCartItem.updateCount(count); // 수량 수정
        return count;
    }

    /**
     * 장바구니에 해당 옵션의 상품이 존재하면 삭제
     */
    public void deleteCartItemIfExists(Long memberId, Long optionId) {
        Option option = optionRepository.findById(optionId).get();
        cartItemRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId())
            .ifPresent(cartItemRepository::delete);
    }
}
