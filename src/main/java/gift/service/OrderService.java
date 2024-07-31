package gift.service;

import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Orders;

import gift.entity.Wish;
import gift.repository.OrderRepository;
import gift.util.KakaoApiUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    OrderRepository orderRepository;
    MemberService memberService;
    OptionService optionService;
    WishlistService wishlistService;
    SnsMemberService snsMemberService;
    KakaoApiUtil kakaoApiUtil;

    public OrderService(OrderRepository orderRepository, MemberService memberService,
        OptionService optionService, WishlistService wishlistService,
        SnsMemberService snsMemberService,
        KakaoApiUtil kakaoApiUtil) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.snsMemberService = snsMemberService;
        this.kakaoApiUtil = kakaoApiUtil;
    }

    public Member findMemberByEmail(String email) {
        return memberService.findByEmail(email);
    }

    public Option findOptionById(Long optionId) {
        return optionService.getOptionById(optionId);
    }

    public Page<Orders> getProductPage(String email, Pageable pageable) {
        Member member = findMemberByEmail(email);
        return orderRepository.findAllByMember(pageable, member);
    }


    public OrderResponseDTO orderOption(OrderRequestDTO orderRequestDTO, String email) {
        Option option = optionService.getOptionById(orderRequestDTO.getOptionId());
        Member member = memberService.findByEmail(email);
        int quantity = orderRequestDTO.getQuantity();
        String message = orderRequestDTO.getMessage();

        Orders order = new Orders(option, quantity, message, member, LocalDateTime.now());
        //상품이 wishlist에 있을 시 위시리스트에서 삭제
        List<Wish> list = wishlistService.getWishlistByEmail(email);
        for (Wish wish : list) {
            if (wish.getProduct().equals(option.getProduct())) {
                wishlistService.deleteWishlist(wish.getProduct().getId(), email);
            }
        }
        //옵션 수량 감소
        optionService.subtractOption(option.getId(), quantity);

        //카카오톡 메시지 보내기
        sendMessage(message, snsMemberService.getOauthAccessTokenByEmail(email), option, quantity);

        //주문목록 추가
        orderRepository.save(order);
        return new OrderResponseDTO(option.getId(), quantity, LocalDateTime.now(), message);
    }

    private void sendMessage(String text, String accessToken, Option option, int quantity) {
        String sb = text + "\n["
            + option.getProduct().getName() + "] 상품을 선물하셨습니다.\n"
            + "상품 옵션: [" + option.getName() + "]\n"
            + "상품 수량: " + quantity;
        kakaoApiUtil.SendOrderMessage(sb, accessToken);
    }

}
