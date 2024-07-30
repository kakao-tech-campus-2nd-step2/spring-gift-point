package gift.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;

@DataJpaTest
public class OptionRepositoryTest {

	private final OptionRepository optionRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@Autowired
	public OptionRepositoryTest(OptionRepository optionRepository, ProductRepository productRepository,
			CategoryRepository categoryRepository) {
		this.optionRepository = optionRepository;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}
	
	private Category category;
	private Product product;
	private Option option;
	
	@BeforeEach
	public void setUp() {
		category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
		categoryRepository.save(category);
		product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
		productRepository.save(product);
		option = new Option("01 [Best] 시어버터 핸드 & 시어 스틱 립 밤", 969, product);
		optionRepository.save(option);
	}
	
	@Test
	void save() {
		Option actual = optionRepository.save(option);
		
		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getName()).isEqualTo(option.getName());
	}
	
	@Test
	void findByProductId() {
		List<Option> options = optionRepository.findByProductId(product.getId());
		
		assertThat(options).isNotEmpty();
		assertThat(options.get(0).getName()).isEqualTo(option.getName());
	}
	
	@Test
	void findByProductIdAndName() {
		Option actual = optionRepository.findByProductIdAndName(product.getId(), option.getName()).orElse(null);
		
		assertThat(actual).isNotNull();
		assertThat(actual.getName()).isEqualTo(option.getName());
	}
}
