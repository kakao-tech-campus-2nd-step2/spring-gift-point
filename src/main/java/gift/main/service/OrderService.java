package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.OrderRequest;
import gift.main.dto.OrderResponse;
import gift.main.dto.UserVo;
import gift.main.entity.Option;
import gift.main.entity.Order;
import gift.main.entity.Product;
import gift.main.entity.User;
import gift.main.repository.OptionRepository;
import gift.main.repository.OrderRepository;
import gift.main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;


    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        OptionRepository optionRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
    }

    /**
     * 이 부분 생성할때 팩토리 디자인 패턴을 이용해볼 수는 없나요?
     * 여기저기서 리파지토리를 엄청 접근하는데, 모두 서비스에서 에러처리를 하니까 무거워보여서요
     */
    @Transactional
    public OrderResponse orderProduct(OrderRequest orderRequest, UserVo sessionUserVo) {
        //구매자 찾기
        User buyer = userRepository.findByEmail(sessionUserVo.getEmail()).get();

        //옵션찾기
        Option option = optionRepository.findById(orderRequest.optionId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OPTION));

        //상품 찾기
        Product purchasedProduct = option.getProduct();

        Order order = new Order(orderRequest, buyer, option, purchasedProduct);
        Order saveOrder = orderRepository.save(order);

        return new OrderResponse(saveOrder);
    }
}
