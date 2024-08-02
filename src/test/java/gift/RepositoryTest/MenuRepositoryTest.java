package gift.RepositoryTest;

import gift.domain.CategoryDomain.Category;
import gift.domain.MenuDomain.Menu;
import gift.domain.OptionDomain.Option;
import gift.repository.CategoryRepository;
import gift.repository.MenuRepository;
import gift.repository.OptionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;
    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;

    @BeforeEach
    void setUp() {
        category1 = new Category(null, "양식","dis","빨강","image.com", new LinkedList<Menu>());
        category2 = new Category(null, "양식","dis","한식","image.com", new LinkedList<Menu>());
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        Menu menu = new Menu("파스타", 3000, "naver.com", category1,new LinkedList<>());
        menuRepository.save(menu);

        option1 = new Option(null, "알리오올리오", 3L,menu);
        option2 = new Option(null, "토마토", 4L,menu);
        option3 = new Option(null, "매운맛", 3L,menu);
        option4 = new Option(null, "순한맛", 4L,menu);

        optionRepository.save(option1);
        optionRepository.save(option2);
        optionRepository.save(option3);
        optionRepository.save(option4);
    }

    @Test
    @DisplayName("메뉴 FindById 테스트")
    @Transactional
    void testFindById() {
        // Given
        List<Option> options = new LinkedList<>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타", 3000, "naver.com", category1, options);
        menu = menuRepository.save(menu);

        // When
        Optional<Menu> foundMenu = menuRepository.findById(menu.getId());

        // Then
        assertThat(foundMenu).isPresent();
        assertThat(foundMenu.get().getName()).isEqualTo("파스타");
        assertThat(foundMenu.get().getOptions()).hasSize(2);
    }

    @Test
    @DisplayName("메뉴 FindAll 테스트")
    void testFindAll() {
        // Given
        List<Option> options2 = new LinkedList<>();
        options2.add(option3);
        options2.add(option4);

        Menu menu2 = new Menu("떡볶이", 3000, "naver.com", category2, options2);

        menuRepository.save(menu2);

        // When
        Page<Menu> page = menuRepository.findAll(PageRequest.of(0, 10));

        // Then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting(Menu::getName).containsExactlyInAnyOrder("파스타", "떡볶이");
    }

    @Test
    @DisplayName("메뉴로 옵션 찾기 테스트")
    void testGetOptionsById() {
        // Given
        List<Option> options = new LinkedList<>();
        options.add(option1);
        options.add(option2);

        Menu menu = new Menu("파스타", 3000, "naver.com", category1, options);
        menu = menuRepository.save(menu);

        // When
        Set<Option> foundOptions = menuRepository.getOptionsByMenuId(menu.getId());

        // Then
        assertThat(foundOptions).hasSize(2);
        assertThat(foundOptions).extracting(Option::getName).containsExactlyInAnyOrder("알리오올리오", "토마토");
    }
}
