package gift.service;

import gift.domain.ProductOption.optionDetail;
import gift.domain.ProductOrder.decreaseProductOption;
import gift.entity.ProductOptionEntity;
import gift.entity.WishEntity;
import gift.entity.enums.SocialType;
import gift.mapper.ProductOptionMapper;
import gift.repository.ProductOptionRepository;
import gift.repository.WishRepository;
import gift.util.ApiCall;
import gift.util.ParsingPram;
import gift.util.errorException.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOrderService {

    private final ProductOptionRepository productOptionRepository;
    private final WishRepository wishRepository;
    private final ProductOptionMapper productOptionMapper;
    private final ApiCall apiCall;
    private final ParsingPram parsingPram;

    @Autowired
    public ProductOrderService(ProductOptionRepository productOptionRepository,
        WishRepository wishRepository, ProductOptionMapper productOptionMapper,
        ApiCall apiCall, ParsingPram parsingPram) {
        this.productOptionRepository = productOptionRepository;
        this.wishRepository = wishRepository;
        this.productOptionMapper = productOptionMapper;
        this.apiCall = apiCall;
        this.parsingPram = parsingPram;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public optionDetail decreaseProductOption(HttpServletRequest req,
        Long productId, Long optionId, decreaseProductOption decrease) {

        //        상품이 존재여부부
        ProductOptionEntity entity = productOptionRepository.findByProductIdAndId(
                productId, optionId)
            .orElseThrow(() -> new BaseHandler(HttpStatus.NOT_FOUND, "해당 상품의 옵션이 존재하지 않습니다."));

        //        재고가 요구치보다 적을경우
        if (entity.getQuantity() < decrease.getQuantity()) {
            throw new BaseHandler(HttpStatus.BAD_REQUEST, "재고가 부족합니다.");
        }

        //        재고 감소(수정)
        entity.setQuantity(entity.getQuantity() - decrease.getQuantity());

        //        위시리스트 포함되어 있으면 삭제
        Optional<WishEntity> wish = wishRepository.findByUserEntityIdAndProductEntityId(
            parsingPram.getId(req),
            productId);
        if (wish.isPresent()) {
            wishRepository.delete(wish.get());
        }

//        카카오 토큰으로 로그인시 메세지 전송 기능
        if (parsingPram.getSocialToken(req) != ""
            && parsingPram.getSocialType(req) == SocialType.KAKAO) {
            apiCall.sendMessageForMe(parsingPram.getSocialToken(req),
                entity.getProduct().getName() + "의 " + entity.getName() + "가 "
                    + decrease.getQuantity() + "개 주문되었습니다.",
                "https://www.google.com/", "https://www.google.com/");
        }

        return productOptionMapper.toDetail(entity);
    }
}
