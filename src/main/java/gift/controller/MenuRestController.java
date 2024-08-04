package gift.controller;

import gift.domain.MenuDomain.MenuRequest;
import gift.domain.MenuDomain.MenuResponse;
import gift.domain.MenuDomain.MenuUpdateRequest;
import gift.domain.OptionDomain.Option;
import gift.service.MenuService;
import io.jsonwebtoken.JwtException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/products")
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestBody MenuRequest request
    ) {
        MenuResponse menuResponse = menuService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<MenuResponse> readMenu(
            @PathVariable("productId") Long id
    ){
        return ResponseEntity.ok().body(menuService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> readAll(
            Pageable pageable,
             @RequestParam(required = false) Long categoryId
    ) {
        if(categoryId == null){
            return ResponseEntity.ok().body(menuService.findall(pageable));
        }
        return ResponseEntity.ok().body(menuService.findByCategoryId(categoryId,pageable));
    }

    @PutMapping("{productId}")
    public ResponseEntity<MenuResponse> update(
            @PathVariable("productId") Long id,
            @RequestBody MenuUpdateRequest menuUpdateRequest
            ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(menuService.update(id, menuUpdateRequest));
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

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleException(JwtException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("TokenError", "허용되지 않는 요청입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleException(NoSuchElementException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("NoSuchElementError", "허용되지 않는 요청입니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

}
