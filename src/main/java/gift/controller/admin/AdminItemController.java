package gift.controller.admin;

import gift.model.dto.CategoryDTO;
import gift.model.dto.ItemDTO;
import gift.model.dto.OptionDTO;
import gift.model.form.ItemForm;
import gift.model.form.OptionForm;
import gift.model.response.ItemResponse;
import gift.service.CategoryService;
import gift.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;

    public AdminItemController(ItemService itemService, CategoryService categoryService) {
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    @GetMapping("/items")
    public String getItemListPage(Model model, Pageable pageable) {
        Page<ItemResponse> itemPage = itemService.getList(pageable, 0L);
        model.addAttribute("itemPage", itemPage);
        return "items";
    }

    @GetMapping("/items/add")
    public String getItemForm(Model model, ItemForm itemForm) {
        List<CategoryDTO> categories = categoryService.getCategoryList();
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("categories", categories);
        return "item-add";
    }

    @PostMapping("/items/add")
    public String addItem(@Valid @ModelAttribute ItemForm itemForm, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            List<CategoryDTO> categories = categoryService.getCategoryList();
            model.addAttribute("categories", categories);
            return "item-add";
        }
        ItemDTO itemDTO = new ItemDTO(itemForm.getName(), itemForm.getPrice(), itemForm.getImgUrl(),
            itemForm.getCategoryId());
        itemService.insertItem(itemDTO, itemForm.getOptionDTOList());
        return "redirect:/admin/items";
    }

    @GetMapping("/items/info/{itemId}")
    public String getItemInfo(@PathVariable("itemId") Long id, Model model) {
        ItemResponse itemResponse = itemService.getItemById(id, 0L);
        List<OptionDTO> options = itemService.getOptionList(id);
        model.addAttribute("item", itemResponse);
        model.addAttribute("options", options);
        return "items-info";
    }

    @GetMapping("/items/update/{productId}")
    public String getUpdateItemForm(@PathVariable("productId") Long id, Model model) {
        ItemDTO itemDTO = itemService.getItemDTO(id);
        List<OptionDTO> options = itemService.getOptionList(id);
        List<CategoryDTO> categories = categoryService.getCategoryList();
        List<OptionForm> optionForms = options.stream().map(OptionForm::new)
            .collect(Collectors.toList());
        ItemForm itemForm = new ItemForm(itemDTO.getId(), itemDTO.getName(), itemDTO.getPrice(),
            itemDTO.getImageUrl(), itemDTO.getCategoryId(), optionForms);
        model.addAttribute("itemForm", itemForm);
        model.addAttribute("categories", categories);
        return "item-update";
    }

    @PutMapping("/items/update/{productId}")
    public String updateItem(@PathVariable("productId") Long id,
        @Valid @ModelAttribute ItemForm itemForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<CategoryDTO> categories = categoryService.getCategoryList();
            model.addAttribute("categories", categories);
            return "item-update";
        }
        ItemDTO itemDTO = new ItemDTO(id, itemForm.getName(), itemForm.getPrice(),
            itemForm.getImgUrl(), itemForm.getCategoryId());
        List<OptionDTO> options = itemForm.getOptionDTOList();
        itemService.updateItem(itemDTO, options);
        return "redirect:/admin/items";
    }

    @DeleteMapping("/items/{itemId}")
    public String deleteItem(@PathVariable("itemId") Long id, Model model) {
        itemService.deleteItem(id);
        return "redirect:/admin/items";
    }
}
