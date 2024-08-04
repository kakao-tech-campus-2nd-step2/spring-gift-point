package gift.order.controller;

import gift.member.presentation.request.ResolvedMember;
import gift.order.controller.request.OrderCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "주문 API")
@RequestMapping("/api/orders")
public interface OrderApi {

    @Operation(summary = "주문 생성")
    @PostMapping
    ResponseEntity<?> order(
            @Parameter(description = "주문 생성 요청 정보", required = true)
            @RequestBody OrderCreateRequest request,
            @Parameter(hidden = true) ResolvedMember resolvedMember
    );
}
