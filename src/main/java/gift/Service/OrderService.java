package gift.Service;

import gift.Entity.Member;
import gift.Entity.Product;
import gift.Repository.MemberJpaRepository;
import gift.Repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.Model.OrderRequestDto;

import java.util.List;

@Service
public class OrderService {

    private final ProductJpaRepository productJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Autowired
    public OrderService(ProductJpaRepository productJpaRepository, MemberJpaRepository memberJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    public int calculateTotalPrice(List<OrderRequestDto> orderRequestDtoList) {
        return orderRequestDtoList.stream()
                .mapToInt(orderRequestDto -> {
                    Product product = productJpaRepository.findById(orderRequestDto.getProductId()).orElseThrow();
                    int price = product.getPrice();
                    int quantity = orderRequestDto.getQuantity();
                    Member member = memberJpaRepository.findById(orderRequestDto.getMemberId()).orElseThrow();
                    member.setPoint(member.getPoint() + (price * quantity / 2)); //산 금액의 절반을 포인트로 설정!
                    memberJpaRepository.save(member);
                    return price * quantity;
                })
                .sum();
    }

}
