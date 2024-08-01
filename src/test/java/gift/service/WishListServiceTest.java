package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.product.ProductPostRequestDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.dto.betweenClient.wish.WishDTO;
import gift.repository.CategoryRepository;
import gift.repository.WishRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class WishListServiceTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberService memberService;

    MemberDTO memberDTO;

    ProductPostRequestDTO productPostRequestDTO;

    ProductRequestDTO productRequestDTO;

    @Mock
    Pageable pageable;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        memberDTO = new MemberDTO("1234@1234.com", "1234", "basic");
        productRequestDTO = new ProductRequestDTO(1L, "제품", 1000,
                "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                "기타");

        productPostRequestDTO = new ProductPostRequestDTO(1L, "제품", 1000,
                "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
                "기타", "옵션1", 10);

        memberService.register(memberDTO);
        productService.addProduct(productPostRequestDTO);

        given(pageable.getPageSize()).willReturn(5);
        given(pageable.getPageNumber()).willReturn(1);
        given(pageable.getSort()).willReturn(Sort.by("id"));
        given(pageable.getPageSize()).willReturn(10);
    }

    @Test
    void addWishes() {
        wishListService.addWishes(memberDTO, productRequestDTO);
        assertThat(wishRepository.count()).isEqualTo(1);
    }

    @Test
    void getWishList() {
        wishListService.addWishes(memberDTO, productRequestDTO);
        List<WishDTO> wishList = wishListService.getWishList(memberDTO, pageable).getContent();

        assertThat(wishList.size()).isEqualTo(1);
    }

    @Test
    void removeWishListProduct() {
        wishListService.addWishes(memberDTO, productRequestDTO);
        assertThat(wishRepository.count()).isEqualTo(1);

        wishListService.removeWishListProduct(memberDTO, 1L);
        assertThat(wishRepository.count()).isEqualTo(0);
    }

    @Test
    void setWishListNumber() {
        wishListService.addWishes(memberDTO, productRequestDTO);
        wishListService.setWishListNumber(memberDTO, productRequestDTO, 10);

        assertThat(wishListService.getWishList(memberDTO, pageable).getContent().getFirst().quantity())
                .isEqualTo(10);
    }
}