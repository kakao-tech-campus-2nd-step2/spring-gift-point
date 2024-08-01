package gift.service;

import gift.dto.betweenClient.order.OrderRequestDTO;
import gift.dto.betweenClient.order.OrderResponseDTO;
import gift.entity.Option;
import gift.entity.OrderHistory;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.InternalServerExceptions.InternalServerException;
import gift.repository.OptionRepository;
import gift.repository.OrderHistoryRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderService(OptionRepository optionRepository,
            OrderHistoryRepository orderHistoryRepository) {
        this.optionRepository = optionRepository;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Transactional
    public OrderResponseDTO saveOrderHistory(OrderRequestDTO orderRequestDTO) {
        try {
            Option option = optionRepository.findById(orderRequestDTO.optionId())
                    .orElseThrow(() -> new BadRequestException("해당 옵션 Id를 찾지 못했습니다."));
            OrderHistory orderHistory = orderRequestDTO.convertToOrder(option, LocalDateTime.now());

            return OrderResponseDTO.convertToDTO(orderHistoryRepository.save(orderHistory));
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
