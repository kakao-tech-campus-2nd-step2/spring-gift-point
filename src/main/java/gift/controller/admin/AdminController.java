package gift.controller.admin;


import gift.dto.category.CategoryResponse;
import gift.dto.gift.ProductRequest;
import gift.dto.gift.ProductResponse;
import gift.dto.option.OptionRequest;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.service.category.CategoryService;
import gift.service.gift.ProductService;
import gift.service.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    @Autowired
    public AdminController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    public String index() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminHome(Model model, @ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<ProductResponse> giftlist = productService.getAllGifts(pagingRequest.getPage(), pagingRequest.getSize());
        model.addAttribute("giftlist", giftlist.getContent());
        return "admin";
    }

    @GetMapping("/admin/gift/create")
    public String giftCreate(Model model) {
        CategoryResponse.InfoList infoList = categoryService.getAllCategories();
        List<CategoryResponse.Info> categories = infoList.categories();
        model.addAttribute("categories", categories);
        return "create_form";
    }

    @PostMapping("/admin/gift/create")
    public String giftCreate(@Valid @ModelAttribute ProductRequest.Create giftRequest) {
        ProductResponse productResponse = productService.addGift(giftRequest);
        for (OptionRequest.Create optionRequest : giftRequest.options()) {
            optionService.addOptionToGift(productResponse.getId(), optionRequest);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gift/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        ProductResponse gift = productService.getGift(id);
        model.addAttribute("gift", gift);
        return "gift_detail";
    }

    @GetMapping("/admin/gift/modify/{id}")
    public String giftModify(Model model, @PathVariable("id") Long id) {
        CategoryResponse.InfoList infoList = categoryService.getAllCategories();
        List<CategoryResponse.Info> categories = infoList.categories();
        model.addAttribute("categories", categories);
        ProductResponse gift = productService.getGift(id);
        model.addAttribute("gift", gift);
        return "modify_form";
    }

    @PutMapping("/admin/gift/modify/{id}")
    public String giftModify(@PathVariable("id") Long id, @ModelAttribute ProductRequest.Update giftRequest) {
        productService.updateGift(giftRequest, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/gift/delete/{id}")
    public String giftDelete(@PathVariable("id") Long id) {
        productService.deleteGift(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/gift/{giftId}/option/create")
    public String showCreateOptionForm(Model model, @PathVariable Long giftId) {
        model.addAttribute("giftId", giftId);
        return "create_option_form";
    }

    @PostMapping("/admin/gift/{giftId}/option/create")
    public String createOption(@PathVariable Long giftId, @Valid @ModelAttribute OptionRequest.Create optionRequest) {
        optionService.addOptionToGift(giftId, optionRequest);
        return "redirect:/admin/gift/detail/" + giftId;
    }
}

