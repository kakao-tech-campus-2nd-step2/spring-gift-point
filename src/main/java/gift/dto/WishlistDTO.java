package gift.dto;

import gift.model.Wishlist;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "위시리스트 DTO")
public class WishlistDTO {

    @Schema(description = "위시리스트 ID", example = "1")
    private Long id;

    @Schema(description = "상품 ID", example = "2001")
    private Long productId;

    @Schema(description = "사용자 이름", example = "user123")
    private String username;

    @Schema(description = "수량", example = "2")
    private int quantity;

    @Schema(description = "상품 이름", example = "스타벅스 기프트 카드")
    private String productName;

    @Schema(description = "가격", example = "10000")
    private int price;

    @Schema(description = "이미지 URL", example = "https://image.istarbucks.co.kr/cardImg/20200818/007633_WEB.png")
    private String imageUrl;

    @Schema(description = "옵션 목록")
    private List<OptionDTO> options;

    @Schema(description = "총 가격", example = "20000")
    private int totalPrice;

    public WishlistDTO() {}

    public WishlistDTO(Long id, Long productId, String username, int quantity, String productName, int price, String imageUrl, List<OptionDTO> options) {
        this.id = id;
        this.productId = productId;
        this.username = username;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.options = options;
        this.totalPrice = calculateTotalPrice();
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getUsername() {
        return username;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<OptionDTO> getOptions() {
        return options;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setOptions(List<OptionDTO> options) {
        this.options = options;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    private int calculateTotalPrice() {
        int optionsTotalPrice = options.stream()
            .mapToInt(option -> option.getPrice() * option.getQuantity())
            .sum();
        return optionsTotalPrice;
    }

    public static WishlistDTO convertToDTO(Wishlist wishlist) {
        List<OptionDTO> optionDTOs = wishlist.getOptions().stream()
            .map(OptionDTO::convertToDTO)
            .collect(Collectors.toList());

        return new WishlistDTO(
            wishlist.getId(),
            wishlist.getProduct().getId(),
            wishlist.getUser().getUsername(),
            wishlist.getQuantity(),
            wishlist.getProduct().getName(),
            wishlist.getPrice(),
            wishlist.getProduct().getImageUrl(),
            optionDTOs
        );
    }

    public static class OptionDTO {

        @Schema(description = "옵션 ID", example = "1")
        private Long id;

        @Schema(description = "옵션 이름", example = "포장 선택")
        private String name;

        @Schema(description = "옵션 수량", example = "1")
        private int quantity;

        @Schema(description = "옵션 가격", example = "3000")
        private int price;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getPrice() {
            return price;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public static OptionDTO convertToDTO(gift.model.Option option) {
            OptionDTO optionDTO = new OptionDTO();
            optionDTO.setId(option.getId());
            optionDTO.setName(option.getName());
            optionDTO.setQuantity(option.getQuantity());
            optionDTO.setPrice(option.getPrice());
            return optionDTO;
        }
    }
}
