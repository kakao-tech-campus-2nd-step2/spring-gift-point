package gift.controller;

import gift.dto.ProductDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "ProductViewController", description = "상품 뷰 관련 API")
@Controller
@RequestMapping("/products")
public class ProductViewController {

  @Autowired
  private ProductService productService;

  @Operation(summary = "새 상품 폼 표시", description = "새로운 상품을 등록하기 위한 폼을 표시합니다.")
  @GetMapping("/new")
  public String showNewProductForm(Model model) {
    model.addAttribute("productDto", new ProductDto());
    return "product-form";
  }

  @Operation(summary = "새 상품 생성", description = "새로운 상품을 생성합니다.")
  @PostMapping
  public String createProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return "product-form";
    }

    productService.createProduct(productDto);
    return "redirect:/products";
  }

  @Operation(summary = "상품 수정 폼 표시", description = "기존 상품을 수정하기 위한 폼을 표시합니다.")
  @GetMapping("/{id}/edit")
  public String showEditProductForm(@PathVariable Long id, Model model) {
    ProductDto productDto = productService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    model.addAttribute("productDto", productDto);
    return "product-form";
  }

  @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
  @PostMapping("/{id}")
  public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductDto productDto, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return "product-form";
    }
    ProductDto updatedProductDto = new ProductDto(id, productDto.getName(), productDto.getPrice(), productDto.getImageUrl(), productDto.getCategoryId());
    productService.updateProduct(id, updatedProductDto);

    return "redirect:/products";
  }
}