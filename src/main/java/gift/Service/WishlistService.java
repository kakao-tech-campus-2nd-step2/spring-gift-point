package gift.Service;

import gift.Exception.UnauthorizedException;
import gift.Model.Member;
import gift.Model.Product;
import gift.Repository.MemberRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishlistRepository;


import java.time.LocalDateTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository, MemberRepository memberRepository){
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Page<Product> getAllWishlist(String email, Pageable pageable) {
        return wishlistRepository.getAllWishlist(email, pageable);

    }

    public Product getProductById(long productId){
        return productRepository.findProductById(productId);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    public void addWishlist(Long memberId, Long productId){
        LocalDateTime createdDate = LocalDateTime.now();
        wishlistRepository.addProductInWishlist(memberId, productId, createdDate);
    }

    public Long getWishlistId(String email, Long productId){
        return wishlistRepository.getWishlistIdByMemberEmailAndProductId(email, productId);
    }

    public Product deleteWishlist(String email, Long productId, Long wishlistId){
        Product deleteProduct = productRepository.findProductById(productId);
        wishlistRepository.changeProductMemberNull(email,productId);
        wishlistRepository.deleteById(wishlistId);
        return deleteProduct;
    }

    public void checkUserByMemberEmail(String email){
        Member checkMember = memberRepository.findByEmail(email);
        if (checkMember == null){
            throw new UnauthorizedException("401 Unauthorized : Invalid or missing token");
        }
    }

    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
    public Sort getSort(String[] sort){
        Sort newSort = Sort.by(Sort.Order.asc(sort[0])); // 기본으로 asc인 sort[0]에 대해서 Sort 객체 생성
        if (sort.length > 1 && "desc".equalsIgnoreCase(sort[1])) { // 올바른 요청이면 길이가 2이고 desc 요청이 들어오면
            newSort = Sort.by(Sort.Order.desc(sort[0])); // desc로 객체 생성
        }
        return newSort;
    }
}
