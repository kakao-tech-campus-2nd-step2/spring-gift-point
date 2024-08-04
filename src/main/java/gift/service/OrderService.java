package gift.service;

import gift.dto.OrderRequest;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final RestClient restClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, WishlistRepository wishlistRepository, MemberRepository memberRepository, RestClient restClient) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.restClient = restClient;
    }

    @Transactional
    public void createOrder(OrderRequest orderRequest, Long memberId) {
        Option option = optionRepository.findById(orderRequest.optionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        int totalPrice = option.getQuantity() * orderRequest.quantity();
        if (orderRequest.usingPoint() > 0) {
            if (member.getPoints() < orderRequest.usingPoint()) {
                throw new IllegalArgumentException("Insufficient points");
            }
            totalPrice -= orderRequest.usingPoint();
            member.deductPoints(orderRequest.usingPoint());
        }

        if (option.getQuantity() < orderRequest.quantity()) {
            throw new IllegalArgumentException("Insufficient quantity");
        }
        option.setQuantity(option.getQuantity() - orderRequest.quantity());
        optionRepository.save(option);

        wishlistRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId()).ifPresent(wishlistRepository::delete);

        Order order = new Order();
        order.setOption(option);
        order.setQuantity(orderRequest.quantity());
        order.setOrderDateTime(LocalDateTime.now());
        order.setMessage(orderRequest.message());
        orderRepository.save(order);

        int earnedPoints = (int) Math.floor(totalPrice * 0.005);
        member.addPoints(earnedPoints);

        memberRepository.save(member);

        sendOrderMessage(order, member.getKakaoToken());
    }

    private void sendOrderMessage(Order order, String kakaoToken) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        String messageTemplate = "{\n" +
                "  \"object_type\": \"text\",\n" +
                "  \"text\": \"Order Details:\\nOption: " + order.getOption().getName() + "\\nQuantity: " + order.getQuantity() + "\\nMessage: " + order.getMessage() + "\",\n" +
                "  \"link\": {\n" +
                "    \"web_url\": \"http://localhost:8080/" + order.getId() + "\",\n" +
                "    \"mobile_web_url\": \"http://localhost:8080/" + order.getId() + "\"\n" +
                "  },\n" +
                "  \"button_title\": \"View Order\"\n" +
                "}";

        try {
            restClient
                    .post()
                    .uri(url)
                    .header("Authorization", "Bearer " + kakaoToken)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body("template_object=" + messageTemplate)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send Kakao message", e);
        }
    }
}
