package gift.Service;

import gift.DTO.WishDTO;
import gift.Entity.ProductEntity;
import gift.Entity.UserEntity;
import gift.Entity.WishEntity;
import gift.Repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @InjectMocks
    private WishService wishService;

    public WishServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testGetWishes() {
//        Pageable pageable = PageRequest.of(0, 10);
//        UserEntity user = new UserEntity();
//        user.setId(1L);
//        ProductEntity product1 = new ProductEntity();
//        product1.setId(1L);
//        ProductEntity product2 = new ProductEntity();
//        product2.setId(2L);
//
//        WishEntity wish1 = new WishEntity(1L, user, product1, "Wish1");
//        WishEntity wish2 = new WishEntity(2L, user, product2, "Wish2");
//        Page<WishEntity> wishPage = new PageImpl<>(Arrays.asList(wish1, wish2), pageable, 2);
//
//        when(wishRepository.findAll(pageable)).thenReturn(wishPage);
//
//        Page<WishDTO> result = wishService.getWishes(pageable);
//
//        assertWishPage(wishPage, result);
//    }

    private void assertWishPage(Page<WishEntity> expectedPage, Page<WishDTO> actualPage) {
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getContent().size(), actualPage.getContent().size());
        for (int i = 0; i < expectedPage.getContent().size(); i++) {
            assertWish(expectedPage.getContent().get(i), actualPage.getContent().get(i));
        }
    }

    private void assertWish(WishEntity expected, WishDTO actual) {
        assertEquals(expected.getProductName(), actual.getProductName());
        assertEquals(expected.getUser().getId(), actual.getUserId());
        assertEquals(expected.getProduct().getId(), actual.getProductId());
    }
}
