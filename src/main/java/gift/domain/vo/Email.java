package gift.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    protected Email() {
    }

    private Email(String value) {
        this.value = value;
    }

    public static Email from(String email) {
        return new Email(email);
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
        Email e = (Email) o;
        return Objects.equals(this.value, e.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
