package gift.service;

import gift.domain.PointPayment.CreatePointPayment;
import gift.domain.PointPayment.PointPaymentDetail;
import gift.domain.PointPayment.PointPaymentSimple;
import gift.domain.PointPayment.getList;
import gift.entity.DiscountPolicyEntity;
import gift.entity.PointPaymentEntity;
import gift.entity.ProductOptionEntity;
import gift.entity.UserEntity;
import gift.mapper.PointPaymentMapper;
import gift.repository.DiscountPolicyRepository;
import gift.repository.PointPaymentRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.UserRepository;
import gift.util.ParsingPram;
import gift.util.errorException.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PointPaymentService {

    private final PointPaymentRepository pointPaymentRepository;
    private final UserRepository userRepository;
    private final ProductOptionRepository productOptionRepository;
    private final DiscountPolicyRepository discountPolicyRepository;

    private final PointPaymentMapper pointPaymentMapper;

    private final ParsingPram parsingPram;

    @Autowired
    public PointPaymentService(PointPaymentRepository pointPaymentRepository,
        UserRepository userRepository, ProductOptionRepository productOptionRepository,
        DiscountPolicyRepository discountPolicyRepository,
        PointPaymentMapper pointPaymentMapper, ParsingPram parsingPram) {
        this.pointPaymentRepository = pointPaymentRepository;
        this.userRepository = userRepository;
        this.productOptionRepository = productOptionRepository;
        this.discountPolicyRepository = discountPolicyRepository;
        this.pointPaymentMapper = pointPaymentMapper;
        this.parsingPram = parsingPram;
    }

    public Page<PointPaymentSimple> getPointPaymentList(HttpServletRequest req, getList param) {
        Page<PointPaymentEntity> pointPayment = pointPaymentRepository.findAllByUserIdAndIsRevoke(
            parsingPram.getId(req), 0, param.toPageable());

        return pointPaymentMapper.toSimpleList(pointPayment);
    }

    public PointPaymentDetail getPointPayment(HttpServletRequest req, long id) {
        PointPaymentEntity pointPayment = pointPaymentRepository.findByIdAndUserIdAndIsRevoke(
                parsingPram.getId(req), id, 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 결제내역이 존재하지 않습니다."));

        return pointPaymentMapper.toDetail(pointPayment);
    }

    public Long createPointPayment(HttpServletRequest req, CreatePointPayment create) {
        UserEntity user = userRepository.findByIdAndIsDelete(parsingPram.getId(req), 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        ProductOptionEntity option = productOptionRepository.findById(create.getProductOptionId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 상품의 옵션이 존재하지 않습니다."));

        DiscountPolicyEntity discountPolicy = discountPolicyRepository.findByIdAndIsDeleteAndEndDateLessThanEqual(
                create.getDiscountPolicyId(), 0, LocalDateTime.now())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 할인 정책이 없습니다."));

        Integer paymentAmount = pointPaymentMapper.toDiscount(create.getRegularPrice(),
            discountPolicy);

        PointPaymentEntity pointPayment = pointPaymentMapper.toEntity(create.getRegularPrice(),
            paymentAmount, option, user, discountPolicy);

        pointPaymentRepository.save(pointPayment);

        return pointPayment.getId();
    }

    public Long revokePointPayment(HttpServletRequest req, long id) {
        PointPaymentEntity pointPayment = pointPaymentRepository.findByIdAndUserIdAndIsRevoke(
                parsingPram.getId(req), id, 0)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 결제내역이 존재하지 않습니다."));

        pointPayment.setIsRevoke(1);

        return pointPayment.getId();
    }
}
