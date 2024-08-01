package gift.service;

import gift.dto.memberDto.MemberDto;
import gift.dto.productDto.ProductMapper;
import gift.dto.productDto.ProductResponseDto;
import gift.dto.wishDto.WishDto;
import gift.exception.ValueNotFoundException;
import gift.model.member.Member;
import gift.model.product.Product;
import gift.model.wish.Wish;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public WishListService(WishRepository wishRepository, MemberRepository memberRepository,ProductRepository productRepository, ProductMapper productMapper){
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Page<Wish> getAllWishes(MemberDto memberDto, Pageable pageable) {
        Member member = memberRepository.findByEmail(memberDto.email()).
                orElseThrow(() -> new ValueNotFoundException("Member not exists in Database"));

        return wishRepository.findAllByMemberId(member.getId(), pageable);
    }

    public ProductResponseDto insertWish(WishDto wishDto, MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.email()).
                orElseThrow(() -> new ValueNotFoundException("Member not exists in Database"));
        Product product = productRepository.findById(wishDto.getProductId()).
                orElseThrow(() -> new ValueNotFoundException("Product not exists in Database"));

        Wish wish = new Wish(product,member);
        wishRepository.save(wish);
        return productMapper.toProductResponseDto(product);
    }

    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }


    /*public void updateWish(Long id, WishDto wishDto){
        Wish targetWish = wishRepository.findById(id).get();
        Wish newWish = new Wish(wishDto.getProduct(),wishDto.getMember(),wishDto.getAmount());
        targetWish.updateWish(newWish);
        wishRepository.save(targetWish);
    }*/
}
