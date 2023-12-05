package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import view.CartView;
import view.CustomerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private final CustomerView customerView;
    private List<Book> books = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();

    private final ComponentFactory componentFactory;

    public CustomerController(CustomerView customerView, ComponentFactory componentFactory) {
        this.customerView = customerView;
        this.componentFactory = componentFactory;
        this.customerView.addAddButtonListener(new AddButtonListener());
        this.customerView.addCartButtonListener(new CartButtonListener());
    }

    private class AddButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String titleBook = customerView.getSelectedBook();

            if(titleBook != null){
                List<Book> allBooks = componentFactory.getBookService().findAll();

                int quantity = customerView.getSelectedQuantity();
                Book book = null;
                for(Book b : allBooks){
                    if(b.getTitle().equals(titleBook)){
                        book = b;
                        break;
                    }
                }

                if(book.getStock() == 0){
                    showMessage("Unavailable item!");

                }
                else{
                    if(book.getStock() >= quantity) {
                        if(!books.isEmpty()){
                            for (int i = 0; i < books.size(); i++) {

                                if((books.get(i)).getTitle().equals(titleBook)) {
                                    int oldQuantity = (quantities.get(i)).intValue();
                                    int newQuantity = oldQuantity + quantity;
                                    if(book.getStock() >= newQuantity){
                                        quantities.set(i,Integer.valueOf(newQuantity));
                                    }
                                    else {
                                        showMessage("Wrong quantity!");
                                    }
                                    break;
                                }
                                else{
                                    quantities.add(quantity);
                                    books.add(book);
                                }
                            }
                        }
                        else{
                            quantities.add(quantity);
                            books.add(book);
                        }
                    }
                    else{
                        showMessage("Wrong quantity!");
                    }
                }
            }
            else{
                showMessage("Choose a book!");
            }
        }
    }
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Be careful!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private class CartButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            if(books.isEmpty())
            {
                showMessage("0 items in cart!");
            }
            else{
                Stage loginStage = (Stage) customerView.getScene().getWindow();
                loginStage.close();
                CartView cartView = new CartView(new Stage(), books, quantities);
                CartController cartController = new CartController(cartView, componentFactory);
            }
        }
    }
}
