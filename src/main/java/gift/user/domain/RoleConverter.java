package gift.user.domain;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Set<Role>, String> {
	private static final String DELIMITER = ",";

	@Override
	public String convertToDatabaseColumn(Set<Role> attribute) {
		return attribute.stream().map(Role::name).sorted().collect(Collectors.joining(DELIMITER));
	}

	@Override
	public Set<Role> convertToEntityAttribute(String dbData) {
		return Arrays.stream(dbData.split(DELIMITER)).map(Role::valueOf).collect(Collectors.toSet());
	}
}
