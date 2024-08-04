package gift.wishedProduct.controller;

import gift.global.argumentresolver.LoginMember;
import gift.member.entity.Member;
import gift.wishedProduct.dto.AddWishedProductRequest;
import gift.wishedProduct.dto.GetWishedProductResponse;
import gift.wishedProduct.dto.WishedProductDto;
import gift.wishedProduct.service.WishedProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish List", description = "위시 리스트 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishedProductController {

    private final WishedProductService wishedProductService;

    @Autowired
    public WishedProductController(WishedProductService wishedProductService) {
        this.wishedProductService = wishedProductService;
    }

    @Operation(summary = "위시 리스트 조회", description = "해당 회원의 위시 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<GetWishedProductResponse>> getWishedProducts(@LoginMember Member member, Pageable pageable) {
        return ResponseEntity.ok().body(wishedProductService.getWishedProducts(member, pageable));
    }

    @Operation(summary = "위시 리스트에 상품 추가", description = "해당 상품을 특정 회원의 위시 리스트에 추가합니다.")
    @PostMapping
    public ResponseEntity<WishedProductDto> addWishedProduct(
        @LoginMember Member member,
        @Valid @RequestBody AddWishedProductRequest addWishedProductRequest
    ) {
        return ResponseEntity.ok().body(wishedProductService.addWishedProduct(member, addWishedProductRequest));
    }

    @Operation(summary = "위시 리스트의 상품 삭제", description = "해당 상품을 특정 회원의 위시 리스트에서 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<WishedProductDto> deleteWishedProduct(@LoginMember Member member, @PathVariable("id") long id) {
        return ResponseEntity.ok().body(wishedProductService.deleteWishedProduct(id));
    }
}
