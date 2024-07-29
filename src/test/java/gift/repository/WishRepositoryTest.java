package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Wish;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("유저 아이디 기반 위시리스트 반환 테스트 및 페이지 사이즈 테스트")
    void findByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wish> page = wishRepository.findByUserId(1L, pageable);

        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(10);

        assertThat(page.getContent().get(0).getProduct().getName()).isEqualTo("Product 1");
    }


    @Test
    @DisplayName("유저 아이디와 위시 아이디 기반 위시 객체 반환 테스트")
    void findByUserIdAndId() {
        Optional<Wish> expectedWish = wishRepository.findById(1L);
        Optional<Wish> actualWish = wishRepository.findByUserIdAndId(1L, 1L);

        assertThat(actualWish).isPresent();
        assertThat(expectedWish).isPresent().hasValueSatisfying(
            w -> assertThat(w.getId()).isEqualTo(actualWish.get().getId()));
        assertThat(expectedWish).hasValueSatisfying(
            w -> assertThat(w.getProduct().getName()).isEqualTo(
                actualWish.get().getProduct().getName()));
    }

    @Test
    @DisplayName("updateWishNumber JPQL 테스트")
    void updateWishNumber() {
        Optional<Wish> wish = wishRepository.findById(1L);
        assertThat(wish).isPresent();
        wishRepository.updateWishNumber(1L, wish.get().getId(), 30);
        entityManager.flush();
        entityManager.clear();

        Optional<Wish> updatedWish = wishRepository.findByUserIdAndId(1L, wish.get().getId());

        assertThat(updatedWish).isPresent().hasValueSatisfying(
            w -> assertThat(w.getNumber()).isEqualTo(30));
    }
}
