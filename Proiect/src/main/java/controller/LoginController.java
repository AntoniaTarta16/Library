package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.user.AuthenticationService;
import view.CustomerView;
import view.LoginView;

public class LoginController {

    private final LoginView loginView;
    private final ComponentFactory componentFactory;


    public LoginController(LoginView loginView, ComponentFactory componentFactory) {
        this.loginView = loginView;
        this.componentFactory = componentFactory;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = componentFactory.getAuthenticationService().login(username, password);

            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }
            else{

                Stage loginStage = (Stage) loginView.getScene().getWindow();
                loginStage.close();
                CustomerView customerView = new CustomerView(new Stage(), componentFactory);
                CustomerController customerController = new CustomerController(customerView, componentFactory);
            }

        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            }
            else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}