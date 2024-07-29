package gift;

import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.WishRepository;
import gift.service.WishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WishServiceTest {

    @Autowired
    private WishService wishService;

    @MockBean
    private WishRepository wishRepository;

    private Member member;
    private Product product;
    private Category category;
    private Wish wish;

    @BeforeEach
    public void setup() {
        member = new Member("test@example.com", "password");
        category = new Category(1L,"Food");
        product = new Product("Product Name", 100, "https://cs.kakao.com/images/icon/img_kakaocs.png", category);
        wish = new Wish();
        wish.setMember(member);
        wish.setProduct(product);
    }

    @Test
    public void testAddWish() {
        Mockito.when(wishRepository.save(Mockito.any(Wish.class))).thenReturn(wish);

        Wish savedWish = wishService.addWish(wish);

        assertThat(savedWish).isNotNull();
        assertThat(savedWish.getMember().getEmail()).isEqualTo(member.getEmail());
        assertThat(savedWish.getProduct().getName()).isEqualTo(product.getName());
    }

    @Test
    public void testDeleteWish() {
        Mockito.when(wishRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(wishRepository).deleteById(Mockito.anyLong());

        wishService.deleteWish(1L);

        Mockito.verify(wishRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteWishThrowsExceptionWhenWishNotFound() {
        Mockito.when(wishRepository.existsById(Mockito.anyLong())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> wishService.deleteWish(1L));
    }
}
