package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.dto.KakaoMessageDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final KakaoService kakaoService;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, KakaoService kakaoService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.kakaoService = kakaoService;
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order(orderRequestDto.optionId(), orderRequestDto.quantity(), orderRequestDto.message());
        Order savedOrder = orderRepository.save(order);
        return OrderResponseDto.convertToDto(savedOrder);
    }

    public void sendOrderMessage(OrderRequestDto orderRequestDto, Member member) {
        try {
            kakaoService.sendKakaoMessageToMe(
                member.getAccessToken(),
                new KakaoMessageDto(
                    "text",
                    textDetail(orderRequestDto, member.getEmail()),
                    "https://gift.kakao.com",
                    "https://gift.kakao.com"
                )
            );
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String textDetail(OrderRequestDto orderRequestDto, String email) {
        Product product = productRepository.findById(orderRequestDto.productId())
            .orElseThrow(() -> new NoSuchElementException("해당 id의 상품 없음: " + orderRequestDto.productId()));
        Option option = product.getOptionById(orderRequestDto.optionId());

        String text = "계정 이메일: " + email
            + "\n주문한 상품명: " + product.getName()
            + "\n주문한 옵션명: " + option.getName()
            + "\n주문 수량: " + orderRequestDto.quantity()
            + "\n요청 사항: " + orderRequestDto.message();

        return text;
    }
}
