package gift.service;

import gift.dto.ProductInfo;
import gift.dto.request.WishListRequest;
import gift.dto.response.GetWishListResponse;
import gift.dto.response.WishListPageResponse;
import gift.dto.response.WishListResponse;
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

import java.util.List;

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

    public GetWishListResponse getWishList(String token){

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");

        if(memberRepository.findById(memberId).isEmpty()){
            throw new CustomException("Member with token not exist", HttpStatus.UNAUTHORIZED, -40101);
        }else{
            List<WishList> wishLists = wishListRepository.findByMemberId(memberId);
            return new GetWishListResponse(wishLists.stream().map(WishListResponse::fromEntity).toList());
        }
       
    }

    @Transactional
    public WishListResponse addWishList(String token, WishListRequest wishListRequest) {

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException("Member with token not exist", HttpStatus.UNAUTHORIZED, -40101));
        Product product = productRepository.findById(wishListRequest.getProductId())
            .orElseThrow(() -> new CustomException("Product with id " +  wishListRequest.getProductId() + " not found", HttpStatus.NOT_FOUND, -40401));

        if(wishListRepository.findByMemberIdAndProductId(memberId, wishListRequest.getProductId()).isEmpty()){
            WishList savedWishList = wishListRepository.save(new WishList(member, product));
            return new WishListResponse(savedWishList.getId(), ProductInfo.fromEntity(product));
        }else{
            throw new CustomException("WishList already exists", HttpStatus.CONFLICT, -40905);
        }

    }

    @Transactional
    public void deleteWishList(String token, long wishListId) {

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");
        List<WishList> wishLists = wishListRepository.findByMemberId(memberId);

        WishList requestedWishList = wishListRepository.findById(wishListId)
            .orElseThrow(() -> new CustomException("wishList with id " + wishListId + " not found", HttpStatus.NOT_FOUND, -40403));
        
        if(wishLists.contains(requestedWishList)){
            wishListRepository.delete(requestedWishList);
        }else{
            throw new CustomException("Can't delete other wish", HttpStatus.FORBIDDEN, -40302);
        }
    }
}
