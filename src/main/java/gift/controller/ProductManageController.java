package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static gift.constant.Page.*;


@Controller
@RequestMapping("/page/manage/products")
public class ProductManageController {

    @GetMapping
    public String retrieveProduct() {
        return MANAGE_PRODUCT_PAGE;
    }

    @GetMapping("/update/{productId}")
    public String editProductForm(@PathVariable Long productId) {
        return PRODUCT_UPDATE_FORM_PAGE;
    }

    @GetMapping("/add")
    public String addProductForm() {
        return PRODUCT_ADDITION_FROM_PAGE;
    }

}
