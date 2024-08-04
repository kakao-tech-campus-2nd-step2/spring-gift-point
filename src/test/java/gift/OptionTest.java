package gift;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.DTO.Category;
import gift.DTO.Option;
import gift.DTO.Product;
import org.junit.jupiter.api.Test;

public class OptionTest {

  @Test
  void subtractTest() {
    Option option = new Option(1L, "옵션1", 2,
      new Product(1L, "product1", 300, "fadsklf",
        new Category(1L, "교환권", "#6c95d1", "image_url", "교환권 카테고리")));

    option.subtract(1);

    assertEquals(1, option.getQuantity());
  }
}
