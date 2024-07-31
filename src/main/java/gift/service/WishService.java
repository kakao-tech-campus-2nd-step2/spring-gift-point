package gift.service;

import gift.domain.Wish;
import gift.domain.Wish.WishResponse;
import gift.entity.MemberEntity;
import gift.entity.ProductEntity;
import gift.entity.WishEntity;
import gift.error.NotFoundException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(
        WishRepository wishRepository,
        MemberRepository memberRepository,
        ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    //해당 사용자의 위시리스트 조회(기존)
    @Transactional(readOnly = true)
    public List<WishResponse> getWishListItems(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("멤버가 존재하지 않습니다."));

        List<WishEntity> wishListEntities = wishRepository.findByMemberEntity(memberEntity);
        return wishListEntities.stream()
            .map(WishResponse::from)
            .collect(Collectors.toList());
    }

    //해당 사용자의 위시리스트 조회(페이지 네이션)
    @Transactional(readOnly = true)
    public Page<WishResponse> getWishListItems(Long memberId, int page, int size) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("멤버가 존재하지 않습니다."));
        Pageable pageable = PageRequest.of(page, size);
        Page<WishEntity> wishListEntities = wishRepository.findByMemberEntity(memberEntity, pageable);
        return wishListEntities.map(WishResponse::from);
    }

    //위시리스트 추가
    @Transactional
    public void addWishListItem(Long memberId, Long productId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("멤버가 존재하지 않습니다."));
        ProductEntity productEntity = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("상품이 존재하지 않습니다."));
        WishEntity wishEntity =  new WishEntity(memberEntity, productEntity);
        wishRepository.save(wishEntity);
    }

    //위시리스트 삭제
    @Transactional
    public void deleteWishListItem(Long id) {
        wishRepository.deleteById(id);
    }

}
