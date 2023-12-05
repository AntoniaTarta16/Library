package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import launcher.ComponentFactory;
import model.User;

public class AdministratorView {
    private Scene scene;
    private TableView<User> tableEmployees;
    private ObservableList<User> employees;
    private TextField usernameInput;
    private TextField passwordInput;
    private Button addButton;
    private Button deleteButton;
    private Button updateButton;
    private Button reportButton;
    private Button logoutButton;
    private Text actiontarget;

    private EventHandler<TableColumn.CellEditEvent<User, ?>> editEventHandler;
    private final ComponentFactory componentFactory;

    public AdministratorView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;

        primaryStage.setTitle("Books Store");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        scene = new Scene(gridPane, 1100, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        initializeSceneTitle(gridPane);

        initializeFields(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeSceneTitle(GridPane gridPane){
        Text sceneTitle = new Text("Employees");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeFields(GridPane gridPane){

        tableEmployees = new TableView<>();
        tableEmployees.setEditable(true);

        TableColumn<User, String> usernameColumn = new TableColumn<>("Employee");
        usernameColumn.setMinWidth(400);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setOnEditCommit(event -> handleEdit(event, "username"));


        employees = getEmployee();
        tableEmployees.setItems(employees);
        tableEmployees.getColumns().addAll(usernameColumn);
        gridPane.add(tableEmployees, 0, 2);

        usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        usernameInput.setMinWidth(150);

        passwordInput = new TextField();
        passwordInput.setPromptText("Password");
        passwordInput.setMinWidth(150);



        addButton = new Button("Add");
        deleteButton = new Button("Delete");
        updateButton = new Button("Update");
        reportButton = new Button("Generate report");
        logoutButton = new Button("Logout");

        HBox inputBox = new HBox();
        inputBox.setPadding(new Insets(10,10,10,10));
        inputBox.setSpacing(7);
        inputBox.getChildren().addAll(usernameInput, passwordInput, logoutButton);

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10, 10, 10, 50));
        buttonBox.setSpacing(110);
        buttonBox.getChildren().addAll(addButton, deleteButton, updateButton, reportButton);


        gridPane.add(inputBox, 0, 5);
        gridPane.add(buttonBox, 0, 6);
        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        gridPane.add(actiontarget, 1, 5);

    }

    private void handleEdit(TableColumn.CellEditEvent<User, ?> event, String propertyName) {

        if (editEventHandler != null) {
            editEventHandler.handle(event);
        }
    }

    public void setEditEventHandler(EventHandler<TableColumn.CellEditEvent<User, ?>> editEventHandler) {
        this.editEventHandler = editEventHandler;
    }

    public Scene getScene() {
        return scene;
    }
    public ObservableList<User>  getEmployee(){
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(componentFactory.getUserService().findEmployees());
        return users;
    }


    public void addAddButtonListener(EventHandler<ActionEvent> addButtonListener) {
        addButton.setOnAction(addButtonListener);
    }


    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addReportButtonListener(EventHandler<ActionEvent> reportButtonListener) {
        reportButton.setOnAction(reportButtonListener);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }

    public void addLogoutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logoutButton.setOnAction(logoutButtonListener);
    }

    public TextField getUsernameInput() {
        return usernameInput;
    }

    public TextField getPasswordInput() {
        return passwordInput;
    }

    public TableView<User> getTableEmployees() {
        return tableEmployees;
    }

    public void setActionTargetText(String text){ this.actiontarget.setText(text);}
}
