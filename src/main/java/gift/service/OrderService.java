package gift.service;

import gift.domain.OrderDTO;
import gift.entity.Order;
import gift.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.util.NoSuchElementException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final OptionService optionService;
    private final KakaoLoginService kakaoLoginService;
    private final OptionsRepository optionsRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, WishlistRepository wishlistRepository, MemberRepository memberRepository, OptionService optionService, KakaoLoginService kakaoLoginService, OptionsRepository optionsRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.optionService = optionService;
        this.kakaoLoginService = kakaoLoginService;
        this.optionsRepository = optionsRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order addOrder(String token, OrderDTO orderDTO) {
        var option = optionRepository.findById(orderDTO.optionId()).orElseThrow(NoSuchElementException::new);
        var timeStamp = new Timestamp(System.currentTimeMillis());
        var order = orderRepository.save(new Order(option, orderDTO.quantity(), timeStamp.toString(), orderDTO.message()));
        optionService.deductQuantity(orderDTO.optionId(), orderDTO.quantity());
        deleteWishlist(token, orderDTO.optionId());
        kakaoLoginService.sendMessage(token, orderDTO.message());
        memberRepository.updatePointByToken(getPoint(orderDTO), token);
        return order;
    }

    public void deleteWishlist(String token, int optionId) {
        var memberId = memberRepository.searchIdByToken(token);
        var productId = optionService.findProductIdByOptionId(optionId);
        wishlistRepository.deleteByMember_idAndProduct_id(memberId, productId);
    }

    public Order findOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public int getPoint(OrderDTO orderDTO) {
        int pointBound = 50000;

        var option = optionRepository.findById(orderDTO.optionId()).orElseThrow(NoSuchElementException::new);
        var productId = optionsRepository.findProductIdByOptionListContaining(option).orElseThrow(NoSuchElementException::new);
        var product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        var totalPrice = product.getPrice() * orderDTO.quantity();

        if(totalPrice > pointBound) {
            return (int) (totalPrice / 10);
        }
        return 0;
    }
}
