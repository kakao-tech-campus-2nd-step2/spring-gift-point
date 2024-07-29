package gift;

import gift.domain.BaseEntity;
import gift.domain.Order;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtil {
    public static void setId(Object entity, Long id) {
        try {
            Field idField = BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setDate(Order entity){
        try {
            Field idField = Order.class.getDeclaredField("orderDateTime");
            idField.setAccessible(true);
            LocalDateTime date = LocalDateTime.now();
            idField.set(entity, date);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
