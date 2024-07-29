package gift.Service;

import gift.Exception.AuthorizedException;
import gift.Exception.ProductNotFoundException;
import gift.Model.*;
import gift.Model.DTO.ProductDTO;
import gift.Model.Entity.MemberEntity;
import gift.Model.Entity.ProductEntity;
import gift.Model.Entity.WishEntity;
import gift.Repository.ProductRepository;
import gift.Repository.MemberRepository;
import gift.Repository.WishRepository;
import gift.Token.JwtTokenProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository){
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public void create(String email, String name){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        Optional<ProductEntity> productOptional = productRepository.findByName(name);
        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원정보가 없습니다.");
        }

        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }

        MemberEntity memberEntity = memberOptional.get();
        ProductEntity productEntity = productOptional.get();
        if(!memberEntity.getRole().equals(Role.ADMIN) && !memberEntity.getRole().equals(Role.CONSUMER))
            throw new AuthorizedException("접근 권한이 없습니다.");

        wishRepository.save(new WishEntity(memberEntity, productEntity));
    }

    public List<String> read(String email){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);

        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원정보가 없습니다.");
        }
        MemberEntity memberEntity = memberOptional.get();

        if(!memberEntity.getRole().equals(Role.ADMIN) && !memberEntity.getRole().equals(Role.CONSUMER)) {
            throw new AuthorizedException("접근 권한이 없습니다.");
        }

        List<WishEntity> wishEntities = wishRepository.findByMemberId(memberEntity.getId());
        List<String> productNames = new ArrayList<>();

        for(WishEntity w : wishEntities){
            productNames.add(w.getProduct().getName());
        }

        return productNames;
    }

    public void delete(String email, String name){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        Optional<ProductEntity> productOptional = productRepository.findByName(name);

        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원정보가 없습니다.");
        }

        if(productOptional.isEmpty()){
            throw new ProductNotFoundException("상품을 찾을 수 없습니다.");
        }

        ProductEntity productEntity = productOptional.get();
        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.getRole().equals(Role.ADMIN) && !memberEntity.getRole().equals(Role.CONSUMER)) {
            throw new AuthorizedException("접근 권한이 없습니다.");
        }

        wishRepository.delete(wishRepository.findByMemberIdAndProductId(memberEntity.getId(), productEntity.getId()));
    }

    public Page<String> getPage(String email, int page){
        List<String> dtoList = read(email);
        Pageable pageable = PageRequest.of(page, 10);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());

        List<String> subList = dtoList.subList(start, end);

        return new PageImpl<>(subList, pageable, dtoList.size());
    }
}
