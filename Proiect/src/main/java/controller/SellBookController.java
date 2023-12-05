package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Order;
import model.builder.BookBuilder;
import model.builder.OrderBuilder;
import view.CartView;
import view.SellBookView;

import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SellBookController {
    private final SellBookView sellBookView;
    private List<Book> books = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private Book selectedBook = null;
    private int quantity = 0;
    private int total = 0;
    private final Long userId;
    private final ComponentFactory componentFactory;

    public SellBookController(SellBookView sellBookView, ComponentFactory componentFactory, Long userId) {
        this.sellBookView = sellBookView;
        this.componentFactory = componentFactory;
        this.userId = userId;
        this.sellBookView.addBookSelectionListener(new BookSelectionListener());
        this.sellBookView.addSellButtonListener(new SellButtonListener());
        sellBookView.addQuantityTextFieldEventHandler(event -> {
            if(sellBookView.getComboBoxBooks().getValue() == null){
                showMessage("Choose a book!");
                sellBookView.getQuantityTextField().clear();
                return ;
            }
            quantity = Integer.parseInt(sellBookView.getQuantityTextField().getText());
            total = selectedBook.getPrice() * quantity;
            sellBookView.getTotalLabel().setText("Total: " + total);
        });
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            if(sellBookView.getComboBoxBooks().getValue() == null){
                showMessage("Choose a book!");
                return ;
            }
            try{
                Integer.parseInt(sellBookView.getQuantityTextField().getText());
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                showMessage("Invalid inputs!");
                return;
            }
            if(sellBookView.getUserTextField().getText() == null){
                showMessage("Invalid inputs!");
                return;
            }
            if(componentFactory.getUserService().findByUsername(sellBookView.getUserTextField().getText()) == null){
                showMessage("Invalid user!");
                return;
            }
            if(quantity > selectedBook.getStock()){
                showMessage("Wrong quantity!");
            }
            else{
                Order order = new OrderBuilder()
                        .setUserId(userId)
                        .setCustomerId(componentFactory.getUserService().findByUsername(sellBookView.getUserTextField().getText()).getId())
                        .setBookId(selectedBook.getId())
                        .setQuantity(quantity)
                        .setTotal(total)
                        .build();
                if(!componentFactory.getOrderService().save(order)) {
                    showMessage("Something went wrong!");
                }

                componentFactory.getBookService().updateStock(selectedBook.getId(), selectedBook.getStock() - quantity);
                showSuccessMessage("Done!");
            }

        }
    }
    private class BookSelectionListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            selectedBook = componentFactory.getBookService().findByTitle(sellBookView.getSelectedBook());
            sellBookView.getTotalLabel().setText("Total: " + selectedBook.getPrice());
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Be careful!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
