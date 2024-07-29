package gift.dto;

import gift.entity.Category;
import gift.entity.Option;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

public class ProductRequestDto {

    private Long id;
    private String name;
    private Integer price;
    private String url;
    private Category category;

    private List<Option> options;

    public ProductRequestDto(String name, Integer price, String url, Category category) {
        if (checkValidProductName(name)) {
            this.name = name;
            this.price = price;
            this.url = url;
            this.category = category;
        }
    }

    public ProductRequestDto(Long id, String name, Integer price, String url, Category category) {
        if (checkValidProductName(name)) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.url = url;
            this.category = category;
        }
    }

    public boolean checkValidProductName(String name) {
        boolean result = true;
        if (!(checkValidLength(name, 1, 15))) {
            result = false;
            throw new IllegalProductNameLengthException();
        }
        if (!(checkValidSpecialCharacter(name))) {
            result = false;
            throw new IllegalProductNameCharacterException();
        }
        if (!(checkNotContainKeyword(name, "카카오"))) {
            result = false;
            throw new IllegalProductNameKeywordException();
        }
        return result;
    }

    private boolean checkValidLength(String name, int start, int end) {
        return (name.length() >= 1 && name.length() <= end);
    }

    private boolean checkValidSpecialCharacter(String name) {
        boolean result = true;

        char[] nameWords = name.toCharArray();
        for (char word : nameWords) {
            if (checkSpecialCharacter(word) && (!checkAllowedSpecialCharacter(word))) {
                result = false;
            }
        }
        return result;
    }

    private boolean checkAllowedSpecialCharacter(char word) {
        char[] allowedSpecialCharacters = {'(', ')', '[', ']', '+', '-', '&', '/', '_'};
        for (char allowedSpecialCharacter : allowedSpecialCharacters) {
            if (word == allowedSpecialCharacter) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSpecialCharacter(char word) {
        boolean result = (word < 'a' || word > 'z') &&
                (word < 'A' || word > 'Z') &&
                (word < '0' || word > '9') &&
                (word < '가' || word > '힣') &&
                (word != ' ');
        return result;
    }

    private boolean checkNotContainKeyword(String name, String keyword) {
        return !(name.contains(keyword));
    }

    @ExceptionHandler({IllegalProductNameLengthException.class, IllegalProductNameCharacterException.class, IllegalProductNameKeywordException.class})
    public String handleProductNameExceptions(RuntimeException e) {
        return e.getMessage();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public Category getCategoryId() {
        return category;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public class IllegalProductNameLengthException extends IllegalArgumentException {
        public IllegalProductNameLengthException() {
            super("상품 이름은 1 ~ 15 글자로 입력해주세요.");
        }
    }

    public class IllegalProductNameCharacterException extends IllegalArgumentException {
        public IllegalProductNameCharacterException() {
            super("상품 이름에서 특수문자는 ()[] + - & / _ 만 사용 가능합니다.");
        }
    }

    public class IllegalProductNameKeywordException extends IllegalArgumentException {
        public IllegalProductNameKeywordException() {
            super("상품이름에 \"카카오\" 키워드를 포함시키고 싶으신 경우, 담당MD와의 협의가 필요합니다. 고객센터로 문의주세요.");
        }
    }
}
