package gift;

import gift.model.Product;
import gift.model.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

  @Test
  public void testProductGettersAndSetters() {
    Category category = new Category("Test Category", "#FFFFFF", "http://example.com/category.jpg", "Test Description");

    Product product = new Product("아이스 카페 아메리카노 T", 4500,"https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", category);

    assertEquals("아이스 카페 아메리카노 T", product.getName());
    assertEquals(4500, product.getPrice());
    assertEquals("https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", product.getImageUrl());
    assertEquals(category, product.getCategory());
  }

  @Test
  void  Illegal_Test(){
    Category category = new Category("Test Category", "#FFFFFF", "http://example.com/category.jpg", "Test Description");
    Product product = new Product("Name", 1000, "http://example.com/product.jpg", category);

    assertThrows(IllegalArgumentException.class,()->product.updateFromDto("",1000,"image",category));
    assertThrows(IllegalArgumentException.class,()->product.updateFromDto("prod",-1,"image",category));
    assertThrows(IllegalArgumentException.class,()->product.updateFromDto("prod",1000,"",category));
    assertThrows(IllegalArgumentException.class,()->product.updateFromDto("prod",1000,"image",null));

  }
}
