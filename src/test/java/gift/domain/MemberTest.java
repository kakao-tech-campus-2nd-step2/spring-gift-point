package gift.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberTest {

    @Test
    public void testMemberCreation() {
        Member member = new Member("test@example.com", "password123");

        assertEquals("test@example.com", member.getEmail());
        assertEquals("password123", member.getPassword());
    }

    @Test
    public void testMemberWishes() {
        Member member = new Member("test@example.com", "password123");
        Category category = new Category();
        Product product = new Product("Test Product", 1000, "test.jpg", category);
        Wish wish = new Wish(member, product);

        member.getWishes().add(wish);

        assertEquals(1, member.getWishes().size());
        assertTrue(member.getWishes().contains(wish));
    }
}
