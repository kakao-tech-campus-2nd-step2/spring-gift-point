package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import gift.entity.User;
import gift.repository.UserRepository;
import gift.service.PointService;
import gift.service.UserService;

public class PointServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PointService pointService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("test@test.com", "pw");
        user.setId(1L);
        user.setPoints(100000);
    }

    @Test
    public void testChargePoints() {
        when(userService.findById(anyLong())).thenReturn(user);

        pointService.chargePoints(user.getId(), 5000);

        verify(userRepository).save(user);
        assertThat(user.getPoints()).isEqualTo(105000);
    }

    @Test
    public void testDeductPoints() {
        when(userService.findById(anyLong())).thenReturn(user);

        int totalPrice = 50000;
        int discountedPrice = (int) (totalPrice * 0.9);
        int pointsToUse = pointService.deductPoints(user.getId(), discountedPrice);

        verify(userRepository).save(user);
        assertThat(pointsToUse).isEqualTo(4500);
        assertThat(user.getPoints()).isEqualTo(100000 - 4500);
    }


    @Test
    public void testAddPoints() {
        when(userService.findById(anyLong())).thenReturn(user);

        int totalPrice = 5000;
        pointService.addPoints(user.getId(), totalPrice);

        verify(userRepository).save(user);
        assertThat(user.getPoints()).isEqualTo(100500);
    }
}
