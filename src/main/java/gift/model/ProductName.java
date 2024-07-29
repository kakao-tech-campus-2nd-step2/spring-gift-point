package gift.model;

public class ProductName {
    private final String name;

    public ProductName(String name){
        this.name = name;

        if (!isCorrectName(this.name)) {
            throw new IllegalArgumentException("이름은 최대 15자 이내이어야 하며, 특수문자로는 (),[],+,-,&,/,_만 사용 가능합니다.");
        }
        if (isContainsKakao(this.name)) {
            throw new IllegalArgumentException("\"카카오\"는 MD와 협의 후에 사용 가능합니다.");
        }
    }

    public String getName() {
        return name;
    }

    private boolean isCorrectName(String name){
        if(name.length()>15){
            return false;
        }
        String letters = "()[]+-&/_ ";
        for(int i=0; i<name.length(); i++){
            char one = name.charAt(i);
            if(!Character.isLetterOrDigit(one) && !letters.contains(Character.toString(one))){
                return false;
            }
        }
        return true;
    }

    private boolean isContainsKakao(String name){
        return name.contains("카카오");
    }
}
