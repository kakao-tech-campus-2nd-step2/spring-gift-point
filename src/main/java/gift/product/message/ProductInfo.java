package gift.product.message;

public final class ProductInfo {
    public static final String PRODUCT_UPDATE_SUCCESS = "상품 수정 성공";
    public static final String PRODUCT_DELETE_SUCCESS = "상품 삭제 성공";
    public static final String PRODUCT_NAME_REQUIRED = "상품 이름은 필수 입력 값입니다.";
    public static final String PRODUCT_NAME_SIZE = "상품 이름은 1자 이상 15자 이하로 입력해주세요.";
    public static final String PRODUCT_NAME_PATTERN = "상품 이름은 한글, 영문, 숫자, 공백, 특수문자 (), [], +, -, &, /, _ 만 입력 가능합니다.";
    public static final String PRODUCT_NAME_KAKAO = "상품 이름에 '카카오' 문구가 포함되어 있습니다. 담당 MD와 협의 후 사용해주세요.";
    public static final String PRODUCT_PRICE_REQUIRED = "상품 가격은 필수 입력 값입니다.";
    public static final String PRODUCT_IMAGE_URL_REQUIRED = "상품 이미지 URL은 필수 입력 값입니다.";
    public static final String PRODUCT_CATEGORY_REQUIRED = "상품 카테고리는 필수 입력 값입니다.";

    private ProductInfo() {
    }
}
