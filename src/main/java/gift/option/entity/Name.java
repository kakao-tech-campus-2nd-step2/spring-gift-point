package gift.option.entity;

import static gift.exception.ErrorMessage.OPTION_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.OPTION_NAME_LENGTH;

import jakarta.persistence.Embeddable;

@Embeddable
public class Name {

    private String value;

    public Name() {
    }

    public Name(String name) {
        validate(name);
        value = name;
    }

    public String getValue() {
        return value;
    }

    public void update(String name) {
        validate(name);
        value = name;
    }

    private void validate(String name) {
        if (!isStringOnlyIncludeAllowedCharacters(name)) {
            throw new IllegalArgumentException(OPTION_NAME_ALLOWED_CHARACTER);
        }

        if (isStringLengthOver50(name)) {
            throw new IllegalArgumentException(OPTION_NAME_LENGTH);
        }
    }

    private boolean isStringLengthOver50(String string) {
        return string.length() > 50;
    }

    private boolean isStringOnlyIncludeAllowedCharacters(String string) {
        return string.matches("^[a-zA-Z0-9가-힣\s()\\[\\]+\\-&/_]*$");
    }
}
