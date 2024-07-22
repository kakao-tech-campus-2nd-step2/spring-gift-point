package gift.wish.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DataJpaTest
@Sql(scripts = "classpath:/static/TestData.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class WishRepositoryTest {
    @Autowired
    private WishRepository wishRepository;

    @Test
    @DisplayName("WishRepository 테스트 Wish 조회")
    void findWishByIdWithUserAndProduct() {
        // given
        Long wishId = 1L;

        // when
        var wish = wishRepository.findWishByIdWithUserAndProduct(wishId);

        // then
        assertTrue(wish.isPresent());
        assertEquals(wishId, wish.get().getId());
    }

    @Test
    @DisplayName("WishRepository 테스트 Wish 조회 & Paging")
    void findWishesByUserIdWithMemberAndProduct() {
        //given
        final Long id = 1L;
        Pageable pageable = Pageable.ofSize(5).first();

        // when
        var wishPages = wishRepository.findWishesByUserIdWithMemberAndProduct(id, pageable);
        var wish = wishPages.getContent();
        // then
        assertEquals(5, wishPages.getContent().size());
        assertEquals(2, wishPages.getTotalPages());
        assertEquals(10, wishPages.getTotalElements());
        assertEquals(0, wishPages.getNumber());
        wish.forEach(w -> assertThat(w.getProduct()).isNotNull());
        wish.forEach(w -> assertThat(w.isOwner(id)).isTrue());
    }
}