package gift.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.Category;
import gift.domain.Product;
import gift.domain.User;
import gift.domain.Wish;
import gift.repository.JpaCategoryRepository;
import gift.repository.JpaProductRepository;
import gift.repository.JpaUserRepository;
import gift.repository.JpaWishRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaWishRepositoryTest {
    //TODO: id잘못됐을 때 에러
    @Autowired
    private JpaWishRepository jpaWishRepository;
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaProductRepository jpaProductRepository;
    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private List<Long> userIdList = new ArrayList<>();
    private List<Wish> wishList;

    private Long insertWish(Wish wish) {
        return jpaWishRepository.save(wish).getId();
    }

    private void insertAllWishList(List<Wish> wishList){
        jpaWishRepository.saveAll(wishList);
    }

    @BeforeEach
    void setWish() {

        User user1 = new User("www.naver.com", "1234", "일반");
        User user2 = new User("www.daum.net", "1234", "관리자");

        Long insertedUserId1 = jpaUserRepository.save(user1).getId();
        Long insertedUserId2 = jpaUserRepository.save(user2).getId();

        userIdList.add(insertedUserId1);
        userIdList.add(insertedUserId2);
        Category category = new Category("교환권", "#6c95d1", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "");
        jpaCategoryRepository.save(category);

        List<Product> products = List.of(
            new Product("사과", 12000, "www.naver.com", category),
            new Product("바나나", 15000, "www.daum.net", category),
            new Product("포도", 10000, "www.kakao.net", category),
            new Product("토마토", 5000, "www.kakao.com", category)
        );

        jpaProductRepository.saveAll(products);

        wishList = List.of(
            new Wish(user1, products.get(0), 10),
            new Wish(user1, products.get(1), 5),
            new Wish(user1, products.get(2), 15),
            new Wish(user1, products.get(3), 20),

            new Wish(user2, products.get(0), 10),
            new Wish(user2, products.get(1), 5)
        );
    }

    @Test
    void 위시_추가() {
        //given
        //when
        Long insertWishId = insertWish(wishList.get(0));
        Wish findWish = jpaWishRepository.findById(insertWishId).get();
        //then
        assertAll(
            () -> assertThat(findWish).isEqualTo(wishList.get(0))
        );
    }

    @Test
    void 위시_조회() {
        //given
        Long insertWishId = insertWish(wishList.get(0));
        //when
        Wish findWish = jpaWishRepository.findById(insertWishId).get();
        //then
        assertAll(
            () -> assertThat(findWish.getId()).isNotNull(),
            () -> assertThat(findWish.getId()).isEqualTo(insertWishId),
            () -> assertThat(findWish.getCount()).isEqualTo(10),

            () -> assertThrows(NoSuchElementException.class,
                () -> jpaWishRepository.findById(100L).get())
        );
    }

    @Test
    void 위시리스트_조회() {
        //given
        insertAllWishList(wishList);
        //when
        List<Wish> wishList1 = jpaWishRepository.findAllByUserId(userIdList.get(0));
        List<Wish> wishList2 = jpaWishRepository.findAllByUserId(userIdList.get(1));
        //then
        assertAll(
            () -> assertThat(wishList1).hasSize(4),
            () -> assertThat(wishList2).hasSize(2)
        );
    }

    @Test
    void 위시_삭제() {
        //given
        Long insertWishId = insertWish(wishList.get(0));
        //when
        Wish findWish = jpaWishRepository.findById(insertWishId).get();
        jpaWishRepository.delete(findWish);
        //then
        List<Wish> wishList = jpaWishRepository.findAllByUserId(1L);
        assertAll(
            () -> assertThat(wishList).hasSize(0)
        );
    }

    @Test
    void 상품_삭제() {
        //given
        Long insertWishId = insertWish(wishList.get(0));
        //when
        Wish findWish = jpaWishRepository.findById(insertWishId).get();
        jpaWishRepository.delete(findWish);
        //then
        List<Wish> productList = jpaWishRepository.findAll();
        assertAll(
            () -> assertThat(productList).hasSize(0)
        );
    }
}