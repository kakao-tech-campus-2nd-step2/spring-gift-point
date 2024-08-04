package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.pageDTO.PageRequestDTO;
import gift.dto.pageDTO.ProductPageResponseDTO;
import gift.dto.productDTO.ProductAddRequestDTO;
import gift.dto.productDTO.ProductAddResponseDTO;
import gift.dto.productDTO.ProductGetResponseDTO;
import gift.dto.productDTO.ProductUpdateRequestDTO;
import gift.dto.productDTO.ProductUpdateResponseDTO;
import gift.exception.AuthorizationFailedException;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.exception.ServerErrorException;
import gift.model.Member;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 관리 API", description = "상품 관리를 위한 API")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품을 조회합니다.")
    public ResponseEntity<ProductPageResponseDTO> getAllProducts(
        @Valid PageRequestDTO pageRequestDTO) {
        try {
            Pageable pageable = PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size(),
                Sort.by(pageRequestDTO.sort()));
            ProductPageResponseDTO productPageResponseDTO = productService.getAllProducts(pageable);
            return ResponseEntity.ok(productPageResponseDTO);
        } catch (Exception e) {
            throw new InvalidInputValueException("잘못된 값이 입력되었습니다.");
        }
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "ID로 상품을 조회합니다.")
    public ResponseEntity<ProductGetResponseDTO> getProduct(@PathVariable Long productId) {
        ProductGetResponseDTO productGetResponseDTO = productService.getProductById(productId);
        if (productGetResponseDTO == null) {
            throw new NotFoundException("상품을 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(productGetResponseDTO);
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    public ResponseEntity<ProductAddResponseDTO> addProduct(
        @RequestBody @Valid ProductAddRequestDTO productAddRequestDTO,
        @LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        try {
            ProductAddResponseDTO productAddResponseDTO = productService.addProduct(
                productAddRequestDTO);
            return ResponseEntity.status(201).body(productAddResponseDTO);
        } catch (Exception e) {
            throw new ServerErrorException("서버 오류가 발생했습니다.");
        }
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(@PathVariable Long productId,
        @RequestBody @Valid ProductUpdateRequestDTO productUpdateRequestDTO,
        @LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        try {
            ProductUpdateResponseDTO productUpdateResponseDTO = productService.updateProduct(
                productUpdateRequestDTO, productId);
            return ResponseEntity.ok(productUpdateResponseDTO);
        } catch (NotFoundException e) {
            throw new NotFoundException("상품을 찾을 수 없습니다.");
        } catch (Exception e) {
            throw new ServerErrorException("서버 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId,
        @LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        try {
            productService.deleteProductAndWishlistAndOptions(productId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            throw new NotFoundException("상품을 찾을 수 없습니다.");
        } catch (Exception e) {
            throw new ServerErrorException("서버 오류가 발생했습니다.");
        }
    }
}
