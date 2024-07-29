package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Color {

    private String value;

    protected Color() {}

    public Color(String value){
        validateColor(value);

        this.value = value;
    }

    private void validateColor(String value){
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("색상 값은 필수입니다");

        if (value.length() != 7)
            throw new IllegalArgumentException(" 색상 값의 길이는 7입니다");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object){
        if (this== object)
            return true;

        if(!(object instanceof Color))
            return false;

        Color color = (Color) object;
        return Objects.equals(this.value, color.getValue());
    }

    @Override
    public int hashCode(){
        return Objects.hash(value);
    }

    @Override
    public String toString(){
        return value;
    }
}
