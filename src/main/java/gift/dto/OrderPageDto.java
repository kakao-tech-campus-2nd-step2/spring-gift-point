package gift.dto;

import java.util.List;

public class OrderPageDto {

    private PageInfoDto pageInfo;
    private List<OrderDto> orderDtoList;

    public OrderPageDto() {
    }

    public OrderPageDto(PageInfoDto pageInfo, List<OrderDto> orderDtoList) {
        this.pageInfo = pageInfo;
        this.orderDtoList = orderDtoList;
    }

    public PageInfoDto getPageInfo() {
        return pageInfo;
    }

    public List<OrderDto> getOrderDtoList() {
        return orderDtoList;
    }
}
