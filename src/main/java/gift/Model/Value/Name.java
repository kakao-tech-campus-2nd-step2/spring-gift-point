package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Name {

    private String value;

    protected Name() {}

    public Name(String value) {
        validateName(value);

        this.value = value;
    }

    private void validateName(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("이름 값은 필수입니다");

    }

    public void checkNameLength(int length){
        if (length <= 0 )
            throw new IllegalArgumentException("이름의 길이값이 0일수는 없습니다");

        if (value.length() > length){
            throw new IllegalArgumentException("이름의 길이가 "+length+"를 초과합니다");
        }
    }

    public void checkNamePattern(Pattern pattern){
        if (pattern == null)
            throw new IllegalArgumentException("검사하려는 패턴이 null입니다");
        if (!pattern.matcher(value).matches())
            throw new IllegalArgumentException("이름에 허용되지 않은 패턴이 들어있습니다");
    }

    public boolean isSame(String value){
        return this.value.equals(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Name))
            return false;

        Name name = (Name) object;
        return Objects.equals(this.value, name.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
