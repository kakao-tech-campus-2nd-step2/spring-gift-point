package gift.controller;

import gift.constants.ResponseMsgConstants;
import gift.dto.betweenClient.product.OneProductResponseDTO;
import gift.dto.betweenClient.product.ProductPostRequestDTO;
import gift.dto.betweenClient.product.ProductResponseDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.swagger.GetProduct;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(description = "서버가 클라이언트에게 제품 목록 페이지를 제공합니다.", tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 제품 목록 페이지를 제공합니다.",
                    content = @Content(mediaType = "application/json", schema =  @Schema(implementation = GetProduct.class))),
            @ApiResponse(responseCode = "400", description = "카테고리가 존재하지 않거나, 요청 양식이 잘못된 경우 입니다.",
                    content = @Content(mediaType = "application/json", schema =  @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public Page<ProductResponseDTO> getProducts(@RequestParam(defaultValue = "1") Long categoryId,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getProductListByCategoryId(categoryId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(description = "서버가 클라이언트에게 제품 하나의 정보를 제공합니다.", tags = "Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 제품 정보를 제공합니다.",
                    content = @Content(mediaType = "text/html", schema =  @Schema(implementation = OneProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 해당 productId가 존재하지 않는 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<OneProductResponseDTO> getOneProduct(@PathVariable @Min(1) @NotNull Long id) {
        return new ResponseEntity<>(productService.getProductWithOptions(id), HttpStatus.OK);
    }

    @PostMapping
    @Hidden
    public ResponseEntity<ResponseDTO> addProduct(@RequestBody @Valid ProductPostRequestDTO productPostRequestDTO) {
        productService.addProduct(productPostRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @Hidden
    public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable @Min(1) @NotNull Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.NO_CONTENT);
    }


    @PutMapping("/{id}")
    @Hidden
    public ResponseEntity<ResponseDTO> updateProduct(@PathVariable @Min(1) @NotNull Long id, @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        productService.updateProduct(id, productRequestDTO);
        return new ResponseEntity<>(new ResponseDTO(false, ResponseMsgConstants.WELL_DONE_MESSAGE), HttpStatus.OK);
    }
}
