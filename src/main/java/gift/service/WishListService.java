package gift.service;

import gift.dto.ProductDTO;
import gift.entity.MemberEntity;
import gift.entity.ProductEntity;
import gift.entity.WishListEntity;
import gift.dto.WishListDTO;
import gift.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public WishListService(WishListRepository wishListRepository,
        WishListRepository wishListRepository1, ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishListRepository = wishListRepository1;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    private WishListEntity dtoToEntity(Long userId, ProductDTO product) throws Exception {
        MemberEntity memberEntity = memberRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        ProductEntity productEntity = productRepository.findById(product.getId())
            .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        return new WishListEntity(productEntity, memberEntity);
    }

    public Page<WishListDTO> readWishList(Long userId, Pageable pageable) {
        Page<WishListEntity> wishListEntities = wishListRepository.findByUserEntity_Id(userId, pageable);
        return wishListEntities.map(WishListEntity::toDTO);
    }

    @Transactional
    public void addProductToWishList(Long userId, ProductDTO product) throws Exception {
        wishListRepository.save(dtoToEntity(userId, product));
    }

    // 특정 유저의 id로 만들어진 위시리스트 전체 삭제
    @Transactional
    public void removeWishList(Long userId) {
        List<WishListEntity> wishListEntities = wishListRepository.findByUserEntity_Id(userId,Pageable.unpaged()).getContent();
        wishListRepository.deleteAll(wishListEntities);
    }

    // 특정 유저의 특정 상품 위시리스트 삭제
    @Transactional
    public void removeProductFromWishList(Long userId, Long productId) {
        Optional<WishListEntity> wishListEntityOpt = wishListRepository.findByUserEntity_IdAndProductEntity_Id(userId, productId);
        wishListEntityOpt.ifPresent(wishListRepository::delete);
    }

    @Transactional
    public void removeOptionFromWishList(Long userId, Long optionId) {
        Optional<WishListEntity> wishListEntityOpt = wishListRepository.findByUserEntity_IdAndOptionEntity_Id(userId, optionId);
        wishListEntityOpt.ifPresent(wishListRepository::delete);
    }

}
