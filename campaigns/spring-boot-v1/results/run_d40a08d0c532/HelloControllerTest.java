import com.example.springboot.HelloController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.GetMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HelloControllerTest {

 @InjectMocks
 private HelloController helloController;

 @Test
 public void testIndex() {
 assertEquals("Greetings from Spring Boot!", helloController.index());
 }
}