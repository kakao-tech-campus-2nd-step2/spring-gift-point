package gift;

import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.DTO.Category;
import gift.DTO.Option;
import gift.DTO.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class OptionTest {

  @Test
  void subtractTest() {
    Option option = new Option(1L, "옵션1", 2,
      new Product(1L, "product1", 300, "fadsklf",
        new Category(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));
    assertThrows(IllegalAccessException.class, () -> {
      option.subtract(3);
    });
  }
}
