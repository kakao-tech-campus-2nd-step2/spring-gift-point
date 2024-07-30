package gift.controller;

import gift.argumentresolver.LoginMember;
import gift.dto.MemberDTO;
import gift.dto.WishedProductDTO;
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
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<Page<WishedProductDTO>> getWishedProducts(@LoginMember MemberDTO memberDTO, Pageable pageable) {
        return ResponseEntity.ok().body(wishedProductService.getWishedProducts(memberDTO, pageable));
    }

    @Operation(summary = "위시 리스트에 상품 추가", description = "해당 상품을 특정 회원의 위시 리스트에 추가합니다.")
    @PostMapping
    public ResponseEntity<WishedProductDTO> addWishedProduct(@LoginMember MemberDTO memberDTO, @Valid @RequestBody WishedProductDTO wishedProductDTO) {
        return ResponseEntity.ok().body(wishedProductService.addWishedProduct(memberDTO, wishedProductDTO));
    }

    @Operation(summary = "위시 리스트의 상품 삭제", description = "해당 상품을 특정 회원의 위시 리스트에서 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<WishedProductDTO> deleteWishedProduct(@PathVariable("id") long id, @LoginMember MemberDTO memberDTO) {
        return ResponseEntity.ok().body(wishedProductService.deleteWishedProduct(id));
    }

    @Operation(summary = "위시 리스트의 상품 수정", description = "해당 상품을 특정 회원의 위시 리스트에서 수정합니다.")
    @PutMapping
    public ResponseEntity<WishedProductDTO> updateWishedProduct(@LoginMember MemberDTO memberDTO, @Valid @RequestBody WishedProductDTO wishedProductDTO) {
        return ResponseEntity.ok().body(wishedProductService.updateWishedProduct(wishedProductDTO));
    }
}
