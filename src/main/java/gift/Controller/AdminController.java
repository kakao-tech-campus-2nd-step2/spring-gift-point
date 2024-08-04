package gift.Controller;

import gift.DTO.PointDTO;
import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.Model.Member;

import gift.Model.Product;
import gift.Service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Admin", description = "관리자 화면")
@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }
    @Operation(
        summary = "모든 상품 가져오기",
        description = "등록된 모든 상품을 가져와 index.html로 전달"
    )
    @ApiResponse(
        responseCode = "200",
        description = "모든 상품 페이지 연결 성공"
    )
    @Parameters({
        @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체"),
        @Parameter(name = "pageable", description = "List에 담긴 Product객체를 개수에 맞춰서 page로 리턴")
    })
    @GetMapping("/admin/products")
    public String getProducts(Model model, Pageable pageable) {
        Page<Product> productPage = adminService.findAll(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        return "index";
    }

    @Operation(
        summary = "관리자 상품 더하기 페이지",
        description = "더할 상품의 정보를 입력을 받기 위해 post.html과 연결"
    )
    @ApiResponse(
        responseCode = "200",
        description = "관리자 상품 더하기 페이지 연결 성공"
    )
    @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체")
    @GetMapping("/admin/products/add")
    public String newProductForm(Model model) {
        model.addAttribute("product", new ProductDTO(0L,"",0,"",new Category(0L,"","","","",null),null));
        model.addAttribute("categories", adminService.getAllCategory());
        return "post";
    }

    @Operation(
        summary = "상품 더하기",
        description = "상품을 product테이블에 저장"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 더하기 성공"
    )
    @Parameter(name = "productDTO", description = "더할 상품 객체")
    @PostMapping("/admin/products")
    public String createProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        adminService.addProduct(productDTO);
        return "redirect:/admin/products";
    }

    @Operation(
        summary = "상품 수정하기",
        description = "상품을 수정"
    )
    @ApiResponse(
        responseCode = "200",
        description = "관리자 상품 수정 페이지 연결 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "수정할 상품 객체의 ID"),
        @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체")
    })
    @GetMapping("/admin/products/update/{productId}")
    public String editProductForm(@PathVariable(value = "productId") Long productId, Model model) {
        Product product = adminService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", adminService.getAllCategory());
        return "update";
    }

    @Operation(
        summary = "상품 수정하기",
        description = "상품을 테이블에 업데이트"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 업데이트 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "수정할 상품 Id"),
        @Parameter(name = "newProductDTO", description = "수정할 상품 객체")
    })
    @PostMapping("/admin/products/update/{productId}")
    public String updateProduct(@PathVariable(value = "productId") Long productId, @Valid @ModelAttribute ProductDTO newProductDTO) {
        adminService.updateProduct(newProductDTO);
        return "redirect:/admin/products";
    }

    @Operation(
        summary = "상품 삭제",
        description = "상품을 product테이블에서 삭제"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 삭제 성공"
    )
    @Parameter(name = "productId", description = "삭제할 상품 객체 Id")
    @PostMapping("/admin/products/delete/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") Long productId) {
        adminService.deleteProduct(productId);
        return "redirect:/admin/products";
    }

    @Operation(
        summary = "모든 회원 가져오기",
        description = "모든 회원을 가져와 member.html로 전달"
    )
    @ApiResponse(
        responseCode = "200",
        description = "회원 페이지 연결 성공"
    )
    @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체")
    @GetMapping("/admin/members")
    public String getMembers(Model model) {
        List<Member> members = adminService.getAllMembers();
        model.addAttribute("members", members);
        return "member";
    }

    @Operation(
        summary = "회원 포인트 충전",
        description = "회원 포인트 충전 페이지"
    )
    @ApiResponse(
        responseCode = "200",
        description = "회원 포인트 충전 페이지 연결 성공"
    )
    @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체")
    @GetMapping("/admin/members/{memberId}")
    public String chargePoint(Model model, @PathVariable(value = "memberId") Long memberId) {
        model.addAttribute("point", new PointDTO(0));
        model.addAttribute("memberId", memberId);
        return "memberCharge";
    }

    @Operation(
        summary = "회원 포인트 충전하기",
        description = "회원에게 포인트를 충전"
    )
    @ApiResponse(
        responseCode = "200",
        description = "회원 포인트 충전 성공"
    )
    @Parameters({
        @Parameter(name = "memberId", description = "포인트 충전할 회원 ID"),
        @Parameter(name = "PointDTO", description = "더할 포인트 DTO")
    })
    @PostMapping("/admin/members/{memberId}")
    public String chargePoint(@PathVariable(value = "memberId") Long memberId, @ModelAttribute PointDTO pointDTO) {
        adminService.chargePoint(memberId,pointDTO.getPoint());
        return "redirect:/admin/members";
    }

}


