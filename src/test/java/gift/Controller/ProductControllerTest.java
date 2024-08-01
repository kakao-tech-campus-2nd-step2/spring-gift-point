package gift.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.DTO.CategoryDto;
import gift.DTO.ProductDto;
import gift.Service.CategoryService;
import gift.Service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ProductControllerTest {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductController productController;
  @Autowired
  private CategoryController categoryController;
  @Autowired
  private CategoryService categoryService;

  @BeforeEach
  public void setUp() {
    productController = new ProductController(productService);
  }


  @DirtiesContext
  @Test
  public void testGetAllProducts() {
    Pageable pageable = PageRequest.of(0, 5);

    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto productDto1 = new ProductDto(1L, "Coffee", 100,
      "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
      categoryDto);
    ProductDto productDto2 = new ProductDto(2L, "Tea", 200,
      "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc94364879792549ads8bdd8a3.jpg",
      categoryDto);

    ProductDto addedProduct1 = productController.addProduct(productDto1);
    ProductDto addedProduct2 = productController.addProduct(productDto2);

    Page<ProductDto> returnedProductEntities = productController.getAllProducts(pageable).getBody();

    assertEquals(2, returnedProductEntities.getContent().size());
    assertEquals(productDto1.getId(), returnedProductEntities.getContent().get(0).getId());
    assertEquals(productDto1.getName(), returnedProductEntities.getContent().get(0).getName());
    assertEquals(productDto1.getPrice(), returnedProductEntities.getContent().get(0).getPrice());
    assertEquals(productDto1.getImageUrl(),
      returnedProductEntities.getContent().get(0).getImageUrl());
    assertEquals(productDto2.getId(), returnedProductEntities.getContent().get(1).getId());
    assertEquals(productDto2.getName(), returnedProductEntities.getContent().get(1).getName());
    assertEquals(productDto2.getPrice(), returnedProductEntities.getContent().get(1).getPrice());
    assertEquals(productDto2.getImageUrl(),
      returnedProductEntities.getContent().get(1).getImageUrl());
  }

  @DirtiesContext
  @Test
  public void testGetProductById() {
    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto productDto = new ProductDto(1L, "Coffee", 100,
      "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
      categoryDto);

    ProductDto addedProduct = productController.addProduct(productDto);

    ResponseEntity<ProductDto> responseEntity = productController.getProductById(1L);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    ProductDto returnedProductDto = responseEntity.getBody();
    assertNotNull(returnedProductDto);
    assertEquals(productDto.getId(), returnedProductDto.getId());
    assertEquals(productDto.getName(), returnedProductDto.getName());
    assertEquals(productDto.getPrice(), returnedProductDto.getPrice());
    assertEquals(productDto.getImageUrl(), returnedProductDto.getImageUrl());
  }

  @DirtiesContext
  @Test
  public void testAddProduct() {
    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto productDto = new ProductDto(1L, "Coffee", 4500,
      "https://example.com/coffee.jpg", categoryDto);

    ProductDto addedProduct = productController.addProduct(productDto);

    assertNotNull(addedProduct);
    assertNotNull(addedProduct.getId());
    assertEquals("Coffee", addedProduct.getName());
    assertEquals(4500, addedProduct.getPrice());
    assertEquals("https://example.com/coffee.jpg", addedProduct.getImageUrl());
  }


  @DirtiesContext
  @Test
  void testUpdateProduct() {
    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto existingProduct = new ProductDto(1L, "Coffee", 4500,
      "https://example.com/coffee.jpg", categoryDto);
    productController.addProduct(existingProduct);

    ProductDto updatedProduct = new ProductDto(1L, "Hot_Coffee", 4000,
      "https://example.com/coffee.jpg", categoryDto);

    ResponseEntity<ProductDto> response = productController.updateProduct(1L,
      updatedProduct);

    ProductDto returnedProductDto = response.getBody();

    assertNotNull(returnedProductDto);
    assertEquals(1L, returnedProductDto.getId());
    assertEquals("Hot_Coffee", returnedProductDto.getName());
    assertEquals(4000, returnedProductDto.getPrice());
    assertEquals("https://example.com/coffee.jpg", returnedProductDto.getImageUrl());
  }

  @DirtiesContext
  @Test
  public void testDeleteProduct() {
    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto productDto = new ProductDto(1L, "Coffee", 100,
      "https://example.com/coffee.jpg", categoryDto);
    productController.addProduct(productDto);


    assertEquals(HttpStatus.NO_CONTENT, productController.deleteProduct(1L).getStatusCode());

  }

  @DirtiesContext
  @Test
  public void testValidate() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    CategoryDto categoryDto = new CategoryDto(1L, "교환권", "#61cdef", "image.url", "교환권 카테고리");
    categoryController.addCategory(categoryDto);

    ProductDto invalidProduct1DTO = new ProductDto(1L, "pppppppppsdfsfdsppppppppProduct 1",
      100, "https://st.kakaocdn.net/product/gift/product/65.jpg",
      categoryDto);
    ProductDto invalidProduct2DTO = new ProductDto(2L, "카카오 product", 100,
      "https://st.kakaocdn.net/product/gift/product/65.jpg", categoryDto);

    Set<ConstraintViolation<ProductDto>> violations1 = validator.validate(invalidProduct1DTO);
    Set<ConstraintViolation<ProductDto>> violations2 = validator.validate(invalidProduct2DTO);

    assertThrows(ConstraintViolationException.class, () -> {
      throw new ConstraintViolationException(violations1);
    });

    assertThrows(ConstraintViolationException.class, () -> {
      throw new ConstraintViolationException(violations2);
    });
  }
}