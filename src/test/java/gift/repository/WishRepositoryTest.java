package gift.repository;

import gift.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("위시 레포티토리 단위테스트")
class WishRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private WishRepository wishRepository;

    private Member testMember;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        testMember = new Member("test@email.com", "password");
        testEntityManager.persist(testMember);

        Category testCategory = new Category("음식", "테스트", "테스트", "테스트");
        testEntityManager.persist(testCategory);

        testProduct1 = new Product("almond", 500, "almond.jpg", testCategory, List.of(new Option("optoin1", 1)));
        testProduct2 = new Product("ice", 900, "ice.jpg", testCategory, List.of(new Option("optoin1", 1)));
        testEntityManager.persist(testProduct1);
        testEntityManager.persist(testProduct2);
    }

    @Test
    @DisplayName("특정 멤버의 위시 전체 조회")
    void findAllByMemberIdWithProduct() {
        //Given
        Wish testWish1 = new Wish(testMember, 100, testProduct1);
        Wish testWish2 = new Wish(testMember, 2000, testProduct2);
        wishRepository.save(testWish1);
        wishRepository.save(testWish2);

        //When
        PageRequest pageable = PageRequest.of(0, 10);
        List<Wish> wishes = wishRepository.findAllByMember(testMember, pageable).getContent();

        //Then
        assertThat(wishes).isNotEmpty()
                .hasSize(2)
                .containsExactly(testWish1, testWish2);

        assertThat(testWish1.getProduct()).isEqualTo(testProduct1);
    }

    @Test
    @DisplayName("멤버와 상품으로 위시 조회")
    void findByMemberAndProduct() {
        //Given
        Wish testWish1 = new Wish(testMember, 100, testProduct1);
        wishRepository.save(testWish1);

        //When
        Optional<Wish> wish = wishRepository.findByMemberAndProduct(testMember, testProduct1);

        //Then
        assertThat(wish).isPresent()
                .hasValueSatisfying(w -> {
                    assertThat(w).isEqualTo(testWish1);
                    assertThat(w.getProduct()).isEqualTo(testProduct1);
                });
    }

    @Nested
    @DisplayName("위시 엔티티 테스트")
    class EntityTest {
        @Test
        @DisplayName("생성된 날짜")
        void dateCheck() {
            Wish wish = new Wish(testMember, 100, testProduct1);

            Wish save = wishRepository.save(wish);

            assertThat(save.getCreatedTime())
                    .isInstanceOf(LocalDateTime.class);
        }

    }
}
