package gift.service;

import gift.dto.response.WishListPageResponse;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishList;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class WishListService {
    
    private WishListRepository wishListRepository;
    private MemberRepository memberRepository;
    private ProductRepository productRepository;

    private JwtUtil jwtUtil;

    public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository, ProductRepository productRepository, JwtUtil jwtUtil) {
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }


    public WishListPageResponse findWishListById(String token, int page, int size) {

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");
        Pageable pageable = PageRequest.of(page, size);

        WishListPageResponse wishListPageResponse = new WishListPageResponse();
        return wishListPageResponse.fromPage(wishListRepository.findByMemberIdOrderByProductIdDesc(pageable, memberId));
    }

    @Transactional
    public void addWishList(String token, long productId) {

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException("Member with id" + memberId + " not found", HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException("Product with id " +  productId + " not found", HttpStatus.NOT_FOUND));;
        wishListRepository.save(new WishList(member, product));

    }

    @Transactional
    public void deleteWishList(String token, long productId) {
        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");
        wishListRepository.deleteById(wishListRepository.findIdByMemberIdAndProductId(memberId, productId));;
    }
}
