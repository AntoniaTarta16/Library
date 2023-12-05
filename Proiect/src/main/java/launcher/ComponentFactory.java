package launcher;

import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;
import view.LoginView;

import java.sql.Connection;

public class ComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;


    /*private static volatile ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTests, Stage stage){
        if (instance == null){
            synchronized(ComponentFactory.class){
                if (instance == null){
                    instance = new ComponentFactory(componentsForTests, stage);
                }
            }
        }
        return instance;
    }*/

    private static ComponentFactory instance;

    public static synchronized ComponentFactory getInstance(Boolean componentsForTests, Stage stage){
        if (instance == null){
            instance = new ComponentFactory(componentsForTests, stage);
        }
        return instance;
    }

    public ComponentFactory(Boolean componentsForTests, Stage stage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.loginView = new LoginView(stage);
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.loginController = new LoginController(loginView, this);
        this.userService = new UserServiceImpl(userRepository);
        this.orderRepository = new OrderRepositoryMySQL(connection);
        this.orderService = new OrderServiceImpl(orderRepository);

    }

    public BookService getBookService() {
        return bookService;
    }

    public AuthenticationService getAuthenticationService(){
        return authenticationService;
    }

    public UserService getUserService(){return userService; }

    public OrderService getOrderService() {
        return orderService;
    }
}