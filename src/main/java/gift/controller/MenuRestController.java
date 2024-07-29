package gift.controller;

import gift.domain.MenuRequest;
import gift.domain.MenuResponse;
import gift.domain.Option;
import gift.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/menus")
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @ModelAttribute @Valid MenuRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        } else {
            MenuResponse menuResponse = menuService.save(request);
            return ResponseEntity.ok().body(menuResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> read(
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(menuService.findall(pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<MenuResponse> update(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute MenuRequest request
    ) {
        return ResponseEntity.ok().body(menuService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        menuService.delete(id);
        return ResponseEntity.ok().body("successfully deleted");
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<Set<Option>> getOptions(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok().body(menuService.getOptions(id));
    }

}
