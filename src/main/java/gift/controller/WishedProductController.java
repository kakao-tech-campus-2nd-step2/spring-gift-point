package gift.controller;

import gift.argumentresolver.LoginMember;
import gift.dto.member.MemberDto;
import gift.dto.wishedProduct.AddWishedProductRequest;
import gift.dto.wishedProduct.GetWishedProductResponse;
import gift.dto.wishedProduct.WishedProductDto;
import gift.service.WishedProductService;
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
    public ResponseEntity<Page<GetWishedProductResponse>> getWishedProducts(@LoginMember MemberDto memberDto, Pageable pageable) {
        return ResponseEntity.ok().body(wishedProductService.getWishedProducts(memberDto, pageable));
    }

    @Operation(summary = "위시 리스트에 상품 추가", description = "해당 상품을 특정 회원의 위시 리스트에 추가합니다.")
    @PostMapping
    public ResponseEntity<WishedProductDto> addWishedProduct(
        @LoginMember MemberDto memberDto,
        @Valid @RequestBody AddWishedProductRequest addWishedProductRequest
    ) {
        return ResponseEntity.ok().body(wishedProductService.addWishedProduct(memberDto, addWishedProductRequest));
    }

    @Operation(summary = "위시 리스트의 상품 삭제", description = "해당 상품을 특정 회원의 위시 리스트에서 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<WishedProductDto> deleteWishedProduct(@LoginMember MemberDto memberDto, @PathVariable("id") long id) {
        return ResponseEntity.ok().body(wishedProductService.deleteWishedProduct(id));
    }
}
