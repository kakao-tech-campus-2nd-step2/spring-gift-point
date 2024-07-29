package gift.Service;

import gift.DTO.RequestWishDTO;
import gift.DTO.ResponseWishDTO;
import gift.Exception.ProductNotFoundException;
import gift.Exception.WishNotFoundException;
import gift.Model.Entity.Member;
import gift.Model.Entity.Product;
import gift.Model.Entity.Wish;
import gift.Repository.ProductRepository;
import gift.Repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;


    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }



    @Transactional
    public Wish addWish(Member member, RequestWishDTO requestWishDTO) {
        Product product = productRepository.findById(requestWishDTO.getProductId())
                .orElseThrow(()->new ProductNotFoundException("매칭되는 상품이 없습니다."));
        Wish wish = new Wish(member, product, requestWishDTO.getCount());
        return wishRepository.save(wish);
    }

    @Transactional(readOnly = true)
    public Page<Wish> getWishList(Member member, Pageable pageable) {
        Page<Wish> wishListPage= wishRepository.findByMember(member,pageable);
        return wishListPage;
    }

    @Transactional(readOnly = true)
    public List<ResponseWishDTO> getWish(Member member) {
        return  wishRepository.findWishListByMember(member)
                .stream()
                .map(ResponseWishDTO::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public Wish findWishByMemberAndProduct(Member member, Product product){
        Optional<Wish> wish= wishRepository.findByMemberAndProduct(member, product);
        return wish.orElseThrow(()->new WishNotFoundException("매칭되는 wish가 없습니다"));
    }

    @Transactional
    public void editWish(Member member, RequestWishDTO requestWishDTO) {
        Product product = productRepository.findById(requestWishDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("매칭되는 물건이 없습니다."));
        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new WishNotFoundException("매칭되는 wish가 없습니다"));
        wish.update(requestWishDTO.getCount());
    }

    @Transactional
    public void deleteWish(Member member, RequestWishDTO requestWishDTO) {
        Product product = productRepository.findById(requestWishDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("매칭되는 물건이 없습니다."));
        Wish wish = wishRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new WishNotFoundException("매칭되는 wish가 없습니다"));
        wishRepository.deleteById(wish.getId());
    }

}