package gift.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gift.product.domain.Product;
import gift.product.repository.ProductJpaRepository;

@DataJpaTest
public class ProductRepositoryTest {
	@Autowired
	private ProductJpaRepository productRepository;

	@Test
	@DisplayName("상품 저장한뒤 반환 값이 일치하는 지 확인")
	void save() {
		// given
		String name = "상품1";
		Product product = Product.of(name, 1000L, "image1");
		productRepository.save(product);

		// when
		Product savedProduct = productRepository.findByName(name);

		// then
		assertThat(savedProduct.getName()).isEqualTo(name);
		assertThat(savedProduct.getPrice()).isEqualTo(1000);
		assertThat(savedProduct.getImageUrl()).isEqualTo("image1");
	}

	@Test
	@DisplayName("저장된 상품을 이름으로 성공적으로 조회한다.")
	void findByName() {
		// given
		String name = "상품1";
		Product product = Product.of(name, 1000L, "image1");
		productRepository.save(product);

		// when
		Product savedProduct = productRepository.findByName(name);

		// then
		assertThat(savedProduct.getName()).isEqualTo(name);
		assertThat(savedProduct.getPrice()).isEqualTo(1000);
		assertThat(savedProduct.getImageUrl()).isEqualTo("image1");
	}
}
