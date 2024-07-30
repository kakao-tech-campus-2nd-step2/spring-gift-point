package gift.entity;

import org.hibernate.usertype.UserType;

public enum MemberType {
    KAKAO("kakao"),
    NORMAL_USER("normal-user");


    private final String value;

    MemberType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MemberType fromValue(String value) {
        for (MemberType type : MemberType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
