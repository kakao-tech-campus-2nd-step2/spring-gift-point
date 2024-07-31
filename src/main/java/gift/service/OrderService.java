package gift.service;

import gift.auth.KakaoClient;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.User;
import gift.domain.Wish.WishList;
import gift.dto.common.PageInfo;
import gift.dto.requestdto.OrderRequestDTO;
import gift.dto.responsedto.OrderPageResponseDTO;
import gift.dto.responsedto.OrderResponseDTO;
import gift.dto.responsedto.ProductPageResponseDTO;
import gift.dto.responsedto.ProductResponseDTO;
import gift.repository.JpaOrderRepository;
import gift.repository.JpaWishRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {
    private final JpaOrderRepository jpaOrderRepository;
    private final JpaWishRepository jpaWishRepository;
    private final KakaoClient kakaoClient;

    public OrderService(JpaOrderRepository jpaOrderRepository, JpaWishRepository jpaWishRepository,
        KakaoClient kakaoClient) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.jpaWishRepository = jpaWishRepository;
        this.kakaoClient = kakaoClient;
    }

    public OrderResponseDTO order(OrderRequestDTO orderRequestDTO, User user, Option option){
        option.subtract(orderRequestDTO.quantity());

        WishList wishList = new WishList(jpaWishRepository.findAllByUserId(user.getId()));
        wishList.checkWishList(option.getProduct())
            .ifPresent(jpaWishRepository::delete);

        Order order = orderRequestDTO.toEntity(user, option);
        jpaOrderRepository.save(order);

        kakaoClient.sendMessage(user.getAccessToken(), orderRequestDTO.message());
        return OrderResponseDTO.from(order);
    }

    public OrderPageResponseDTO getAllOrders(Long userId, int page, int size, String criteria) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(criteria));

        Page<Order> orderPage = jpaOrderRepository.findAllByUserId(userId, pageable);

        List<OrderResponseDTO> orderResponseDTOList = orderPage
            .stream()
            .map(OrderResponseDTO::from)
            .toList();

        PageInfo pageInfo = new PageInfo(page, orderPage.getTotalElements(), orderPage.getTotalPages());

        return new OrderPageResponseDTO(pageInfo, orderResponseDTOList);

    }
}
