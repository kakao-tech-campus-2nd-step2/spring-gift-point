package gift.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {

    @Column(name = "password")
    private String value;

    protected Password() {
    }

    private Password(String value) {
        this.value = value;
    }

    public static Password from(String password) {
        return new Password(password);
    }

    public boolean matches(String password) {
        return this.value.equals(password);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password p = (Password) o;
        return Objects.equals(this.value, p.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
