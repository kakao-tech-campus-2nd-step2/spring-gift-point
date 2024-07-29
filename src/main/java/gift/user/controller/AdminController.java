package gift.user.controller;

import gift.product.dto.ProductDto;
import gift.product.dto.ProductSortField;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
//@Secured("ROLE_ADMIN")
@RequestMapping("/admin")
public class AdminController {

  private final ProductService productService;

  @Autowired
  public AdminController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public String showAllProducts(Model model,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") ProductSortField sort,
      @RequestParam(defaultValue = "asc") Sort.Direction direction) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort.getFieldName()));
    Page<ProductDto> products = productService.findAll(pageable);

    model.addAttribute("products", products);
    model.addAttribute("currentPage", products.getNumber());
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("totalItems", products.getTotalElements());
    model.addAttribute("pageSize", products.getSize());
    model.addAttribute("sortField", sort);
    model.addAttribute("sortDirection", direction);
    model.addAttribute("reverseSortDirection", direction.equals("asc") ? "desc" : "asc");

    return "product-list";
  }

  @GetMapping("/add")
  public String showAddProductForm(Model model) {
    model.addAttribute("product", new ProductDto());
    return "product-form";
  }

  @GetMapping("/edit/{id}")
  public String showEditProductForm(@PathVariable("id") Long id, Model model) {
    try {
      ProductDto productDto = productService.getProductById(id);
      model.addAttribute("product", productDto);
      return "product-form";
    } catch (RuntimeException e) {
      return "redirect:/admin";
    }
  }


  @PostMapping("/save")
  public String saveProduct(@Valid @ModelAttribute ProductDto productDto,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("product", productDto);
      return "product-form";
    }
    if (productDto.getId() == null) {
      productService.addProduct(productDto);
    } else {
      productService.updateProduct(productDto.getId(), productDto);
    }
    return "redirect:/admin";
  }


  @GetMapping("/delete/{id}")
  public String deleteProduct(@PathVariable("id") Long id) {
    productService.deleteProduct(id);
    return "redirect:/admin";
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  public String deleteProductAjax(@PathVariable("id") Long id) {
    productService.deleteProduct(id);
    return "success";
  }
}
