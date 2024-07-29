package gift.service;

import gift.domain.Wish;
import gift.domain.Wish.createWish;
import gift.domain.Wish.wishDetail;
import gift.domain.Wish.wishSimple;
import gift.entity.ProductEntity;
import gift.entity.UserEntity;
import gift.entity.WishEntity;
import gift.mapper.WishMapper;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import gift.util.errorException.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishMapper wishMapper;

    @Autowired
    public WishService(WishRepository wishRepository, UserRepository userRepository,
        ProductRepository productRepository, WishMapper wishMapper) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.wishMapper = wishMapper;
    }

    public wishDetail getWish(long id) {
        WishEntity wishEntity = wishRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "위시리스트에 존재하지 않습니다."));

        return wishMapper.toDetail(wishEntity);
    }

    public Page<wishSimple> getWishList(Long userId, Wish.getList param) {
        Page<WishEntity> wishEntities = wishRepository.findByUserEntityId(userId,
            param.toPageable());

        return wishMapper.toSimpleList(wishEntities);
    }

    public Long createWish(Long userId, createWish create) {
        ProductEntity productEntity = productRepository.findById(create.getProductId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

        UserEntity userEntity = userRepository.findByIdAndIsDelete(userId, 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        if (wishRepository.findByUserEntityIdAndProductEntityId(userId, create.getProductId())
            .isPresent()) {
            throw new BaseHandler(HttpStatus.ALREADY_REPORTED, "이미 위시리스트에 존재 합니다.");
        }

        WishEntity wishEntity = wishMapper.toEntity(userEntity, productEntity);
        wishRepository.save(wishEntity);
        return wishEntity.getId();
    }

    public Long deleteWish(long id) {
        WishEntity wishEntity = wishRepository.findById(id)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "위시리스트에 존재하지 않습니다."));

        wishRepository.delete(wishEntity);
        return wishEntity.getId();
    }
}
