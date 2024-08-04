package gift.controller;

import gift.model.response.ItemResponse;
import gift.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product Api")
@RestController
@RequestMapping("/api/products")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "단일 상품 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItem(@PathVariable("id") Long id,
        @RequestAttribute("userId") Long userId) {

        ItemResponse itemResponse = itemService.getItemById(id, userId);

        return ResponseEntity.ok(itemResponse);
    }

    @Operation(summary = "상품 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<Page<ItemResponse>> getItemList(
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable,
        @RequestAttribute("userId") Long userId) {
        Page<ItemResponse> list = itemService.getList(pageable, userId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "카테고리 별 상품 목록 조회", description = "카테고리 id에 해당되는 상품 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getItemListByCategory(
        @Parameter(description = "조회할 카테고리 id") @RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable,
        @RequestAttribute("userId") Long userId) {
        if (categoryId != 0L) {
            Page<ItemResponse> list = itemService.getListByCategoryId(categoryId, pageable, userId);
            return ResponseEntity.ok(list);
        }
        return ResponseEntity.ok(itemService.getList(pageable, userId));
    }
}