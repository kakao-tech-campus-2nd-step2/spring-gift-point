package gift.service;

import gift.config.KakaoUserClinet;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.dto.OrderResponseDtoBuilder;
import gift.exception.ValueNotFoundException;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.wish.Wish;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final KakaoUserClinet kakaoUserClinet;

    @Autowired
    public OrderService(KakaoUserClinet kakaoUserClinet,OptionRepository optionRepository,WishRepository wishRepository) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoUserClinet = kakaoUserClinet;
    }


    public OrderResponseDto requestOrder(OrderRequestDto orderRequestDto, String accessToken) {
        Option option = optionRepository.findById(orderRequestDto.optionId()).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));
        if (option.isProductNotEnough(orderRequestDto.quantity())) {
            throw new RuntimeException("Not enough product available");
        }
        option.updateAmount(orderRequestDto.quantity());
        optionRepository.save(option);

        Product product = option.getProduct();
        Optional<Wish> wish = wishRepository.findByProductId(product.getId());
        if (wish.isPresent()) {
            wishRepository.delete(wish.get());
        }

        int resultCode = kakaoUserClinet.sendOrderConfirmationMessage(orderRequestDto.message(), accessToken).block();
        if (resultCode != 0) {
            throw new RuntimeException("Kakao Server Error");
        }

        OrderResponseDto responseDto = new OrderResponseDtoBuilder()
                .id(1)
                .optionId(orderRequestDto.optionId())
                .quantity(orderRequestDto.quantity())
                .orderDateTime(LocalDateTime.now())
                .message(orderRequestDto.message())
                .build();

        return responseDto;
    }
}
