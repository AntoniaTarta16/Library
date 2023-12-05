package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import launcher.ComponentFactory;
import model.Book;

import java.time.LocalDate;

public class EmployeeView {
    private Scene scene;
    private TableView<Book> tableBooks;
    private ObservableList<Book> books;
    private TextField titleInput;
    private TextField authorInput;
    private TextField publishedDateInput;
    private TextField stockInput;
    private TextField priceInput;
    private Button addButton;
    private Button deleteButton;
    private Button updateButton;
    private Button sellButton;
    private Button reportButton;
    private Button logoutButton;

    private EventHandler<CellEditEvent<Book, ?>> editEventHandler;
    private final ComponentFactory componentFactory;

    public EmployeeView(Stage primaryStage, ComponentFactory componentFactory) {
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
        Text sceneTitle = new Text("Books");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeFields(GridPane gridPane){

        tableBooks = new TableView<>();
        tableBooks.setEditable(true);

        TableColumn<Book, String> titleBook = new TableColumn<>("Title");
        titleBook.setMinWidth(200);
        titleBook.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleBook.setCellFactory(TextFieldTableCell.forTableColumn());
        titleBook.setOnEditCommit(event -> handleEdit(event, "title"));

        TableColumn<Book, String> authorBook = new TableColumn<>("Author");
        authorBook.setMinWidth(200);
        authorBook.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorBook.setCellFactory(TextFieldTableCell.forTableColumn());
        authorBook.setOnEditCommit(event -> handleEdit(event, "author"));

        TableColumn<Book, LocalDate> publishedDateBook = new TableColumn<>("Published Date");
        publishedDateBook.setMinWidth(200);
        publishedDateBook.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        publishedDateBook.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        publishedDateBook.setOnEditCommit(event -> handleEdit(event, "publishedDate"));

        TableColumn<Book, Integer> stockBook = new TableColumn<>("Stock");
        stockBook.setMinWidth(100);
        stockBook.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockBook.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        stockBook.setOnEditCommit(event -> handleEdit(event, "stock"));

        TableColumn<Book, Integer> priceBook = new TableColumn<>("Price");
        priceBook.setMinWidth(100);
        priceBook.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceBook.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceBook.setOnEditCommit(event -> handleEdit(event, "price"));

        books = getBook();
        tableBooks.setItems(books);
        tableBooks.getColumns().addAll(titleBook, authorBook, publishedDateBook, stockBook, priceBook);
        gridPane.add(tableBooks, 0, 2);

        titleInput = new TextField();
        titleInput.setPromptText("Title");
        titleInput.setMinWidth(150);

        authorInput = new TextField();
        authorInput.setPromptText("Author");
        authorInput.setMinWidth(150);

        publishedDateInput = new TextField();
        publishedDateInput.setPromptText("Date dd/MM/yyyy");
        publishedDateInput.setMinWidth(150);

        stockInput = new TextField();
        stockInput.setPromptText("Stock");
        stockInput.setMinWidth(70);

        priceInput = new TextField();
        priceInput.setPromptText("Price");
        priceInput.setMinWidth(70);

        addButton = new Button("Add");
        deleteButton = new Button("Delete");
        updateButton = new Button("Update");
        sellButton = new Button("Sell");
        reportButton = new Button("Generate report");
        logoutButton = new Button("Logout");

        HBox inputBox = new HBox();
        inputBox.setPadding(new Insets(10,10,10,10));
        inputBox.setSpacing(7);
        inputBox.getChildren().addAll(titleInput, authorInput, publishedDateInput, stockInput, priceInput);

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10, 10, 10, 10));
        buttonBox.setSpacing(110);
        buttonBox.getChildren().addAll(addButton, deleteButton, updateButton, sellButton, reportButton, logoutButton);


        gridPane.add(inputBox, 0, 5);
        gridPane.add(buttonBox, 0, 6);

    }

    private void handleEdit(CellEditEvent<Book, ?> event, String propertyName) {

        if (editEventHandler != null) {
            editEventHandler.handle(event);
        }
    }

    public void setEditEventHandler(EventHandler<CellEditEvent<Book, ?>> editEventHandler) {
        this.editEventHandler = editEventHandler;
    }

    public Scene getScene() {
        return scene;
    }
    public ObservableList<Book>  getBook(){
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(componentFactory.getBookService().findAll());
        return books;
    }


    public void addAddButtonListener(EventHandler<ActionEvent> addButtonListener) {
        addButton.setOnAction(addButtonListener);
    }

    public void addLogoutButtonListener(EventHandler<ActionEvent> logoutButtonListener) {
        logoutButton.setOnAction(logoutButtonListener);
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

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener) {
        sellButton.setOnAction(sellButtonListener);
    }
    public TextField getTitleInput() {
        return titleInput;
    }

    public TextField getAuthorInput() {
        return authorInput;
    }

    public TextField getPublishedDateInput() {
        return publishedDateInput;
    }

    public TextField getStockInput() {
        return stockInput;
    }

    public TextField getPriceInput() {
        return priceInput;
    }

    public TableView<Book> getTableBooks() {
        return tableBooks;
    }

}
