package gift.service;

import gift.domain.ProductOrder.OrderDetail;
import gift.domain.ProductOrder.OrderSimple;
import gift.domain.ProductOrder.decreaseProductOption;
import gift.domain.ProductOrder.getList;
import gift.entity.ProductOptionEntity;
import gift.entity.ProductOrderEntity;
import gift.entity.UserEntity;
import gift.entity.WishEntity;
import gift.entity.enums.SocialType;
import gift.mapper.ProductOrderMapper;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductOrderRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import gift.util.ApiCall;
import gift.util.ParsingPram;
import gift.util.errorException.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOrderService {

    private final UserRepository userRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOrderRepository productOrderRepository;
    private final WishRepository wishRepository;
    private final ProductOrderMapper productOrderMapper;
    private final ApiCall apiCall;
    private final ParsingPram parsingPram;

    @Autowired
    public ProductOrderService(UserRepository userRepository,
        ProductOptionRepository productOptionRepository,
        ProductOrderRepository productOrderRepository, WishRepository wishRepository,
        ProductOrderMapper productOrderMapper, ApiCall apiCall, ParsingPram parsingPram) {
        this.userRepository = userRepository;
        this.productOptionRepository = productOptionRepository;
        this.productOrderRepository = productOrderRepository;
        this.wishRepository = wishRepository;
        this.productOrderMapper = productOrderMapper;
        this.apiCall = apiCall;
        this.parsingPram = parsingPram;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public OrderDetail decreaseProductOption(HttpServletRequest req, Long optionId,
        decreaseProductOption decrease) {
        //        유저 존재 검증
        UserEntity userEntity = userRepository.findByIdAndIsDelete(parsingPram.getId(req), 0)
            .orElseThrow(
                () -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."));

        //        상품이 존재여부부
        ProductOptionEntity productOptionEntity = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 상품의 옵션이 존재하지 않습니다."));

        //        재고가 요구치보다 적을경우
        if (productOptionEntity.getQuantity() < decrease.getQuantity()) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "재고가 부족합니다.");
        }

        //        재고 감소(수정)
        productOptionEntity.setQuantity(productOptionEntity.getQuantity() - decrease.getQuantity());

        //        위시리스트 포함되어 있으면 삭제
        Optional<WishEntity> wish = wishRepository.findByUserEntityIdAndProductEntityId(
            parsingPram.getId(req), productOptionEntity.getProduct().getId());
        if (wish.isPresent()) {
            wishRepository.delete(wish.get());
        }

        //        카카오 토큰으로 로그인시 메세지 전송 기능
        if (parsingPram.getSocialToken(req) != ""
            && parsingPram.getSocialType(req) == SocialType.KAKAO) {
            apiCall.sendMessageForMe(parsingPram.getSocialToken(req),
                productOptionEntity.getProduct().getName() + "의 " + productOptionEntity.getName()
                    + "가 "
                    + decrease.getQuantity() + "개 주문되었습니다.",
                "https://www.google.com/", "https://www.google.com/");
        }

        //        주문기록 저장
        ProductOrderEntity productOrderEntity = new ProductOrderEntity(decrease.getQuantity(),
            userEntity, productOptionEntity);
        productOrderRepository.save(productOrderEntity);

        return productOrderMapper.toDetail(productOptionEntity);
    }

    public Page<OrderSimple> getOrderList(HttpServletRequest req, getList param) {
        return productOrderMapper.toSimple(
            productOrderRepository.findAllByUserEntityId(parsingPram.getId(req),
                param.toPageable()));
    }
}
