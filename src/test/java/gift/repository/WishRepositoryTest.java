package gift.repository;

import gift.domain.Wish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Test
    void findWishByMemberId() {
        List<Wish> wishes = wishRepository.findWishByMemberId(1L);
        wishes.stream()
                .map(Wish::getProduct)
                .forEach(System.out::println);
    }
}