package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.NotNull;

public class Wish {

    public static class getList extends PageParam {

    }

    public static class getWish {

        @NotNull
        private Long userId;
        @NotNull
        private Long productId;

        public getWish() {
        }

        public getWish(Long userId, Long productId) {
            this.userId = userId;
            this.productId = productId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }

    public static class createWish {

        @NotNull
        private Long productId;

        public createWish() {
        }

        public createWish(Long productId) {
            this.productId = productId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }

    public static class wishSimple {

        private Long id;
        private Long userId;
        private Long productId;

        public wishSimple(Long id, Long userId, Long productId) {
            this.id = id;
            this.userId = userId;
            this.productId = productId;
        }

        public wishSimple() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }

    public static class wishDetail {

        private Long id;
        private Long userId;
        private Long productId;
        private String name;
        private Integer price;
        private String imageUrl;

        public wishDetail(Long id, Long userId, Long productId, String name, Integer price,
            String imageUrl) {
            this.id = id;
            this.userId = userId;
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public wishDetail() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

}
