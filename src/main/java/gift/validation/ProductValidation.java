package gift.validation;

import gift.dto.CreateProductDto;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProductValidation {
    private static final String ALLOWED_SPECIAL_CHARACTERS = "()[]+\\-&/_.";

    public void validateProductDto(CreateProductDto productDto) {
        if (productDto.getName() == null || productDto.getPrice() == null || productDto.getImageUrl() == null) {
            throw new IllegalArgumentException("상품의 이름, 가격, 설명을 모두 입력해야합니다.");
        }
    }

    public void validateProductExists(Long productId, ProductRepository productRepository) {
        if (productRepository.findById(productId) == null) {
            throw new IllegalArgumentException("일치하는 상품이 없습니다.");
        }
    }

    public void validateProductName(String name) {
        try {
            if (name.length() > 15) {
                throw new IllegalArgumentException("상품 이름은 최대 15자까지 입력할 수 있습니다.");
            }

            if (name.contains("카카오")) {
                // 여기에서 담당 MD와의 협의 로직 생각하기
                throw new IllegalArgumentException("\"카카오\"가 포함된 상품 이름은 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
            }

            Pattern pattern = Pattern.compile("[^" + ALLOWED_SPECIAL_CHARACTERS + "a-zA-Z0-9]");
            Matcher matcher = pattern.matcher(name);
            if (matcher.find()) {
                throw new IllegalArgumentException("상품 이름에는 허용된 특수 문자 (" + ALLOWED_SPECIAL_CHARACTERS + ") 만 사용할 수 있습니다.");
            }
        } catch (IllegalArgumentException e) {
            // 각 검증 단계에서 발생한 에러에 대한 정보를 클라이언트에게 제공
            System.out.println(e.getMessage());
        }
    }
}
