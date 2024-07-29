package gift.controller;

import gift.domain.Menu;
import gift.domain.MenuRequest;
import gift.domain.MenuResponse;
import gift.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/menus/view")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    public String returnView(
            String errorMsg,
            Model model,
            Pageable pageable) {
        if (errorMsg != null) {
            model.addAttribute("errors", errorMsg);
            model.addAttribute("menus", menuService.findall(pageable));
            return "Menu";
        }
        model.addAttribute("menus", menuService.findall(pageable));
        return "redirect:/menu";
    }

    @PostMapping
    public void save(
            @ModelAttribute @Valid MenuRequest request,
            Model model,
            Pageable pageable
    ) {
        returnView(null, model, pageable);
    }

    @GetMapping
    public String read(Model model, Pageable pageable) {
        List<MenuResponse> menus = menuService.findall(pageable);
        model.addAttribute("menus", menus);
        return "Menu";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Menu menu = menuService.findById(id);
        model.addAttribute("menu", menu);
        return "update_menu";
    }

}
