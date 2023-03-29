
import com.demo.learning.UserController;
import com.demo.learning.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {


    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("spring-config.xml");
        UserService userService = context.getBean("userService", UserService.class);
        System.out.println(userService.sayHi());
        UserController userController = context.getBean("userController", UserController.class);
        System.out.println(userController.sayHello());
    }

}
