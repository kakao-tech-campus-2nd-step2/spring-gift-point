package gift.controller.product;

/*
@Controller
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;

    AdminController(ProductService productService, CategoryService  categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/product")
    public String registerProductForm(Model model) {
        PageRequest pageRequest = PageRequest.of(0, 100);
        PageResponse<CategoryResponse> categoryList = categoryService.findAllCategory(pageRequest);
        List<CategoryResponse> categories = categoryList.responses();
        model.addAttribute("categories", categories);
        return "addProduct";
    }

    @PostMapping("/admin/product")
    public String registerProduct(CreateProductRequest createProductRequest) {
        productService.addProduct(createProductRequest);
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String getAllProducts(Model model,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ProductResponse> response = productService.findAllProduct(pageable);
        model.addAttribute("products", response);
        return "productList";
    }

    @GetMapping("/admin/product/{id}")
    public ProductResponse getProduct(@PathVariable("id") Long id) {
        ProductResponse response = productService.findProduct(id);
        return response;
    }

    @GetMapping("/product")
    public String updateProductForm(@RequestParam("id") Long id) {
        return "editProduct";
    }

    @PutMapping("/admin/product/{id}")
    public String updateProduct(@PathVariable("id") Long id, ProductRequest.Create request) {
        productService.updateProduct(id, request);
        return "redirect:/products";
    }

    @DeleteMapping("/admin/product/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
*/
