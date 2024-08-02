package gift.Service;

import gift.DTO.KakaoUserDTO;
import gift.DTO.OrderRequestDTO;
import gift.DTO.OrderResponseDTO;
import gift.DTO.OptionDTO;
import gift.Entity.OptionEntity;
import gift.Entity.OrderEntity;
import gift.Entity.ProductEntity;
import gift.Entity.WishEntity;
import gift.Mapper.OptionServiceMapper;
import gift.Repository.OrderRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionService optionService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private OptionServiceMapper optionServiceMapper;

    @Autowired
    private KakaoMessageService kakaoMessageService;

    @Autowired
    private KakaoUserService kakaoUserService;



    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        ProductEntity product = productRepository.findById(orderRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        OptionEntity option = optionRepository.findById(orderRequestDTO.getOptionId()).orElseThrow(() -> new RuntimeException("Option not found"));

        OptionDTO optionDTO = optionServiceMapper.convertToDTO(option);

        // 옵션 수량 차감
        optionService.subtractQuantity(orderRequestDTO.getOptionId(), orderRequestDTO.getQuantity(), optionDTO);

        OrderEntity order = new OrderEntity(product, option, orderRequestDTO.getQuantity());
        order = orderRepository.save(order);

        // 위시리스트에서 해당 상품 삭제
        List<WishEntity> wishes = wishRepository.findByProductIdAndUserId(orderRequestDTO.getProductId(), orderRequestDTO.getUserId());
        for (WishEntity wish : wishes) {
            wishRepository.delete(wish);
        }

        // 사용자 정보 가져오기
        Long kakaoId = orderRequestDTO.getUserId();
        String accessToken = kakaoUserService.findByKakaoId(String.valueOf(kakaoId)).getAccessToken();

        // 카카오톡 메시지 보내기
        String message = "주문이 완료되었습니다. 상품: " + product.getName() + ", 옵션: " + option.getName() + ", 수량: " + orderRequestDTO.getQuantity();
        kakaoMessageService.sendMessage(accessToken, message);

        return new OrderResponseDTO(order.getId(), "주문 성공", "주문 성공");
    }
}
