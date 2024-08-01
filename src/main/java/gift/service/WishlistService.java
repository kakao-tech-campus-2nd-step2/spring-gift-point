package gift.service;

import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<Wishlist> getAllWishlist(String token, int page, int size) {
        Pageable pageRequest = createPageRequestUsing(page, size);
        var member_id = memberRepository.searchIdByToken(token);

        int start = (int) pageRequest.getOffset();
        int end = start + pageRequest.getPageSize();
        if (page > 0) { start += 1; }

        List<Wishlist> pageContent = wishlistRepository.findByIdAndIdAndMember_id(start, end, member_id);
        return new PageImpl<>(pageContent, pageRequest, pageContent.size());
    }

    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }
  
    public void deleteItem(String token, int product_id) {
        var member_id = memberRepository.searchIdByToken(token);

        if(isItem(member_id, product_id)) {
            wishlistRepository.deleteByMember_idAndProduct_id(member_id, product_id);
        }
        else {
            throw new NoSuchElementException();
        }
    }

    public void changeNum(String token, int product_id, int count) {
        var member_id = memberRepository.searchIdByToken(token);
        var member = memberRepository.findById(member_id);
        var product = productRepository.findById(product_id);

        try {
            if (count == 0) {
                wishlistRepository.deleteByMember_idAndProduct_id(member_id, product_id);
            } else {
                var wishlist = new Wishlist(member, product, count);
                wishlistRepository.save(wishlist);
            }
        }
        catch(Exception e) {
            throw new NoSuchElementException();
        }
    }

    public void addItem(String token, int product_id) {
        var member_id = memberRepository.searchIdByToken(token);
        var member = memberRepository.findById(member_id);
        var product = productRepository.findById(product_id);

        try {
            if (isItem(member_id, product_id)) {
                var num = wishlistRepository.searchCount_productByMember_idAndProduct_id(member_id, product_id);
                var wishlist = new Wishlist(member, product, num+1);
                wishlistRepository.save(wishlist);
            } else {
                var wishlist = new Wishlist(member, product, 1);
                wishlistRepository.save(wishlist);
            }
        }
        catch(Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isItem(int member_id, int product_id) {
        return wishlistRepository.searchCount_productByMember_idAndProduct_id(member_id, product_id) > 0;
    }
}
