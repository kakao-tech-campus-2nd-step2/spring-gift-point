package gift.api.wishlist;

import static org.assertj.core.api.Assertions.assertThat;

import gift.api.wishlist.repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Test
    void deleteById() {
        // given
        Long id = 1L;
        assertThat(wishRepository.existsById(id)).isTrue();

        // when
        wishRepository.deleteById(id);

        // then
        assertThat(wishRepository.findById(id)).isEmpty();
    }
}