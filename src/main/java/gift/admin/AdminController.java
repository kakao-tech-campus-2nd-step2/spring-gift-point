package gift.admin;

import gift.controller.category.CategoryRequest;
import gift.controller.category.CategoryResponse;
import gift.controller.member.MemberRequest;
import gift.controller.member.MemberResponse;
import gift.controller.member.SignUpRequest;
import gift.controller.product.ProductRequest;
import gift.controller.product.ProductResponse;
import gift.service.CategoryService;
import gift.service.MemberService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, MemberService memberService,
        CategoryService categoryService) {
        this.productService = productService;
        this.memberService = memberService;
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listCategories(Model model, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryResponse> categories = categoryService.findAll(pageable);
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/categories/add")
    public String categoryAddForm(Model model) {
        model.addAttribute("category", new CategoryRequest("", "", "", ""));
        return "category-add-form";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute CategoryRequest category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String categoryEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("category", categoryService.getCategoryResponse(id));
        return "category-edit-form";
    }

    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable UUID id, @ModelAttribute CategoryRequest category) {
        categoryService.update(id, category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/members")
    public String listMembers(Model model, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemberResponse> members = memberService.findAll(pageable);
        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/members/add")
    public String memberAddForm(Model model) {
        model.addAttribute("member", new SignUpRequest("", "", ""));
        return "member-add-form";
    }

    @PostMapping("/members/add")
    public String addMember(@ModelAttribute SignUpRequest member) {
        memberService.save(member);
        return "redirect:/admin/members";
    }

    @GetMapping("/members/edit/{id}")
    public String memberEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("member", memberService.findById(id));
        return "member-edit-form";
    }

    @PostMapping("/members/edit/{id}")
    public String editMember(@PathVariable UUID id, @ModelAttribute MemberRequest member) {
        memberService.update(id, member);
        return "redirect:/admin/members";
    }

    @GetMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable UUID id) {
        memberService.delete(id);
        return "redirect:/admin/members";
    }

    @GetMapping("/products")
    public String listProducts(Model model, @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = productService.findAll(pageable);
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        model.addAttribute("pageSize", size);
        return "products";
    }

    @GetMapping("/products/add")
    public String productAddForm(Model model) {
        model.addAttribute("product", new ProductRequest("", 0L, "", ""));
        return "product-add-form";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute @Valid ProductRequest product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String productEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.getProductResponse(id));
        return "product-edit-form";
    }

    @PostMapping("/products/edit/{id}")
    public String editProduct(@PathVariable UUID id,
        @ModelAttribute @Valid ProductRequest product) {
        productService.update(id, product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}