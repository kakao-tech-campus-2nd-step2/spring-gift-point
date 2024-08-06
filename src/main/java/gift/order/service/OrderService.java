package gift.order.service;

import gift.order.dto.OrderRequest;
import gift.product.facadeRepository.ProductFacadeRepository;
import gift.repository.JpaMemberRepository;
import gift.repository.JpaWishRepository;
import gift.exceptionAdvisor.exceptions.GiftBadRequestException;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.member.entity.Member;
import gift.product.entity.Product;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final ProductFacadeRepository productFacadeRepository;
    private final JpaMemberRepository jpaMemberRepository;
    private final JpaWishRepository jpaWishRepository;

    public OrderService(ProductFacadeRepository productFacadeRepository,
        JpaMemberRepository jpaMemberRepository,JpaWishRepository jpaWishRepository) {
        this.productFacadeRepository = productFacadeRepository;
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaWishRepository = jpaWishRepository;
    }

    public void create(OrderRequest orderRequest) {

        Product product = productFacadeRepository.getProduct(orderRequest.getProductId());
        Member member = jpaMemberRepository.findById(orderRequest.getMemberId())
            .orElseThrow(() -> new GiftNotFoundException("회원이 존재하지 않습니다."));

        if (product.getGiftOptionList().stream()
            .noneMatch(giftOption -> giftOption.getId().equals(orderRequest.getGiftOptionId()))) {
            throw new GiftNotFoundException("상품 옵션이 존재하지 않습니다.");
        }

        product.getGiftOptionList().stream()
            .filter(giftOption -> giftOption.getId().equals(orderRequest.getGiftOptionId()))
            .forEach(giftOption -> {
                if (giftOption.getQuantity() < orderRequest.getQuantity()) {
                    throw new GiftBadRequestException("상품 옵션 수량이 부족합니다.");
                }
                giftOption.subtractQuantity(orderRequest.getQuantity());
            });

        if(member.getWishList().stream().allMatch(wish -> Objects.equals(wish.getProduct().getId(),
            product.getId()))){
            jpaWishRepository.deleteByMemberIdAndProductId(member.getId(), product.getId());
        }

        usePoint(product.getPrice()*orderRequest.getQuantity(),member);

    }

    private int usePoint(int price, Member member){
        int memberPoint = member.getPoint();

        if(memberPoint < price){
            member.useAllPoint();
            return price - memberPoint;
        }
        member.usePoint(price);
        return 0;
    }
}
