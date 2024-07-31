package gift.controller;

import gift.domain.MenuRequest;
import gift.domain.MenuResponse;
import gift.domain.Option;
import gift.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/products")
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody MenuRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
        } else {
            MenuResponse menuResponse = menuService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(menuResponse);
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<MenuResponse> readMenu(
            @PathVariable("productId") Long id
    ){
        return ResponseEntity.ok().body(menuService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> read(
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(menuService.findall(pageable));
    }

    @PutMapping("{productId}")
    public ResponseEntity<MenuResponse> update(
            @PathVariable("productId") Long id,
            @RequestBody MenuRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(menuService.update(id, request));
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<String> delete(@PathVariable("productId") Long id) {
        menuService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully deleted");
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<Set<Option>> getOptions(
            @PathVariable("productId") Long id
    ){
        return ResponseEntity.ok().body(menuService.getOptions(id));
    }

}
