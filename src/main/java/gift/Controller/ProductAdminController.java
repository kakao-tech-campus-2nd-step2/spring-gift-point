package gift.Controller;

import gift.DTO.ProductDto;
import gift.Service.CategoryService;
import gift.Service.ProductService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("admin/products")
public class ProductAdminController {

  private final ProductService productService;
  private final CategoryService categoryService;

  public ProductAdminController(ProductService productService, CategoryService categoryService) {
    this.productService = productService;
    this.categoryService = categoryService;

  }

  @GetMapping
  public String listProducts(Model model, Pageable pageable) {
    model.addAttribute("products", productService.getAllProducts(pageable));
    return "product-list";
  }

  @GetMapping("/new")
  public String newProductForm(Model model) {
    model.addAttribute("categories", categoryService.getAllCategories());
    model.addAttribute("product", new ProductDto());
    return "product-form";
  }

  @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Map<String, String>> addProduct(@RequestBody ProductDto productDto) {
    productService.addProduct(productDto);
    Map<String, String> response = new HashMap<>();
    response.put("redirectUrl", "/admin/products");
    return ResponseEntity.ok(response);
  }

  @GetMapping("product/{id}")
  public String editProductForm(@PathVariable Long id, Model model) {
    ProductDto productDto = productService.getProductById(id);
    model.addAttribute("categories", categoryService.getAllCategories());
    model.addAttribute("product", productDto);
    return "product-form";
  }

  @PostMapping("/product/{id}")
  public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Long id,
    @RequestBody ProductDto productDto) {
    productService.updateProduct(id, productDto);
    Map<String, String> response = new HashMap<>();
    response.put("redirectUrl", "/admin/products");
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{id}")
  public String deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return "redirect:/admin/products";
  }
}
