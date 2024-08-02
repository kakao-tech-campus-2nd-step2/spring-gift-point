package gift.service;

import gift.domain.PointCharge.CreatePointCharge;
import gift.domain.PointCharge.PointChargeDetail;
import gift.domain.PointCharge.PointChargeSimple;
import gift.domain.PointCharge.getList;
import gift.entity.PointChargeEntity;
import gift.entity.UserEntity;
import gift.mapper.PointChargeMapper;
import gift.repository.PointChargeRepository;
import gift.repository.UserRepository;
import gift.util.ParsingPram;
import gift.util.errorException.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointChargeService {

    private final PointChargeRepository pointChargeRepository;
    private final UserRepository userRepository;

    private final PointChargeMapper pointChargeMapper;

    private final ParsingPram parsingPram;


    @Autowired
    public PointChargeService(PointChargeRepository pointChargeRepository,
        UserRepository userRepository, PointChargeMapper pointChargeMapper,
        ParsingPram parsingPram) {
        this.pointChargeRepository = pointChargeRepository;
        this.userRepository = userRepository;
        this.pointChargeMapper = pointChargeMapper;
        this.parsingPram = parsingPram;
    }

    public Page<PointChargeSimple> getPointChargeList(HttpServletRequest req, getList param) {
        Page<PointChargeEntity> pointCharge = pointChargeRepository.findAllByUserIdAndIsRevoke(
            parsingPram.getId(req), 0, param.toPageable());

        return pointChargeMapper.toSimpleList(pointCharge);
    }

    public PointChargeDetail getPointCharge(HttpServletRequest req, long id) {
        PointChargeEntity pointChargeEntity = pointChargeRepository.findByIdAndUserIdAndIsRevoke(id,
                parsingPram.getId(req), 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 포인트 충전 내역이 없습니다."));

        return pointChargeMapper.toDetail(pointChargeEntity);
    }

    @Transactional
    public Long createPointCharge(HttpServletRequest req, CreatePointCharge create) {
        UserEntity userEntity = userRepository.findByIdAndIsDelete(parsingPram.getId(req), 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        PointChargeEntity pointChargeEntity = pointChargeMapper.toEntity(create, userEntity);
        pointChargeRepository.save(pointChargeEntity);

        userEntity.setPoint(create.getPrice());

        return pointChargeEntity.getId();
    }

    @Transactional
    public Long revokePointCharge(HttpServletRequest req, long id) {
        PointChargeEntity pointChargeEntity = pointChargeRepository.findByIdAndUserIdAndIsRevoke(id,
                parsingPram.getId(req), 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 포인트 충전 내역이 없습니다."));

        UserEntity userEntity = userRepository.findByIdAndIsDelete(parsingPram.getId(req), 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        if (userEntity.getPoint() < pointChargeEntity.getPrice()) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "취소할 포인트보다 보유한 포인트가 더 적습니다.");
        }
        userEntity.setPoint(pointChargeEntity.getPrice() * -1);
        pointChargeEntity.setIsRevoke(1);

        return pointChargeEntity.getId();
    }
}
