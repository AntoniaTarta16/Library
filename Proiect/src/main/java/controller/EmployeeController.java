package controller;

import com.itextpdf.text.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Book;
import model.Order;
import model.builder.BookBuilder;
import view.EmployeeView;
import view.SellBookView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class EmployeeController {

    private final EmployeeView employeeView;
    private final ComponentFactory componentFactory;

    private final Long userId;
    private final List<Book> modifiedBooks = new ArrayList<>();


    public EmployeeController(EmployeeView employeeView, ComponentFactory componentFactory, Long userId) {
        this.employeeView = employeeView;
        this.componentFactory = componentFactory;
        this.userId = userId;
        this.employeeView.addAddButtonListener(new AddButtonListener());
        this.employeeView.addDeleteButtonListener(new DeleteButtonListener());
        this.employeeView.addUpdateButtonListener(new UpdateButtonListener());
        this.employeeView.addSellButtonListener(new SellButtonListener());
        this.employeeView.addReportButtonListener(new ReportButtonListener());

        employeeView.setEditEventHandler(this::handleEdit);
    }

    private void handleEdit(CellEditEvent<Book, ?> event) {
        Book book = event.getRowValue();
        String columnName = event.getTableColumn().getText();
        Object newValue = event.getNewValue();

        switch (columnName) {
            case "Title":
                book.setTitle((String) newValue);
                break;
            case "Author":
                book.setAuthor((String) newValue);
                break;
            case "Published Date":
                book.setPublishedDate((LocalDate) newValue);
                break;
            case "Stock":
                book.setStock((Integer) newValue);
                break;
            case "Price":
                book.setPrice((Integer) newValue);
                break;
        }

        if (!modifiedBooks.contains(book)) {
            modifiedBooks.add(book);
        }

        employeeView.getTableBooks().refresh();
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<Book> failedToUpdateBooks = new ArrayList<>();
            for (Book modifiedBook : modifiedBooks) {
                if(!componentFactory.getBookService().updateBook(modifiedBook)){
                    failedToUpdateBooks.add(modifiedBook);
                }
            }

            if (!failedToUpdateBooks.isEmpty()) {
                for (Book failedBook : failedToUpdateBooks) {
                    employeeView.getTableBooks().getItems().stream()
                            .filter(book -> book.getId().equals(failedBook.getId()))
                            .findFirst()
                            .ifPresent(book -> {employeeView.getTableBooks().refresh();});
                }
                showMessage("Failed to update some books in the database!");
            }
            else {
                if(!modifiedBooks.isEmpty()){
                    showSuccessMessage("Done!");
                    modifiedBooks.clear();
                    employeeView.getTableBooks().refresh();
                }
            }
        }
    }
    private class AddButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(employeeView.getPublishedDateInput().getText(), inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String outputDate = localDate.format(outputFormatter);

            try{
                Integer.parseInt(employeeView.getStockInput().getText());
                Integer.parseInt(employeeView.getPriceInput().getText());
                LocalDate.parse(outputDate);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                showMessage("Invalid inputs!");
                return;
            }
            if(employeeView.getTitleInput().getText() == null || employeeView.getAuthorInput().getText() == null ){
                showMessage("Invalid inputs!");
                return;
            }

            Book book = new BookBuilder()
                    .setTitle(employeeView.getTitleInput().getText())
                    .setAuthor(employeeView.getAuthorInput().getText())
                    .setPublishedDate(LocalDate.parse(outputDate))
                    .setStock(Integer.parseInt(employeeView.getStockInput().getText()))
                    .setPrice(Integer.parseInt(employeeView.getPriceInput().getText()))
                    .build();

            if(!componentFactory.getBookService().save(book)){
                showMessage("Something went wrong!");
                return;
            }

            showSuccessMessage("Done!");

            book.setId(componentFactory.getBookService().findByTitle(book.getTitle()).getId());
            employeeView.getTableBooks().getItems().add(book);

            employeeView.getTitleInput().clear();
            employeeView.getAuthorInput().clear();
            employeeView.getPublishedDateInput().clear();
            employeeView.getStockInput().clear();
            employeeView.getPriceInput().clear();
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            ObservableList<Book> allBooks;
            ObservableList<Book> selectedBooks;

            allBooks = employeeView.getTableBooks().getItems();
            selectedBooks = employeeView.getTableBooks().getSelectionModel().getSelectedItems();

            List<Book> selectedBookList = new ArrayList<>(selectedBooks);

            if(!selectedBookList.isEmpty()){
                for(Book book : selectedBookList){
                    if(!componentFactory.getBookService().deleteBookByTitleAndAuthor(book)) {
                        showMessage("Something went wrong!");
                        return;
                    }
                }

                selectedBooks.forEach(allBooks::remove);

                showSuccessMessage("Done!");
            }
            else {
                showMessage("Select a book!");
            }
        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Stage loginStage = (Stage) employeeView.getScene().getWindow();
            loginStage.close();
            SellBookView sellBookView = new SellBookView(new Stage(), componentFactory);
            SellBookController sellBookController = new SellBookController(sellBookView, componentFactory, userId);

        }
    }

    private class ReportButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("SoldItemsReport.pdf"));
                document.open();

                document.add(new Paragraph(new Phrase("User " + componentFactory.getUserService().findById(userId).getUsername() + "-SOLD ITEMS")));
                List<Order> orders = componentFactory.getOrderService().findOrderByEmployee(userId);

                if(!orders.isEmpty()) {
                    for (Order order : orders) {
                        document.add(new Paragraph(new Phrase("Book:" + componentFactory.getBookService().findById(order.getBookId()).getTitle()) + " " +
                                "Quantity:" + String.valueOf(order.getQuantity()) + " " +
                                "Total price:" + String.valueOf(order.getTotal())));
                    }
                }
                else{
                    document.add(new Phrase("ZERO ITEMS SOLD"));
                }
                showSuccessMessage("Done!");

            }
            catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
                showMessage("Somethinq went wrong!");
            }
            finally {
                document.close();
            }
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Attention!");
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
