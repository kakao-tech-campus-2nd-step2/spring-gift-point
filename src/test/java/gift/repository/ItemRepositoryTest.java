package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.categories.Category;
import gift.model.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    private final Category category = new Category("식품","img");
    private final Item testItem = new Item("김치",2000L,"url",category);

    @BeforeEach
    void setUp(){
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 추가 성공 테스트")
    void saveSuccessTest(){
        Item saved = itemRepository.save(testItem);
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("상품 조회 성공 테스트")
    void findItemSuccessTest(){
        Item saved = itemRepository.save(testItem);
        Item result = itemRepository.findById(saved.getId()).orElse(null);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("상품 조회 실패 테스트")
    void findItemFailTest(){
        itemRepository.save(testItem);
        Item result = itemRepository.findById(100L).orElse(null);
        assertThat(result).isNull();
    }
}
