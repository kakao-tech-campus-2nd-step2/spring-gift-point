package gift.dto;

import java.util.List;

public class ProductPageDto {

    private PageInfoDto pageInfo;
    private List<ProductDto> productDtoList;

    public ProductPageDto() {
    }

    public ProductPageDto(PageInfoDto pageInfo, List<ProductDto> productDtoList) {
        this.pageInfo = pageInfo;
        this.productDtoList = productDtoList;
    }

    public PageInfoDto getPageInfo() {
        return pageInfo;
    }

    public List<ProductDto> getProductDtoList() {
        return productDtoList;
    }
}
