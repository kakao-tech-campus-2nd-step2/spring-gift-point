package gift.controller;

import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.dto.point.PointRequest;
import gift.dto.point.PointResponse;
import gift.service.OrderService;
import gift.service.PointService;
import gift.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management System", description = "Operations related to order management")
public class OrderController {
    private final OrderService orderService;
    private final PointService pointService;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService, PointService pointService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.pointService = pointService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @Operation(summary = "Get all orders for the logged-in user", description = "Fetches all orders made by the logged-in user", tags = {"Order Management System"})
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        logger.info("Received request to fetch orders");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring("Bearer ".length());
        if (!jwtUtil.validateToken(token) || jwtUtil.isTokenExpired(token)) {
            logger.warn("Invalid or expired token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.extractEmail(token);
        if (email == null) {
            logger.warn("Failed to extract email from token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<OrderResponse> orderResponses = orderService.getOrdersByEmail(email, pageable);

        return ResponseEntity.ok(orderResponses);
    }

    @PostMapping
    @Operation(summary = "Place an order", description = "Places a new order in the system", tags = {"Order Management System"})
    public ResponseEntity<OrderResponse> placeOrder(
            @Parameter(description = "Order details", required = true) @RequestBody OrderRequest orderRequest,
            @Parameter(description = "Authorization token", required = true) @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Received request to place order");
        logger.info("Server current time: {}", Instant.now().getEpochSecond());

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.warn("Authorization header is missing or invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new OrderResponse("Authorization header is missing or invalid"));
        }

        String token = authorizationHeader.substring("Bearer ".length());
        logger.info("Token: {}", token);

        try {
            if (!jwtUtil.validateToken(token)) {
                logger.warn("Token validation failed");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new OrderResponse("Token validation failed"));
            }

            if (jwtUtil.isTokenExpired(token)) {
                logger.warn("Token is expired");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new OrderResponse("Token is expired"));
            }

            String email = jwtUtil.extractEmail(token);
            logger.info("Email extracted from token: {}", email);

            if (email == null) {
                logger.warn("Failed to extract email from token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new OrderResponse("Failed to extract email from token"));
            }

            // Check if points should be used
            if (orderRequest.isUsePoints()) {
                PointRequest pointRequest = new PointRequest(orderRequest.getProductId(), orderRequest.getOptionId(), orderRequest.getQuantity(), email);
                PointResponse pointResponse = pointService.usePoints(pointRequest);
                if (pointResponse.getMessage() != null && pointResponse.getMessage().contains("Insufficient points")) {
                    return ResponseEntity.badRequest().body(new OrderResponse("Insufficient points to complete the order"));
                }
            }

            OrderRequest newOrderRequest = new OrderRequest(
                    orderRequest.getProductId(),
                    orderRequest.getOptionId(),
                    orderRequest.getQuantity(),
                    orderRequest.isUsePoints(),
                    orderRequest.getMessage(),
                    LocalDateTime.now(),
                    email
            );

            OrderResponse orderResponse = orderService.placeOrder(newOrderRequest);
            logger.info("Order placed successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);

        } catch (Exception e) {
            logger.error("Error processing order placement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderResponse("Internal server error"));
        }
    }
}
