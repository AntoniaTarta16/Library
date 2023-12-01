package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.CartItem;
import model.Order;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.user.AuthenticationService;
import view.CartView;
import view.CustomerView;
import view.LoginView;

import java.util.List;

public class CartController {
    private final CartView cartView;
    private List<Order> orders;
    private final ComponentFactory componentFactory;

    public CartController(CartView cartView, ComponentFactory componentFactory) {
        this.cartView = cartView;
        this.componentFactory = componentFactory;

        this.cartView.addBuyButtonListener(new BuyButtonListener());
    }

    private class BuyButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<CartItem> cartItems = cartView.getCartItems();

            //////////////////////////////////
            if (cartItems.isEmpty()) {
                return;
            }
            /////////////////////////////////

            ///// create Order /////////////
            ///////////////////////////////
            for(CartItem cartItem : cartItems){
                System.out.println(cartItem.getTitle());
                Book book = componentFactory.getBookService().findByTitle(cartItem.getTitle());
                componentFactory.getBookService().updateStock(book.getId(), book.getStock() - cartItem.getQuantity());
            }

            Stage loginStage = (Stage) cartView.getScene().getWindow();
            loginStage.close();
            CustomerView customerView = new CustomerView(new Stage(), componentFactory);
            CustomerController customerController = new CustomerController(customerView, componentFactory);

            showSuccessMessage("The order has been successfully placed!");
        }
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succes!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
