package gift.controller;


import gift.dto.product.ProductWithOptionDTO;
import gift.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductService productService;

    @GetMapping("/admin/products")
    public String adminProducts(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "0") int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<ProductWithOptionDTO> products = productService.getAllProductsWithOption(pageable);
        request.setAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/admin/add")
    public String adminProductsAdd() {
        return "admin/add";
    }

    @GetMapping("/admin/modify")
    public String adminProductsModify() {
        return "admin/modify";
    }

    @GetMapping("/admin/delete")
    public String adminProductsDelete() {
        return "admin/delete";
    }
}
