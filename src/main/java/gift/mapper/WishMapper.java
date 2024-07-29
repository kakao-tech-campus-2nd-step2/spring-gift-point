package gift.mapper;

import gift.domain.Wish.wishDetail;
import gift.domain.Wish.wishSimple;
import gift.entity.ProductEntity;
import gift.entity.UserEntity;
import gift.entity.WishEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class WishMapper {

    public wishDetail toDetail(WishEntity entity) {
        wishDetail detail = new wishDetail();

        detail.setId(entity.getId());
        detail.setUserId(entity.getUserDTO().getId());
        detail.setProductId(entity.getProductDTO().getId());
        detail.setName(entity.getProductDTO().getName());
        detail.setPrice(entity.getProductDTO().getPrice());
        detail.setImageUrl(entity.getProductDTO().getImageUrl());

        return detail;
    }

    public Page<wishSimple> toSimpleList(Page<WishEntity> all) {
        List<wishSimple> simpleList = all.stream()
            .map(entity -> new wishSimple(
                entity.getId(),
                entity.getUserDTO().getId(),
                entity.getProductDTO().getId()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public WishEntity toEntity(UserEntity userEntity, ProductEntity productEntity) {
        return new WishEntity(productEntity, userEntity);
    }

}
