package gift.dto;

public class WishResponse {
    private Long id;
    private String productName;
    private int productPrice;
    private String productImageurl;
    private String productCategory;
    private String optionName;

    public WishResponse() {
    }

    public WishResponse(Long id, String productName, int productPrice, String productImageurl, String productCategory, String optionName) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageurl = productImageurl;
        this.productCategory = productCategory;
        this.optionName = optionName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageurl() {
        return productImageurl;
    }

    public void setProductImageurl(String productImageurl) {
        this.productImageurl = productImageurl;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
}