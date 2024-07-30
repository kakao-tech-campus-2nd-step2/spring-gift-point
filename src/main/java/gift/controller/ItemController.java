package gift.controller;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomArgumentNotValidException;
import gift.model.item.ItemDTO;
import gift.model.item.ItemForm;
import gift.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<ItemDTO> getItem(@PathVariable("id") Long id) {
        ItemDTO itemDTO = itemService.getItemById(id);
        return ResponseEntity.ok(itemDTO);
    }

    @Operation(summary = "상품 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<Page<ItemDTO>> getItemList(
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable) {
        Page<ItemDTO> list = itemService.getList(pageable);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "카테고리 별 상품 목록 조회", description = "카테고리 id에 해당되는 상품 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getItemListByCategory(
        @Parameter(description = "조회할 카테고리 id") @RequestParam("categoryId") Long categoryId,
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable) {
        Page<ItemDTO> list = itemService.getListByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "상품 추가")
    @PostMapping
    public ResponseEntity<Long> createItem(@Valid @RequestBody ItemForm form, BindingResult result)
        throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.INVALID_INPUT);
        }
        ItemDTO itemDTO = new ItemDTO(form.getName(), form.getPrice(), form.getImgUrl(),
            form.getCategoryId());
        Long itemId = itemService.insertItem(itemDTO, form.getOptions());
        return ResponseEntity.ok(itemId);
    }


    @Operation(summary = "상품 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updateItem(
        @Parameter(description = "수정할 상품의 id") @PathVariable Long id,
        @Valid @RequestBody ItemForm form,
        BindingResult result) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new CustomArgumentNotValidException(result, ErrorCode.BAD_REQUEST);
        }
        ItemDTO itemDTO = new ItemDTO(id, form.getName(), form.getPrice(), form.getImgUrl(),
            form.getCategoryId());
        return ResponseEntity.ok(itemService.updateItem(itemDTO));
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteItem(
        @Parameter(description = "삭제할 상품의 id") @PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(id);
    }
}