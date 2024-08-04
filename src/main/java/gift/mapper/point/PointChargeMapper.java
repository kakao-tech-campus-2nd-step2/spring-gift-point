package gift.mapper.point;

import gift.domain.point.PointCharge.CreatePointCharge;
import gift.domain.point.PointCharge.PointChargeDetail;
import gift.domain.point.PointCharge.PointChargeSimple;
import gift.entity.point.PointChargeEntity;
import gift.entity.auth.UserEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class PointChargeMapper {
    public Page<PointChargeSimple> toSimpleList(Page<PointChargeEntity> all) {
        List<PointChargeSimple> simpleList = all.stream()
            .map(entity -> new PointChargeSimple(
                entity.getId(),
                entity.getPrice()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public PointChargeDetail toDetail(PointChargeEntity entity){
        return new PointChargeDetail(entity.getId(),entity.getPrice(),entity.getTransactionDate());
    }

    public PointChargeEntity toEntity(CreatePointCharge create, UserEntity userEntity){
        return new PointChargeEntity(create.getPrice(), userEntity);
    }
}
