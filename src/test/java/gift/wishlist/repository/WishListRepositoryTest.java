package gift.wishlist.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.member.MemberFixture;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.product.ProductFixture;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wishlist.WishListFixture;
import gift.wishlist.entity.WishList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("위시 리스트 리파지토리 테스트")
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Member member = MemberFixture.createMember("wishListRepo@test.com", "password");
        memberRepository.save(member);

        List<Product> products = List.of(
                ProductFixture.createProduct("상품1", 1000, "image1"),
                ProductFixture.createProduct("상품2", 2000, "image2"),
                ProductFixture.createProduct("상품3", 3000, "image3"),
                ProductFixture.createProduct("상품4", 4000, "image4")
        );
        productRepository.saveAll(products);

        // 임의의 위시리스트 3개
        List<WishList> wishLists = List.of(
                WishListFixture.createWishList(member, products.get(0), 4),
                WishListFixture.createWishList(member, products.get(1), 1),
                WishListFixture.createWishList(member, products.get(2), 3)
        );
        wishListRepository.saveAll(wishLists);
    }

    @Test
    @DisplayName("위시리스트 생성")
    void addWishList() {
        //given
        Member member = memberRepository.findAll().getFirst();      // 기존 회원
        Product product = productRepository.findAll().getLast();    // 아직 위시리스트에 없는 상품
        WishList wishList = WishListFixture.createWishList(member, product, 2);

        //when
        WishList savedWishList = wishListRepository.save(wishList);

        //then
        assertAll(
                () -> assertNotNull(savedWishList.getId()),
                () -> assertEquals(wishList.getMember().getId(), savedWishList.getMember().getId()),
                () -> assertEquals(wishList.getProduct().getId(), savedWishList.getProduct().getId()),
                () -> assertEquals(wishList.getQuantity(), savedWishList.getQuantity())
        );
    }

    @Test
    @DisplayName("위시리스트 조회")
    void findWishList() {
        // when
        List<WishList> wishLists = wishListRepository.findAll();

        // then
        assertEquals(3, wishLists.size());

        wishLists.forEach(
                wishList -> assertAll(
                        () -> assertNotNull(wishList.getId()),
                        () -> assertNotNull(wishList.getMember()),
                        () -> assertNotNull(wishList.getProduct()),
                        () -> assertNotNull(wishList.getQuantity())
                )
        );

        // 존재하지 않는 위시리스트 조회
        assertFalse(wishListRepository.existsByMemberIdAndProductId(1L, 4L));
    }

    @Test
    @DisplayName("위시리스트 수정")
    void updateWishList() {
        //given
        WishList wishList = wishListRepository.findAll().getLast();
        int quantityToAdded = 3;

        // 기존 수량 저장
        int beforeQuantity = wishList.getQuantity();

        //when
        wishList.addQuantity(quantityToAdded);

        //then
        WishList updatedWishList = wishListRepository.findById(wishList.getId()).get();

        assertAll(
                () -> assertEquals(beforeQuantity + quantityToAdded, updatedWishList.getQuantity()),
                () -> assertEquals(wishList.getMember().getId(), updatedWishList.getMember().getId()),
                () -> assertEquals(wishList.getProduct().getId(), updatedWishList.getProduct().getId()),
                () -> assertEquals(wishList.getId(), updatedWishList.getId())
        );
    }

    @Test
    @DisplayName("위시리스트 삭제")
    void deleteWishList() {
        //given
        WishList wishList = wishListRepository.findAll().getLast();

        //when
        wishListRepository.deleteById(wishList.getId());

        //then
        assertFalse(wishListRepository.existsById(wishList.getId()));
    }
}
