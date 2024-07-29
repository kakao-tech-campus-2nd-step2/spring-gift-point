package gift.controller.admin;


import gift.dto.category.CategoryResponse;
import gift.dto.gift.GiftRequest;
import gift.dto.gift.GiftResponse;
import gift.dto.option.OptionRequest;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.service.category.CategoryService;
import gift.service.gift.GiftService;
import gift.service.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private final GiftService giftService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    @Autowired
    public AdminController(GiftService giftService, CategoryService categoryService, OptionService optionService) {
        this.giftService = giftService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    public String index() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminHome(Model model, @ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<GiftResponse> giftlist = giftService.getAllGifts(pagingRequest.getPage(), pagingRequest.getSize());
        model.addAttribute("giftlist", giftlist.getContent());
        return "admin";
    }

    @GetMapping("/admin/gift/create")
    public String giftCreate(Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "create_form";
    }

    @PostMapping("/admin/gift/create")
    public String giftCreate(@Valid @ModelAttribute GiftRequest.Create giftRequest) {
        GiftResponse giftResponse = giftService.addGift(giftRequest);
        for (OptionRequest.Create optionRequest : giftRequest.options()) {
            optionService.addOptionToGift(giftResponse.getId(), optionRequest);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gift/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        GiftResponse gift = giftService.getGift(id);
        model.addAttribute("gift", gift);
        return "gift_detail";
    }

    @GetMapping("/admin/gift/modify/{id}")
    public String giftModify(Model model, @PathVariable("id") Long id) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        GiftResponse gift = giftService.getGift(id);
        model.addAttribute("gift", gift);
        return "modify_form";
    }

    @PutMapping("/admin/gift/modify/{id}")
    public String giftModify(@PathVariable("id") Long id, @ModelAttribute GiftRequest.Update giftRequest) {
        giftService.updateGift(giftRequest, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/gift/delete/{id}")
    public String giftDelete(@PathVariable("id") Long id) {
        giftService.deleteGift(id);
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

