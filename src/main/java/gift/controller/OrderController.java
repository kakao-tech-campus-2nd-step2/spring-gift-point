package gift.controller;

import gift.dto.order.OrderPageDTO;
import gift.dto.order.OrderResponseDTO;
import gift.dto.order.OrderRequestDTO;
import gift.service.OrderService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/orders")
    public OrderResponseDTO order(@RequestHeader("Authorization") String token, @RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.order(orderRequestDTO, token);
    }

    @GetMapping("/api/orders")
    public OrderPageDTO getOrders(@RequestHeader("Authorization") String token,
                                  @RequestParam(value = "page", defaultValue = "0") int pageNum,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  @RequestParam(value = "sort", defaultValue = "orderDateTime,desc") String sortString) {
        List<String> sortStringList = Arrays.stream(sortString.split(",")).toList();
        String sortProperty = sortStringList.getFirst(), sortDirection = sortStringList.get(1);
        Pageable pageable;
        if (sortDirection.equals("asc"))
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.ASC, sortProperty));
        else
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, sortProperty));
        return orderService.getOrders(token, pageable);
    }

}
