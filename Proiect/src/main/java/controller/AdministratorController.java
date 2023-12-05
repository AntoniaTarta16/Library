package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.Order;
import model.User;
import model.validator.Notification;
import view.AdministratorView;
import view.LoginView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdministratorController {
    private final AdministratorView administratorView;
    private final ComponentFactory componentFactory;

    private final List<User> modifiedEmployees = new ArrayList<>();


    public AdministratorController(AdministratorView administratorView, ComponentFactory componentFactory) {
        this.administratorView = administratorView;
        this.componentFactory = componentFactory;
        this.administratorView.addAddButtonListener(new AddButtonListener());
        this.administratorView.addDeleteButtonListener(new DeleteButtonListener());
        this.administratorView.addUpdateButtonListener(new UpdateButtonListener());
        this.administratorView.addReportButtonListener(new ReportButtonListener());
        this.administratorView.addLogoutButtonListener(new LogoutButtonListener());

        administratorView.setEditEventHandler(this::handleEdit);
    }

    private void handleEdit(TableColumn.CellEditEvent<User, ?> event) {
        User employee = event.getRowValue();
        String columnName = event.getTableColumn().getText();
        Object newValue = event.getNewValue();

        switch (columnName) {
            case "Employee":
                employee.setUsername((String) newValue);
                break;
        }

        if (!modifiedEmployees.contains(employee)) {
            modifiedEmployees.add(employee);
        }

        administratorView.getTableEmployees().refresh();
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<User> failedToUpdateEmployees = new ArrayList<>();
            for (User modifiedEmployee : modifiedEmployees) {
                if(!componentFactory.getUserService().updateUsername(modifiedEmployee.getId(), modifiedEmployee.getUsername())){
                    failedToUpdateEmployees.add(modifiedEmployee);
                }
            }

            if (!failedToUpdateEmployees.isEmpty()) {
                showMessage("Failed to update some employees in the database!");
            }
            else {
                if(!modifiedEmployees.isEmpty()){
                    showSuccessMessage("Done!");
                    modifiedEmployees.clear();
                    administratorView.getTableEmployees().refresh();
                }
            }
        }
    }
    private class AddButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {

            String username = administratorView.getUsernameInput().getText();
            String password =  administratorView.getPasswordInput().getText();

            Notification<Boolean> registerNotification = componentFactory.getAuthenticationService().register(username, password);
            User user = componentFactory.getUserService().findByUsername(username);
            componentFactory.getUserService().updateRole(user.getId(),"employee");
            if (registerNotification.hasErrors()) {
                administratorView.setActionTargetText(registerNotification.getFormattedErrors());
            }
            else {
                administratorView.getTableEmployees().getItems().add(user);
                administratorView.setActionTargetText("Register successful!");
                administratorView.getUsernameInput().clear();
                administratorView.getPasswordInput().clear();
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            ObservableList<User> allUsers;
            ObservableList<User> selectedUsers;

            allUsers = administratorView.getTableEmployees().getItems();
            selectedUsers = administratorView.getTableEmployees().getSelectionModel().getSelectedItems();

            List<User> selectedUserList = new ArrayList<>(selectedUsers);

            if(!selectedUserList.isEmpty()){
                for(User user : selectedUserList){
                    if(!componentFactory.getUserService().deleteUser(user.getUsername())) {
                        showMessage("Something went wrong!");
                        return;
                    }
                }

                selectedUsers.forEach(allUsers::remove);

                showSuccessMessage("Done!");
            }
            else {
                showMessage("Select an employee!");
            }
        }
    }

    private class ReportButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("EmployeesReport.pdf"));
                document.open();

                List<User> employees = componentFactory.getUserService().findEmployees();
                for(User employee : employees){
                    List<Order> orders = componentFactory.getOrderService().findOrderByEmployee(employee.getId());
                    int quantity = 0;
                    int total = 0;
                    for(Order order : orders){
                        quantity += order.getQuantity();
                        total += order.getTotal();
                    }
                    document.add(new Paragraph(new Phrase("Employee: " + employee.getUsername() + " " +
                            "sold " + quantity + " items, " +
                            "total value is: " + total)));
                }
                showSuccessMessage("Done!");

            }
            catch (DocumentException | FileNotFoundException e) {
                e.printStackTrace();
                showMessage("Something went wrong!");
            }
            finally {
                document.close();
            }
        }
    }

    private class LogoutButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            Stage loginStage = (Stage) administratorView.getScene().getWindow();
            loginStage.close();
            LoginView loginView = new LoginView(new Stage());
            LoginController loginController = new LoginController(loginView, componentFactory);
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
