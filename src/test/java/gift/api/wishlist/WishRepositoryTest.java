package gift.api.wishlist;

import static org.assertj.core.api.Assertions.assertThat;

import gift.api.wishlist.domain.WishId;
import gift.api.wishlist.repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Test
    void deleteById() {
        WishId wishId = new WishId(1L, 1L);
        assertThat(wishRepository.existsById(wishId)).isTrue();

        wishRepository.deleteById(wishId);

        assertThat(wishRepository.findById(wishId)).isEmpty();
    }
}