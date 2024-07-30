package gift.Service;

import gift.Model.Member;
import gift.Model.Product;
import gift.Repository.MemberRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishlistRepository;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        wishlistRepository.addProductInWishlist(memberId, productId);
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
        try {
            memberRepository.findByEmail(email);
        }catch (EmptyResultDataAccessException e){
            throw new IllegalArgumentException("이메일 다름");
        }
    }

    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email);
    }
}
