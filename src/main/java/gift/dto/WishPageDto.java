package gift.dto;

import java.util.List;

public class WishPageDto {

    private PageInfoDto pageInfo;
    private List<WishDto> wishDtoList;

    public WishPageDto() {
    }

    public WishPageDto(PageInfoDto pageInfo, List<WishDto> wishDtoList) {
        this.pageInfo = pageInfo;
        this.wishDtoList = wishDtoList;
    }

    public PageInfoDto getPageInfo() {
        return pageInfo;
    }

    public List<WishDto> getWishDtoList() {
        return wishDtoList;
    }
}
