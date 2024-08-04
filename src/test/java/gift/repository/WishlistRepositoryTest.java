package gift.repository;

import gift.model.wishlist.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WishlistRepositoryTest {

    private String testEmail = "test@naver.com";
    private Wishlist wishlist;

    @Autowired
    private WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        wishlist = wishlistRepository.save(new Wishlist(testEmail));
    }

    @Test
    public void save() {
        // given
        // when
        Wishlist expect = wishlistRepository.save(wishlist);

        // then
        assertThat(expect.getEmail()).isEqualTo(testEmail);
    }

    @Test
    public void find() {
        // given
        // when
        wishlistRepository.save(wishlist);
        Wishlist expect = wishlistRepository.findByEmail(testEmail).get();

        // then
        assertThat(expect.getEmail()).isEqualTo(testEmail);
    }
}
