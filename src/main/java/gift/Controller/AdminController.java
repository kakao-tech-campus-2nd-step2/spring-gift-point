package gift.Controller;

import gift.DTO.*;
import gift.Model.Entity.Member;
import gift.Service.MemberService;
import gift.Service.OptionService;
import gift.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import gift.Model.Entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OptionService optionService;
    private final MemberService memberService;

    public AdminController(ProductService productService, OptionService optionService, MemberService memberService) {
        this.productService = productService;
        this.optionService = optionService;
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public String getMembers(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             Model model) {
        Page<Member> memberPage = memberService.getAllMembers(pageable);
        String sortField = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().toString().equalsIgnoreCase("ASC") ? "asc" : "desc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("members", memberPage.getContent());
        model.addAttribute("totalPages", memberPage.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "members";
    }

    @GetMapping("/members/charge/{id}")
    public String chargePointForm(@PathVariable("id") Long id, Model model) {
        Member member = memberService.selectMember(id);
        model.addAttribute("requestAddPointDTO", new RequestAddPointDTO(0));
        model.addAttribute("id", id);
        return "charge-member-points";
    }

    @PutMapping("/members/charge/{id}")
    public String chargePoint(@PathVariable("id") Long id, @Valid @ModelAttribute RequestAddPointDTO requestAddPointDTO) {
        memberService.addPoints(id, requestAddPointDTO.chargePoint());
        return "redirect:/admin/members";
    }

    @DeleteMapping("/members/delete/{id}")
    public String deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }

    @GetMapping("/products")
    public String getProducts(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                              Model model) {
        Page<Product> productPage = productService.getAllProducts(pageable);
        String sortField = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().toString().equalsIgnoreCase("ASC") ? "asc" : "desc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "products";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new RequestProductPostDTO("이름을 입력해주세요", 1, "url을 입력해주세요", 1L, "옵션 이름을 입력해주세요", 1));
        return "new-product";
    }

    @PostMapping("/products")
    public String newProduct(@Valid @ModelAttribute RequestProductPostDTO requestProductPostDTO) {
        productService.addProduct(requestProductPostDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.selectProduct(id);
        model.addAttribute("product", RequestProductDTO.of(product));
        model.addAttribute("id", id);
        return "edit-product";
    }

    @PutMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute RequestProductDTO requestProductDTO) {
        productService.editProduct(id, requestProductDTO);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

}