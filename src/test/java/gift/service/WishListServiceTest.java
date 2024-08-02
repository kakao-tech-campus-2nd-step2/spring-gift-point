package gift.service;

import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.DTO.Wish.WishProductRequest;
import gift.DTO.Wish.WishProductResponse;
import gift.TestUtil;
import gift.domain.Category;
import gift.domain.Product;
import gift.domain.User;
import gift.domain.WishProduct;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class WishListServiceTest {
    @Mock
    private WishListRepository wishListRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private WishListService wishListService;

    @Test
    @DisplayName("saveTestWhenNew")
    void test1() {
        User user = new User("user1@email.com", "aaaaa");
        TestUtil.setId(user, 1L);
        Product product = new Product("product", 15000, "url", new Category("물품"));
        TestUtil.setId(product, 1L);
        WishProductRequest wishProductRequest = new WishProductRequest(
                new UserResponse(user),
                new ProductResponse(product)
        );

        given(wishListRepository.existsByUserIdAndProductId(1L, 1L)).willAnswer(
                invocation -> false
        );

        WishProduct wishProduct = new WishProduct(user, product);
        TestUtil.setId(wishProduct, 1L);
        given(userRepository.findById(1L)).willAnswer(invocation -> Optional.of(user));
        given(productRepository.findById(1L)).willAnswer(invocation -> Optional.of(product));
        given(wishListRepository.save(any(WishProduct.class))).willAnswer(invocation -> wishProduct);
        // when
        WishProductResponse savedWishProduct = wishListService.addWishList(wishProductRequest);
        //then
        Assertions.assertThat(savedWishProduct.getWishId()).isEqualTo(1L);
        Assertions.assertThat(savedWishProduct.getName()).isEqualTo("product");
        Assertions.assertThat(savedWishProduct.getPrice()).isEqualTo(15000);
        Assertions.assertThat(savedWishProduct.getImageUrl()).isEqualTo("url");
    }

    @Test
    @DisplayName("saveTestWhenExist")
    void test2(){
        User user = new User("user1@email.com", "aaaaa");
        TestUtil.setId(user, 1L);
        Product product = new Product("product", 15000, "url", new Category("물품"));
        TestUtil.setId(product, 1L);
        WishProductRequest wishProductRequest = new WishProductRequest(
                new UserResponse(user),
                new ProductResponse(product)
        );

        given(wishListRepository.existsByUserIdAndProductId(1L, 1L)).willAnswer(
                invocation -> true
        );

        WishProduct wishProduct = new WishProduct(user, product);
        given(wishListRepository.findByUserIdAndProductId(1L, 1L)).willAnswer(
                invocation -> wishProduct
        );
        // when
        WishProductResponse savedWishProduct = wishListService.addWishList(wishProductRequest);
        //then
        Assertions.assertThat(savedWishProduct.getWishId()).isEqualTo(1L);
        Assertions.assertThat(savedWishProduct.getName()).isEqualTo("product");
        Assertions.assertThat(savedWishProduct.getPrice()).isEqualTo(15000);
        Assertions.assertThat(savedWishProduct.getImageUrl()).isEqualTo("url");
    }

    @Test
    @DisplayName("findWishListASCTest")
    void test3(){
        List<WishProduct> wishList = new ArrayList<>();
        User user = new User("user@email.com", "aaaaa");
        TestUtil.setId(user, 1L);
        for (int i = 1; i <= 5; i++) {
            Product product = new Product("product" + i, 4000, "url" + i, new Category("신규"));
            WishProduct wishProduct = new WishProduct(user, product);
            TestUtil.setId(wishProduct, (long) i);
            wishList.add(wishProduct);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), wishList.size());
        List<WishProduct> subList = wishList.subList(start, end);

        Page<WishProduct> page = new PageImpl<>(subList, pageable, wishList.size());
        given(wishListRepository.findByUserId(1L, pageable)).willAnswer(invocation -> page);
        // when
        Page<WishProductResponse> pageResult = wishListService.findWishListASC(1L, 0, 5, "id");
        // then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.get().count()).isEqualTo(5);
        List<WishProductResponse> content = pageResult.getContent();
        for(int i = 1; i <= 5; i++){
            Long id = content.get(i-1).getWishId();
            Assertions.assertThat(id).isEqualTo((long)i);
        }
    }

    @Test
    @DisplayName("findWishListDESCTest")
    void test4(){
        List<WishProduct> wishList = new ArrayList<>();
        User user = new User( "user@email.com", "aaaaa");
        TestUtil.setId(user, 1L);
        for (int i = 1; i <= 5; i++) {
            Product product = new Product("product" + i, 4000, "url" + i, new Category("신규"));
            WishProduct wishProduct = new WishProduct(user, product);
            TestUtil.setId(wishProduct, (long) i);
            wishList.add(wishProduct);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(0, 5, Sort.by(sorts));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), wishList.size());
        List<WishProduct> subList = wishList.subList(start, end);

        Page<WishProduct> page = new PageImpl<>(subList, pageable, wishList.size());
        given(wishListRepository.findByUserId(1L, pageable)).willAnswer(invocation -> page);
        // when
        Page<WishProductResponse> pageResult = wishListService.findWishListDESC(1L, 0, 5, "id");
        // then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.get().count()).isEqualTo(5);
        List<WishProductResponse> content = pageResult.getContent();
        for(int i = 5; i >= 1; i--){
            Long id = content.get(i-1).getWishId();
            Assertions.assertThat(id).isEqualTo((long)i);
        }
    }

    @Test
    @DisplayName("updateWishProductTest")
    void test5(){
        // given
        User user = new User("user@email.com", "aaaaa");
        Product product = new Product("product", 4500, "url", new Category("신규"));
        WishProduct wishProduct = new WishProduct(user, product);
        given(wishListRepository.findByUserIdAndProductId(anyLong(), anyLong())).willAnswer(
                invocation -> wishProduct
        );
        // when
        wishListService.updateWishProduct(1L, 1L);
        // then
        Assertions.assertThat(wishProduct.getProduct()).isEqualTo(product);
        Assertions.assertThat(wishProduct.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("deleteWishProductTestWhenOneLeft")
    void test6(){
        // given
        User user = new User( "user@email.com", "aaaaa");
        Product product = new Product("product", 4500, "url", new Category("신규"));
        WishProduct wishProduct = new WishProduct(user, product);
        given(wishListRepository.findById(anyLong())).willAnswer(invocation -> Optional.of(wishProduct));
        // when
        wishListService.deleteWishProduct(1L);
        // then
        then(wishListRepository).should(times(1)).deleteById(1L);
    }
}
