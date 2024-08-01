package gift.model.valueObject;

public class OptionName {
    private final String name;

    public OptionName(String name) {
        this.name = name;

        if (!isCorrectOptionName(this.name)) {
            throw new IllegalArgumentException("이름은 최대 50자 이내이어야 하며, 특수문자로는 (),[],+,-,&,/,_만 사용 가능합니다.");
        }
    }

    public String getName() {
        return name;
    }

    private boolean isCorrectOptionName(String name){
        if(name.length()>50){
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
}
