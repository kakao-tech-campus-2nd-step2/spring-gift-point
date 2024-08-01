package gift.domain.Menu;

import jakarta.persistence.Embeddable;

@Embeddable
public class MenuName {
    String MenuName;

    public MenuName(String menuName){
        validMenuName(menuName);
        this.MenuName = menuName;
    }

    public MenuName() {

    }

    public String getMenuName() {
        return MenuName;
    }

    public void validMenuName(String menuName){
        String PATTERN = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$";

        if(menuName.length() > 15){
            throw new IllegalArgumentException("메뉴는 15자 이내로 작성해주세요");
        }
        if(!menuName.matches(PATTERN)){
            throw new IllegalArgumentException(" ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다.");
        }
        if(menuName.contains("카카오")){
            throw new IllegalArgumentException("'카카오'가 들어간 상품명은 담당 MD와 상의해주세요");
        }
    }
}
