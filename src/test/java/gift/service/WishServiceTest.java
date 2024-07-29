package gift.service;

import gift.dto.*;
import gift.entity.Product;
import gift.entity.ProductName;
import gift.entity.User;
import gift.entity.Wish;
import gift.exception.BusinessException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WishServiceTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WishService wishService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @AfterEach
    public void tearDown() {
        wishRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Rollback
    public void 위시리스트_추가_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        User user = userRepository.save(new User("user@example.com", "password"));
        Product product = productRepository.save(new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product1.jpg", categoryService.getCategoryEntityById(category)));
        ProductResponseDto productResponseDto = productService.getProductById(product.getId());

        WishRequestDto requestDto = new WishRequestDto(productResponseDto.getId());
        WishResponseDto createdWish = wishService.addWish(user.getId(), requestDto);

        assertNotNull(createdWish);
        assertNotNull(createdWish.getId());
        assertEquals(productResponseDto.getId(), createdWish.getProductId());
        assertEquals(productResponseDto.getName(), createdWish.getProductName());
        assertEquals(productResponseDto.getPrice(), createdWish.getProductPrice());
        assertEquals(productResponseDto.getImageUrl(), createdWish.getProductImageUrl());
    }

    @Test
    @Rollback
    public void 위시리스트_조회_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        User user = userRepository.save(new User("user@example.com", "password"));
        Product product = productRepository.save(new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product1.jpg", categoryService.getCategoryEntityById(category)));
        wishRepository.save(new Wish(user, product));

        WishPageResponseDto wishPage = wishService.getWishesByUserId(user.getId(), PageRequest.of(0, 10));

        assertNotNull(wishPage);
        assertEquals(1, wishPage.getTotalItems());
        WishResponseDto retrievedWish = wishPage.getWishes().get(0);
        assertEquals(product.getId(), retrievedWish.getProductId());
        assertEquals(product.getName().getValue(), retrievedWish.getProductName());
        assertEquals(product.getPrice(), retrievedWish.getProductPrice());
        assertEquals(product.getImageUrl(), retrievedWish.getProductImageUrl());
    }

    @Test
    @Rollback
    public void 위시리스트_삭제_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        User user = userRepository.save(new User("user@example.com", "password"));
        Product product = productRepository.save(new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product1.jpg", categoryService.getCategoryEntityById(category)));
        Wish wish = wishRepository.save(new Wish(user, product));

        wishService.deleteWish(wish.getId());

        WishPageResponseDto wishPage = wishService.getWishesByUserId(user.getId(), PageRequest.of(0, 10));
        assertTrue(wishPage.getWishes().isEmpty());
    }

    @Test
    @Rollback
    public void 위시리스트_삭제_없는위시_예외_발생() {
        assertThrows(BusinessException.class, () -> wishService.deleteWish(999L));
    }

    @TestFactory
    @Rollback
    public Stream<DynamicTest> 위시리스트_목록_페이지네이션_성공() {
        Long category = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리")).getId();
        User user = userRepository.save(new User("user@example.com", "password"));
        Product product1 = productRepository.save(new Product(new ProductName("상품 1"), 1000, "https://example.com/product1.jpg", categoryService.getCategoryEntityById(category)));
        Product product2 = productRepository.save(new Product(new ProductName("상품 2"), 2000, "https://example.com/product2.jpg", categoryService.getCategoryEntityById(category)));
        Product product3 = productRepository.save(new Product(new ProductName("상품 3"), 3000, "https://example.com/product3.jpg", categoryService.getCategoryEntityById(category)));
        Product product4 = productRepository.save(new Product(new ProductName("상품 4"), 4000, "https://example.com/product4.jpg", categoryService.getCategoryEntityById(category)));
        Product product5 = productRepository.save(new Product(new ProductName("상품 5"), 5000, "https://example.com/product5.jpg", categoryService.getCategoryEntityById(category)));

        wishRepository.save(new Wish(user, product1));
        wishRepository.save(new Wish(user, product2));
        wishRepository.save(new Wish(user, product3));
        wishRepository.save(new Wish(user, product4));
        wishRepository.save(new Wish(user, product5));

        return Stream.of(
                DynamicTest.dynamicTest("첫번째 페이지 조회", () -> {
                    WishPageResponseDto wishPage = wishService.getWishesByUserId(user.getId(), PageRequest.of(0, 2));
                    assertNotNull(wishPage);
                    assertEquals(2, wishPage.getWishes().size());
                    assertEquals(5, wishPage.getTotalItems());
                    assertEquals(3, wishPage.getTotalPages());
                }),
                DynamicTest.dynamicTest("두번째 페이지 조회", () -> {
                    WishPageResponseDto wishPage2 = wishService.getWishesByUserId(user.getId(), PageRequest.of(1, 2));
                    assertNotNull(wishPage2);
                    assertEquals(2, wishPage2.getWishes().size());
                    assertEquals(5, wishPage2.getTotalItems());
                }),
                DynamicTest.dynamicTest("세번째 페이지 조회", () -> {
                    WishPageResponseDto wishPage3 = wishService.getWishesByUserId(user.getId(), PageRequest.of(2, 2));
                    assertNotNull(wishPage3);
                    assertEquals(1, wishPage3.getWishes().size());
                    assertEquals(5, wishPage3.getTotalItems());
                })
        );
    }
}
