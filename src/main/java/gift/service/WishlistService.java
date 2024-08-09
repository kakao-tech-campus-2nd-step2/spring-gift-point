package gift.service;

import gift.domain.WishlistDTO;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

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

    public Page<WishlistDTO> getAllWishlist(String token, int page, int size) {
        Pageable pageRequest = createPageRequestUsing(page, size);
        var member_id = memberRepository.searchIdByToken(token);

        int start = (int) pageRequest.getOffset();
        int end = start + pageRequest.getPageSize();
        if (page > 0) { start += 1; }

        List<Wishlist> pageContent = wishlistRepository.findByIdAndIdAndMember_id(start, end, member_id);
        List<WishlistDTO> dto = pageContent.stream().map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dto, pageRequest, pageContent.size());
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
        var product = productRepository.findById(product_id).orElseThrow(NoSuchElementException::new);

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
        var product = productRepository.findById(product_id).orElseThrow(NoSuchElementException::new);

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

    public WishlistDTO toDTO(Wishlist wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.id = wishlist.getId();
        dto.product = wishlist.getProduct();
        return dto;
    }

    public Integer getMostCommonCategoryId(int memberId) {
        List<Integer> productIds = wishlistRepository.findProductIdByMember_id(memberId);

        return productIds.stream()
                .map(productRepository::searchCategory_IdById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Map<String, Integer> getRecommendations(String token) {
        Map<String, Integer> body = new HashMap<>();
        var memberId = memberRepository.searchIdByToken(token);
        var categoryId = getMostCommonCategoryId(memberId);
        body.put("Most Frequent Category", categoryId);
        var productId = productRepository.searchProduct_IdByCategory_Id(categoryId).orElseThrow(NoSuchElementException::new);
        body.put("Recommended Product", productId);

        return body;
    }
}
