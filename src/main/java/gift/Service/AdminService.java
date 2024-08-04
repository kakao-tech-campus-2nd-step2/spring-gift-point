package gift.Service;

import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.Model.Member;
import gift.Model.Option;
import gift.Model.Product;
import gift.Repository.CategoryRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final ProductService productService;
    private final MemberService memberService;
    public AdminService(ProductService productService, MemberService memberService){
        this.productService = productService;
        this.memberService = memberService;
    }

    public Page<Product> findAll(Pageable pageable){
        return productService.findAll(pageable);
    }

    public Product getProductById(Long productId){
        return productService.getProductById(productId);
    }

    public void addProduct(ProductDTO productDTO){
        productService.addProduct(productDTO);
    }

    public void updateProduct(ProductDTO productDTO){
        productService.updateProduct(productDTO);
    }

    public void deleteProduct(Long productId){
        productService.deleteProduct(productId);
    }

    public List<Category> getAllCategory(){
        return productService.getAllCategory();
    }

    public List<Member> getAllMembers(){
        return memberService.getAllMembers();
    }

    public int chargePoint(Long memberId, int charge){
        Member member = memberService.getMemberById(memberId);
        int point = member.chargePoint(charge);
        memberService.updateMember(member);
        return point;
    }

}