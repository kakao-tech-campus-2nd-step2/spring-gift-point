package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.CategoryEntity;
import gift.entity.MemberEntity;
import gift.entity.ProductEntity;
import gift.entity.WishListEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Transactional
@DataJpaTest
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private MemberEntity user;
    private ProductEntity product;
    private WishListEntity wishListEntity;

    @BeforeEach
    void setUp() {
        CategoryEntity category = new CategoryEntity("Blue", "color", "https://example.com/image.png", "New Category");
        categoryRepository.save(category);

        product = new ProductEntity("아이스티", 3000, "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", category);
        productRepository.save(product);

        user = new MemberEntity("admin@gmail.com", "password");
        memberRepository.save(user);
        
        wishListEntity = new WishListEntity(product, user);
        wishListRepository.save(wishListEntity);
    }

    @Test
    void testFindByUserEntity_IdAndProductEntity_Id() {
        Optional<WishListEntity> wishEntity = wishListRepository.findByUserEntity_IdAndProductEntity_Id(user.getId(), product.getId());
        assertThat(wishEntity).isPresent();
        assertThat(wishEntity.get().getUserEntity().getId()).isEqualTo(user.getId());
        assertThat(wishEntity.get().getProductEntity().getId()).isEqualTo(product.getId());
    }

    @Test
    void testFindByUserEntity_Id() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<WishListEntity> wishEntities = wishListRepository.findByUserEntity_Id(user.getId(), pageable);
        assertThat(wishEntities).isNotEmpty();
    }
}