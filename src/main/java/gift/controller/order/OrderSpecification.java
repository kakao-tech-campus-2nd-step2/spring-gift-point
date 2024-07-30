package gift.controller.order;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "주문 관리", description = "주문을 위한 API")
@SecurityRequirement(name = "bearerAuth")
public interface OrderSpecification {

    @Operation(summary = "상품 주문", description = "주어진 ID의 상품을 주문합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderResponse.class)
                            )
                    )
            })
    ResponseEntity<OrderResponse.Info> order(@Parameter(hidden = true) @RequestAttribute("user") User user,
                                        @Valid @RequestBody OrderRequest.Create orderRequest);
}
