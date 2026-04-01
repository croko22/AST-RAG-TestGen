import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ApplicationTest {

 @Mock
 private ApplicationContext ctx;

 @Mock
 private SpringApplication springApplication;

 @InjectMocks
 private Application application;

 @Test
 void testMain() {
 application.main(new String[]{});
 verify(springApplication, org.mockito.Mockito.times(1)).run(any(Class.class), any(String[].class));
 }

 @Test
 void testCommandLineRunner() {
 CommandLineRunner commandLineRunner = application.commandLineRunner(ctx);
 commandLineRunner.run(new String[]{});
 verify(ctx, org.mockito.Mockito.times(1)).getBeanDefinitionNames();
 }
}