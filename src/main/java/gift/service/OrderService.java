package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderDTO;
import gift.model.entity.Member;
import gift.model.entity.Option;
import gift.model.entity.Product;
import gift.model.entity.Wish;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final KakaoApi kakaoApi;
    private final OptionService optionService;
    private final ProductService productService;
    private final KakaoAuthService kakaoAuthService;
    private final PointService pointService;
    private final WishRepository wishRepository;

    public OrderService(KakaoApi kakaoApi, OptionService optionService, ProductService productService, KakaoAuthService kakaoAuthService, PointService pointService, WishRepository wishRepository) {
        this.kakaoApi = kakaoApi;
        this.optionService = optionService;
        this.productService = productService;
        this.kakaoAuthService = kakaoAuthService;
        this.pointService = pointService;
        this.wishRepository = wishRepository;
    }

    public void orderProduct(String token, OrderDTO orderDTO) throws JsonProcessingException {
        int quantity = orderDTO.getQuantity();
        Option option = optionService.getOption(orderDTO.getOptionId());
        Product product = productService.getProductById(option.getProductID());
        //옵션 수량 만큼 감소(remove) num
        optionService.removeOption(option, quantity);
        //멤버 ID찾음 token
        Member member = kakaoAuthService.getDBMemberByToken(token);
        //위시리스트에 상품있으면 삭제 멤버iD, productID
        Optional<Wish> OptionalWish = wishRepository.findByMember_IdAndProduct_Id(member.getId(), option.getProductID());
        OptionalWish.ifPresent(wish ->
                wishRepository.deleteById(wish.getId()));

        //포인트 사용시 가격의 10% 포인트 감소
        if (orderDTO.getPoint()) {
            pointService.usePoint(member, product);
        }

        //카카오톡 메세지 보내기
        kakaoApi.sendKakaoMessage(token, orderDTO.getMessage());
    }
}
