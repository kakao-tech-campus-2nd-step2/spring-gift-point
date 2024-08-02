package gift.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


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
