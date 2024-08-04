package gift.Service;

import gift.Model.MemberDto;
import gift.Model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.Model.OrderRequestDto;

import java.util.List;

@Service
public class OrderService {

    private final ProductService productService;
    private final MemberService memberService;

    @Autowired
    public OrderService(ProductService productService, MemberService memberService) {
        this.productService = productService;
        this.memberService = memberService;
    }

    public int calculateTotalPrice(List<OrderRequestDto> orderRequestDtoList) {
        return orderRequestDtoList.stream()
                .mapToInt(orderRequestDto -> {
                    ProductDto productDto = productService.getProductById(orderRequestDto.getProductId()).get();
                    int price = productDto.getPrice();
                    int quantity = orderRequestDto.getQuantity();
                    MemberDto memberDto = memberService.findByMemberId(orderRequestDto.getMemberId()).get();
                    memberDto.setPoint(memberDto.getPoint() + (price * quantity / 2)); //산 금액의 절반을 포인트로 설정!
                    memberService.updatePoint(memberDto);
                    return price * quantity;
                })
                .sum();
    }

}
