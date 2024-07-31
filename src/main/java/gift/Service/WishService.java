package gift.Service;

import gift.Exception.Login.AuthorizedException;
import gift.Exception.ProductNotFoundException;
import gift.Exception.wish.WishNotFoundException;
import gift.Model.*;
import gift.Model.Entity.MemberEntity;
import gift.Model.Entity.ProductEntity;
import gift.Model.Entity.WishEntity;
import gift.Model.request.WishRequest;
import gift.Model.response.WishResponse;
import gift.Repository.ProductRepository;
import gift.Repository.MemberRepository;
import gift.Repository.WishRepository;
import org.springframework.data.domain.*;
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

    public void create(String email, WishRequest wishRequest){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        Optional<ProductEntity> productOptional = productRepository.findById(wishRequest.productId());
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

    public List<WishResponse> read(String email){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);

        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원정보가 없습니다.");
        }
        MemberEntity memberEntity = memberOptional.get();

        if(!memberEntity.getRole().equals(Role.ADMIN) && !memberEntity.getRole().equals(Role.CONSUMER)) {
            throw new AuthorizedException("접근 권한이 없습니다.");
        }

        List<WishEntity> wishEntities = wishRepository.findByMemberId(memberEntity.getId());
        List<WishResponse> productNames = new ArrayList<>();

        for(WishEntity w : wishEntities){
            productNames.add(w.mapToResponse());
        }

        return productNames;
    }

    public void delete(String email, Long wishId){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);
        Optional<WishEntity> wishOptional = wishRepository.findById(wishId);

        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원정보가 없습니다.");
        }

        if(wishOptional.isEmpty()){
            throw new WishNotFoundException("상품을 찾을 수 없습니다.");
        }

        MemberEntity memberEntity = memberOptional.get();
        if(!memberEntity.getRole().equals(Role.ADMIN) && !memberEntity.getRole().equals(Role.CONSUMER)) {
            throw new AuthorizedException("접근 권한이 없습니다.");
        }

        wishRepository.deleteById(wishId);
    }

    public Page<WishResponse> getPage(String email, int page, int size, String sort){
        List<WishResponse> dtoList = read(email);
        Sort sortType = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sortType);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());

        List<WishResponse> subList = dtoList.subList(start, end);

        return new PageImpl<>(subList, pageable, dtoList.size());
    }
}
