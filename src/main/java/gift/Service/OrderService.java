package gift.Service;

import gift.Exception.Login.AuthorizedException;
import gift.Exception.ProductNotFoundException;
import gift.Model.Entity.MemberEntity;
import gift.Model.Entity.OptionEntity;
import gift.Model.Entity.OrderEntity;
import gift.Model.Entity.ProductEntity;
import gift.Model.Role;
import gift.Model.request.OrderRequest;
import gift.Model.response.OrderResponse;
import gift.Repository.MemberRepository;
import gift.Repository.OptionRepository;
import gift.Repository.OrderRepository;
import gift.Repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    public OrderService(MemberRepository memberRepository, OptionRepository optionRepository, OrderRepository orderRepository, ProductRepository productRepository, RestTemplate restTemplate){
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    public OrderResponse order(String email, OrderRequest orderRequest){
        Optional<OptionEntity> optionEntityOptional = optionRepository.findById(orderRequest.optionId());
        // 옵션 수량 차감
        if(optionEntityOptional.isEmpty()){
            throw new IllegalArgumentException("해당 옵션이 없습니다.");
        }
        MemberEntity memberEntity = memberRepository.findByEmail(email).get();

        OptionEntity optionEntity = optionEntityOptional.get();
        optionEntity.subtract(orderRequest.quantity());
        optionRepository.save(optionEntity);

        // 포인트 사용 및 적립
        Optional<ProductEntity> productEntityOptional = productRepository.findById(orderRequest.productId());

        if(productEntityOptional.isEmpty()){
            throw new ProductNotFoundException("제품을 찾을 수 없습니다.");
        }

        ProductEntity productEntity = productEntityOptional.get();

        if(productEntity.getPrice() >= memberEntity.getPoint()){
            int priceAfterPoint = productEntity.getPrice() - memberEntity.getPoint();
            memberEntity.setPoint(0);
            memberEntity.setPoint(priceAfterPoint / 10);
        }
        else{
            memberEntity.setPoint(memberEntity.getPoint() - productEntity.getPrice());
        }

        memberRepository.save(memberEntity);

        // 주문 내역 저장
        OrderEntity orderEntity = new OrderEntity(optionEntity, orderRequest.quantity(), LocalDateTime.now(), orderRequest.message());
        orderRepository.save(orderEntity);

        return new OrderResponse(orderEntity.getId(), orderRequest.optionId(), orderEntity.getQuantity(), orderEntity.getOrderDateTime(), orderEntity.getMessage());
    }

    public List<OrderResponse> read(String email){
        Optional<MemberEntity> memberOptional = memberRepository.findByEmail(email);

        if(memberOptional.isEmpty()) {
            throw new AuthorizedException("회원정보가 없습니다.");
        }
        MemberEntity memberEntity = memberOptional.get();

        if(!memberEntity.getRole().equals(Role.ADMIN) && !memberEntity.getRole().equals(Role.CONSUMER)) {
            throw new AuthorizedException("접근 권한이 없습니다.");
        }
        List<OrderEntity> orderEntities = orderRepository.findAll();
        List<OrderResponse> orderDTOs = new ArrayList<>();

        for(OrderEntity o : orderEntities){
            orderDTOs.add(new OrderResponse(o.getId(), o.getOptionEntity().getId(), o.getQuantity(), o.getOrderDateTime(), o.getMessage()));
        }

        return orderDTOs;
    }

    public Page<OrderResponse> getPage(String email, int page, int size, String sort){
        List<OrderResponse> dtoList = read(email);
        Sort sortType = Sort.by(Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, size, sortType);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());

        List<OrderResponse> subList = dtoList.subList(start, end);

        return new PageImpl<>(subList, pageable, dtoList.size());
    }

    public void sendKakaoTalkMessage(String accessToken, OrderResponse orderResponse){
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"object_type\":\"text\",\"text\":\"")
                .append(orderResponse)
                .append("\",\"link\":{\"web_url\":\"\"}}");

        body.add("template_object",sb.toString());

        RequestEntity request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity response = new RestTemplate().exchange(request, Map.class);
    }
}
