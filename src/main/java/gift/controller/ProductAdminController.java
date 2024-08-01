package gift.controller;


import gift.dto.product.ResponseProductDTO;
import gift.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ProductAdminController {

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
