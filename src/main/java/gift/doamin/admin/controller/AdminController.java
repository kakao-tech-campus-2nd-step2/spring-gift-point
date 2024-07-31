package gift.doamin.admin.controller;

import gift.doamin.product.dto.ProductParam;
import gift.doamin.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/admin")
    public ModelAndView showAdminPage(
        @RequestParam(defaultValue = "1", name = "page") int pageNum) {
        Page<ProductParam> page = productService.getPage(pageNum - 1);
        int lastPage = Math.max(1, page.getTotalPages());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/index");
        modelAndView.addObject("products", page.getContent());
        modelAndView.addObject("productsCnt", page.getTotalElements());
        modelAndView.addObject("page", pageNum);
        modelAndView.addObject("startPage", Math.max(1, pageNum - 2));
        modelAndView.addObject("endPage", Math.max(lastPage, pageNum + 2));
        modelAndView.addObject("lastPage", lastPage);
        return modelAndView;
    }
}
