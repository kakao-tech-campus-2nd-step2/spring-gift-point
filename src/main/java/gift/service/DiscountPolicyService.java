package gift.service;

import gift.domain.DiscountPolicy.CreateDiscountPolicy;
import gift.domain.DiscountPolicy.DiscountPolicyDetail;
import gift.domain.DiscountPolicy.DiscountPolicySimple;
import gift.domain.DiscountPolicy.getList;
import gift.entity.DiscountPolicyEntity;
import gift.entity.ProductEntity;
import gift.mapper.DiscountPolicyMapper;
import gift.repository.DiscountPolicyRepository;
import gift.repository.ProductRepository;
import gift.util.errorException.BaseHandler;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscountPolicyService {

    private final DiscountPolicyRepository discountPolicyRepository;
    private final ProductRepository productRepository;

    private final DiscountPolicyMapper discountPolicyMapper;

    @Autowired
    public DiscountPolicyService(DiscountPolicyRepository discountPolicyRepository,
        ProductRepository productRepository, DiscountPolicyMapper discountPolicyMapper) {
        this.discountPolicyRepository = discountPolicyRepository;
        this.productRepository = productRepository;
        this.discountPolicyMapper = discountPolicyMapper;
    }

    public Page<DiscountPolicySimple> getDiscountPolicyList(getList param) {
        Page<DiscountPolicyEntity> discountPolicy = discountPolicyRepository.findAllByIsDeleteAndEndDateLessThanEqual(
            0, param.toPageable(), LocalDateTime.now());
        return discountPolicyMapper.toSimpleList(discountPolicy);
    }

    public DiscountPolicyDetail getDiscountPolicy(long id) {
        DiscountPolicyEntity discountPolicy = discountPolicyRepository.findByIdAndIsDeleteAndEndDateLessThanEqual(
                id, 0, LocalDateTime.now())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 할인 정책이 없습니다."));

        return discountPolicyMapper.toDetail(discountPolicy);
    }

    public Long createDiscountPolicy(CreateDiscountPolicy create) {
        ProductEntity product = productRepository.findById(create.getProductId())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

        if (create.getEndDate().isBefore(LocalDateTime.now())){
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "종료 시간은 현재보다 이전일수는 없습니다.");
        }

        DiscountPolicyEntity discountPolicy = discountPolicyMapper.toEntity(create, product);
        discountPolicyRepository.save(discountPolicy);

        return discountPolicy.getId();
    }

    @Transactional
    public Long deleteDiscountPolicy(long id) {
        DiscountPolicyEntity discountPolicy = discountPolicyRepository.findByIdAndIsDeleteAndEndDateLessThanEqual(
                id, 0, LocalDateTime.now())
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 할인 정책이 없습니다."));

        discountPolicy.setIsDelete(1);

        return discountPolicy.getId();
    }
}
