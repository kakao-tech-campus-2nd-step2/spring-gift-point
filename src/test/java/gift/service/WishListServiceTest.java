package gift.service;

import gift.dto.ProductDTO;
import gift.dto.WishListDTO;
import gift.entity.MemberEntity;
import gift.entity.ProductEntity;
import gift.entity.WishListEntity;
import gift.repository.WishListRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WishListServiceTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WishListService wishListService;

    private MemberEntity memberEntity;
    private ProductEntity productEntity;

    @BeforeEach
    void setUp() {
        memberEntity = new MemberEntity("test@example.com", "password123");
        memberRepository.save(memberEntity);

        productEntity = new ProductEntity("Test Product", 1000, "http://image.url/1", null);
        productRepository.save(productEntity);
    }

    @Test
    @DisplayName("위시리스트 조회 성공")
    void readWishList_Success() throws Exception {
        // Given
        Long userId = memberEntity.getId();
        for (int i = 0; i < 7; i++) {
            ProductEntity product = new ProductEntity("Test Product " + i, 1000 + i, "http://image.url/" + i, null);
            productRepository.save(product);
            wishListService.addProductToWishList(userId, new ProductDTO(product.getId(), "Test Product " + i, 1000 + i, "http://image.url/" + i, 2L, List.of(3L)));
        }
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<WishListDTO> result = wishListService.readWishList(userId, pageable);

        // Then
        assertEquals(7, result.getTotalElements());
    }


    @Test
    @DisplayName("위시 리스트 생성 성공")
    void addProductToWishList_Success() throws Exception {
        // Given
        Long userId = memberEntity.getId();
        ProductDTO productDTO = new ProductDTO(productEntity.getId(), "Test Product", 1000, "http://image.url/1", 2L, List.of(3L));

        // Then
        assertDoesNotThrow(() -> {
            wishListService.addProductToWishList(userId, productDTO);
        });

        List<WishListEntity> wishListEntities = wishListRepository.findByUserEntity_Id(userId, Pageable.unpaged()).getContent();
        assertEquals(1, wishListEntities.size());
        assertEquals(productEntity.getId(), wishListEntities.get(0).getProductEntity().getId());
    }

    @Test
    @DisplayName("유저가 존재하지 않는 위시리스트")
    void addProductToWishList_UserNotFound() {
        // Given
        Long userId = 123L;
        ProductDTO productDTO = new ProductDTO(productEntity.getId(), "Test Product", 1000, "http://image.url/1", 2L, List.of(3L));

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            wishListService.addProductToWishList(userId, productDTO);
        });
        assertEquals("유저가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품이 존재하지 않는 위시리스트")
    void addProductToWishList_ProductNotFound() {
        // Given
        Long userId = memberEntity.getId();
        Long nonExistentProductId = 123L;
        ProductDTO productDTO = new ProductDTO(nonExistentProductId, "Test Product", 1000, "http://image.url/1", 2L, List.of(3L));

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            wishListService.addProductToWishList(userId, productDTO);
        });
        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("단일 위시리스트 삭제 성공")
    void removeProductFromWishList_Success() {
        // Given
        Long userId = memberEntity.getId();
        Long productId = productEntity.getId();

        WishListEntity wishListEntity = new WishListEntity(productEntity, memberEntity);
        wishListRepository.save(wishListEntity);

        // Then
        assertDoesNotThrow(() -> {
            wishListService.removeProductFromWishList(userId, productId);
        });

        List<WishListEntity> wishListEntities = wishListRepository.findByUserEntity_Id(userId, Pageable.unpaged()).getContent();
        assertEquals(0, wishListEntities.size());
    }

    @Test
    @DisplayName("삭제하려는 위시가 존재하지 않음")
    void removeProductFromWishList_NotFound() {
        // Given
        Long userId = memberEntity.getId();
        Long productId = 123L;

        // Then
        assertDoesNotThrow(() -> {
            wishListService.removeProductFromWishList(userId, productId);
        });
    }

    @Test
    @DisplayName("전체 위시 삭제 성공")
    void removeWishList_Success() {
        // Given
        Long userId = memberEntity.getId();

        WishListEntity wishListEntity = new WishListEntity(productEntity, memberEntity);
        wishListRepository.save(wishListEntity);

        // Then
        assertDoesNotThrow(() -> {
            wishListService.removeWishList(userId);
        });

        List<WishListEntity> wishListEntities = wishListRepository.findByUserEntity_Id(userId, Pageable.unpaged()).getContent();
        assertEquals(0, wishListEntities.size());
    }
}
