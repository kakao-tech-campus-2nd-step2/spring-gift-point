package gift.Service;

import gift.Entity.Product;
import gift.Repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.Model.OrderRequestDto;

import java.util.List;

@Service
public class OrderService {

    private final ProductJpaRepository productJpaRepository;

    @Autowired
    public OrderService(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    public int calculateTotalPrice(List<OrderRequestDto> orderRequestDtoList) {
        return orderRequestDtoList.stream()
                .mapToInt(orderRequestDto -> {
                    Product product = productJpaRepository.findById(orderRequestDto.getProductId()).orElseThrow();
                    int price = product.getPrice();
                    int quantity = orderRequestDto.getQuantity();

                    return price * quantity;
                })
                .sum();
    }

}
