package gift.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;

import gift.domain.product.dto.OptionRequest;
import gift.domain.product.dto.ProductReadAllResponse;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import gift.domain.product.entity.Category;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.CategoryJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.domain.wishlist.repository.WishlistJpaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AutoConfigureMockMvc
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private CategoryJpaRepository categoryJpaRepository;

    @MockBean
    private OptionService optionService;

    @MockBean
    private WishlistJpaRepository wishlistJpaRepository;

    private static final List<OptionRequest> optionRequestDtos = List.of(
        new OptionRequest("수박맛", 969),
        new OptionRequest("자두맛", 90)
    );
    private static final ProductRequest PRODUCT_REQUEST_DTO = new ProductRequest(1L, "탕종 블루베리 베이글", 3500, "https://image.istarbucks.co.kr/upload/store/skuimg/2023/09/[9300000004823]_20230911131337469.jpg", optionRequestDtos);
    private static final Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");

    @Test
    @DisplayName("상품 생성 서비스 테스트")
    void create() {
        // given
        Product expected = PRODUCT_REQUEST_DTO.toProduct(category);

        given(categoryJpaRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(productJpaRepository.save(any(Product.class))).willReturn(expected);
        doNothing().when(optionService).create(any(Product.class), any());

        // when
        ProductResponse actual = productService.create(PRODUCT_REQUEST_DTO);

        // then
        assertThat(actual).isEqualTo(ProductResponse.from(expected));
    }

    @Test
    @DisplayName("상품 전체 조회 서비스 테스트")
    void readAll() {
        // given
        List<Product> products = List.of(
            new Product(1L, category, "책", 20000, "https://example.com/book.jpg"),
            new Product(2L, category, "스마트폰", 800000, "https://example.com/smartphone.jpg"),
            new Product(3L, category, "키보드", 50000, "https://example.com/keyboard.jpg"),
            new Product(4L, category, "마우스", 30000, "https://example.com/mouse.jpg"),
            new Product(5L, category, "이어폰", 60000, "https://example.com/earphone.jpg"),
            new Product(6L, category, "헤드폰", 100000, "https://example.com/headphone.jpg"),
            new Product(7L, category, "카메라", 300000, "https://example.com/camera.jpg"),
            new Product(8L, category, "그래픽 카드", 80000, "https://example.com/gpu.jpg"),
            new Product(9L, category, "메모리", 150000, "https://example.com/memory.jpg"),
            new Product(10L, category, "모니터", 400000, "https://example.com/monitor.jpg"),
            new Product(11L, category, "마우스 패드", 2000, "https://example.com/mousepad.jpg"),
            new Product(12L, category, "키링", 10000, "https://example.com/keyring.jpg"),
            new Product(13L, category, "백팩", 70000, "https://example.com/backpack.jpg"),
            new Product(14L, category, "의자", 150000, "https://example.com/chair.jpg"),
            new Product(15L, category, "커피머신", 25000, "https://example.com/coffee-machine.jpg"),
            new Product(16L, category, "라면", 3000, "https://example.com/ramen.jpg"),
            new Product(17L, category, "스피커", 80000, "https://example.com/speaker.jpg"),
            new Product(18L, category, "텔레비전", 500000, "https://example.com/television.jpg"),
            new Product(19L, category, "게임기", 350000, "https://example.com/game-console.jpg"),
            new Product(20L, category, "블루투스 스피커", 9000, "https://example.com/bluetooth-speaker.jpg"),
            new Product(21L, category, "선풍기", 40000, "https://example.com/fan.jpg"),
            new Product(22L, category, "에어컨", 700000, "https://example.com/air-conditioner.jpg"),
            new Product(23L, category, "냉장고", 900000, "https://example.com/fridge.jpg"),
            new Product(24L, category, "전기밥솥", 150000, "https://example.com/rice-cooker.jpg"),
            new Product(25L, category, "전자레인지", 100000, "https://example.com/microwave.jpg"),
            new Product(26L, category, "믹서기", 80000, "https://example.com/blender.jpg"),
            new Product(27L, category, "청소기", 200000, "https://example.com/vacuum.jpg"),
            new Product(28L, category, "다리미", 30000, "https://example.com/iron.jpg"),
            new Product(29L, category, "세탁기", 600000, "https://example.com/washer.jpg"),
            new Product(30L, category, "건조기", 500000, "https://example.com/dryer.jpg"),
            new Product(31L, category, "전기포트", 25000, "https://example.com/kettle.jpg"),
            new Product(32L, category, "토스터기", 30000, "https://example.com/toaster.jpg"),
            new Product(33L, category, "헤어드라이기", 40000, "https://example.com/hair-dryer.jpg"),
            new Product(34L, category, "헤어스타일러", 70000, "https://example.com/hair-styler.jpg"),
            new Product(35L, category, "수세미", 2000, "https://example.com/scrubber.jpg"),
            new Product(36L, category, "비타민", 30000, "https://example.com/vitamin.jpg"),
            new Product(37L, category, "운동화", 120000, "https://example.com/sneakers.jpg")
        );
        List<String> expectedNames = products.stream().map(Product::getName).toList();
        given(categoryJpaRepository.findById(eq(1L))).willReturn(Optional.of(category));
        given(productJpaRepository.findAllByCategory(any(Category.class), any(Pageable.class))).willReturn(
            new PageImpl<>(products, PageRequest.of(0, 5), products.size()));

        // when
        Page<ProductReadAllResponse> actual = productService.readAll(1L, PageRequest.of(0, 5));
        List<String> actualNames = actual.getContent().stream().map(ProductReadAllResponse::name).toList();

        // then
        assertAll(
            () -> assertThat(actual.getSize()).isEqualTo(5),
            () -> assertThat(actualNames). isEqualTo(expectedNames),
            () -> assertThat(actual.getTotalElements()).isEqualTo(37),
            () -> assertThat(actual.getTotalPages()).isEqualTo(8)
        );
    }

    @Test
    @DisplayName("상품 조회 서비스 테스트")
    void readById() {
        // given
        Product expected = PRODUCT_REQUEST_DTO.toProduct(category);
        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(expected));

        // when
        ProductResponse actual = productService.readById(1L);

        // then
        assertThat(actual).isEqualTo(ProductResponse.from(expected));
    }

    @Test
    @DisplayName("상품 수정 서비스 테스트")
    void update() {
        // given
        Product product = PRODUCT_REQUEST_DTO.toProduct(category);
        List<OptionRequest> optionUpdateDtos = List.of(
          new OptionRequest("사과맛", 80),
          new OptionRequest("치즈맛", 100)
        );
        ProductRequest productRequest = new ProductRequest(1L, "아이스 카페 라떼 T", 4500, "https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[110563]_20210426095937947.jpg", optionUpdateDtos);
        Product expected = productRequest.toProduct(category);

        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(categoryJpaRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(productJpaRepository.save(any(Product.class))).willReturn(expected);

        // when
        ProductResponse actual = productService.update(1L, productRequest);

        // then
        assertThat(actual).isEqualTo(ProductResponse.from(expected));
    }

    @Test
    @DisplayName("상품 삭제 서비스 테스트")
    void delete() {
        // given
        Product product = PRODUCT_REQUEST_DTO.toProduct(category);

        given(productJpaRepository.findById(anyLong())).willReturn(Optional.of(product));
        willDoNothing().given(wishlistJpaRepository).deleteAllByProductId(anyLong());
        willDoNothing().given(productJpaRepository).delete(any(Product.class));

        given(categoryJpaRepository.findById(eq(1L))).willReturn(Optional.of(category));

        // when
        productService.delete(1L);

        // then
        Page<ProductReadAllResponse> productList = productService.readAll(1L, PageRequest.of(0, 10));
        assertThat(productList).isEmpty();
    }
}