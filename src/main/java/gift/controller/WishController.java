package gift.controller;

import gift.domain.Wish;
import gift.dto.WishDTO;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wishes")
@Tag(name = "WishController", description = "위시 상품 관리 API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 추가", description = "위시리스트에 상품을 추가합니다.")
    @PostMapping
    @ResponseBody
    public void addWish(@RequestBody WishDTO wishDTO) {
        wishService.addWish(wishDTO.getMemberId(), wishDTO.getProductName());
    }

    @Operation(summary = "위시리스트 조회", description = "회원의 위시리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<WishDTO>> getWishes(@RequestParam Long memberId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<Wish> wishPage = wishService.getWishes(memberId, pageable);

        List<WishDTO> wishDTOList = wishPage.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(wishDTOList, HttpStatus.OK);
    }

    @Operation(summary = "위시리스트 삭제", description = "위시리스트에서 상품을 삭제합니다.")
    @DeleteMapping("/remove")
    @ResponseBody
    public ResponseEntity<Void> removeWish(@RequestBody WishDTO wishDTO) {
        wishService.removeWish(wishDTO.getMemberId(), wishDTO.getProductName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private WishDTO convertToDto(Wish wish) {
        WishDTO dto = new WishDTO();
        dto.setId(wish.getId());
        dto.setMemberId(wish.getMember().getId());
        dto.setProductId(wish.getProduct().getId());
        dto.setProductName(wish.getProduct().getName());
        dto.setProductPrice(wish.getProduct().getPrice());
        dto.setProductImageUrl(wish.getProduct().getImageUrl());
        return dto;
    }
}
