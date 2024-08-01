package gift.controller.admin;

import gift.DTO.member.MemberResponse;
import gift.DTO.product.ProductRequest;
import gift.DTO.product.ProductResponse;
import gift.domain.Member;
import gift.service.MemberService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/admin")
public class AdminController {

    private final ProductService productService;
    private final MemberService memberService;
    // 생성자를 사용하여 ProductRepository 초기화
    public AdminController(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    /**
     * 모든 상품 조회
     *
     * @return 모든 상품 목록
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProducts(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "2") Integer size
    ) {
        List<ProductResponse> products = productService.getProductsByPage(page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * id로 특정 상품 조회
     *
     * @param id 조회할 상품의 id
     * @return 조회된 상품 객체와 200 OK, 해당 id가 없으면 404 NOT FOUND
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        try {
            ProductResponse product = productService.getProductById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch(RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 새로운 상품 추가
     *
     * @param productRequest 추가할 상품
     * @return 같은 ID의 상품이 존재하지 않으면 201 Created, 아니면 400 Bad Request
     */
    @PostMapping("/products")
    public ResponseEntity<ProductResponse> addProduct(
        @RequestBody @Valid ProductRequest productRequest
    ) {
        ProductResponse product = productService.addProduct(productRequest);
        return new ResponseEntity<>(product, HttpStatus.CREATED); // 201 Created
    }

    /**
     * 상품 삭제
     *
     * @param id 삭제할 상품의 id
     * @return 삭제에 성공하면 204 NO CONTENT, 해당 ID의 상품이 없으면 404 NOT FOUND
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 상품 정보 수정
     *
     * @param id             수정할 상품의 id
     * @param updatedProduct 새로운 상품 객체
     * @return 상품 정보 수정에 성공하면 200 OK, 해당 id의 상품이 없으면 404 NOT FOUND
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable Long id,
        @RequestBody @Valid ProductRequest updatedProduct
    ) {
        ProductResponse product = productService.updateProduct(id, updatedProduct);
        return new ResponseEntity<>(product, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> members = memberService.getMembers();
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }
}
