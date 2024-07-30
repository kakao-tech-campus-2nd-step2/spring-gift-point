package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.constants.ErrorMessage;
import gift.domain.KakaoMessageSender;
import gift.dto.KakaoCommerceMessage;
import gift.dto.OptionSubtractRequest;
import gift.dto.OrderFindAllResponse;
import gift.dto.OrderRequest;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.OrderProductOption;
import gift.entity.Product;
import gift.repository.KakaoTokenRepository;
import gift.repository.MemberJpaDao;
import gift.repository.OptionJpaDao;
import gift.repository.OrderJpaDao;
import gift.repository.OrderProductOptionJpaDao;
import gift.repository.ProductJpaDao;
import gift.repository.WishlistJpaDao;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductJpaDao productJpaDao;
    private final MemberJpaDao memberJpaDao;
    private final WishlistJpaDao wishlistJpaDao;
    private final OptionJpaDao optionJpaDao;
    private final OrderJpaDao orderJpaDao;
    private final OrderProductOptionJpaDao orderProductOptionJpaDao;

    private final OptionService optionService;

    private final KakaoMessageSender kakaoMessageSender;

    public OrderService(ProductJpaDao productJpaDao, MemberJpaDao memberJpaDao,
        WishlistJpaDao wishlistJpaDao, OptionJpaDao optionJpaDao, OrderJpaDao orderJpaDao,
        OrderProductOptionJpaDao orderProductOptionJpaDao, OptionService optionService,
        KakaoMessageSender kakaoMessageSender) {
        this.productJpaDao = productJpaDao;
        this.memberJpaDao = memberJpaDao;
        this.wishlistJpaDao = wishlistJpaDao;
        this.optionJpaDao = optionJpaDao;
        this.orderJpaDao = orderJpaDao;
        this.orderProductOptionJpaDao = orderProductOptionJpaDao;
        this.optionService = optionService;
        this.kakaoMessageSender = kakaoMessageSender;
    }

    public Page<OrderFindAllResponse> findAllOrders(Pageable pageable) {
        return orderProductOptionJpaDao.findAll(pageable).map(order ->
            new OrderFindAllResponse(order.getOrder(), order.getProduct(), order.getOption())
        );
    }

    /**
     * 메모리에 저장된 카카오 토큰, 회원, 상품의 존재 여부를 확인 후 메시지 전송. 만약 위시 리스트에 추가된 상품이라면 위시 리스트에서 삭제.
     */
    @Transactional
    public void order(OrderRequest orderRequest, String email) throws JsonProcessingException {
        Member member = findMember(email);
        Product product = findProduct(orderRequest.getProductId());
        Option option = findOption(orderRequest.getOptionId());

        // 메시지 보내고 옵션 수량 차감
        sendMessageForMe(product, orderRequest, email);
        optionService.subtractOption(new OptionSubtractRequest(orderRequest));

        // 주문 객체 생성 및 insert
        Order order = new Order(member, orderRequest.getQuantity(), orderRequest.getMessage());
        saveOrder(order, product, option);

        // 만약 위시리스트에 존재하면 제거
        if (member.containsWish(product.getId())) {
            wishlistJpaDao.deleteByMember_EmailAndProduct_Id(email, product.getId());
        }
    }

    private void saveOrder(Order order, Product product, Option option) {
        orderJpaDao.save(order);
        orderProductOptionJpaDao.save(new OrderProductOption(order, product, option));
    }

    private void sendMessageForMe(Product product, OrderRequest orderRequest, String email)
        throws JsonProcessingException {
        String kakaoToken = KakaoTokenRepository.getAccessToken(email);
        kakaoMessageSender.sendForMe(kakaoToken,
            new KakaoCommerceMessage(product.getName(), product.getImageUrl(),
                orderRequest.getMessage(),
                product.getImageUrl(), (int) product.getPrice())
        );
    }

    private Member findMember(String email) {
        return memberJpaDao.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.MEMBER_NOT_EXISTS_MSG));
    }

    private Product findProduct(Long productId) {
        return productJpaDao.findById(productId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PRODUCT_NOT_EXISTS_MSG));
    }

    private Option findOption(Long optionId) {
        return optionJpaDao.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.OPTION_NOT_EXISTS_MSG));
    }
}
