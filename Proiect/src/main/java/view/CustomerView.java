package view;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import launcher.ComponentFactory;
import model.Book;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CustomerView {
    private Scene scene;
    private TableView<Book> tableBooks;
    private ObservableList<Book> books;
    private ComboBox<String> comboBoxBooks;

    private Spinner<Integer> quantitySpinner;

    private Button addButton;
    private Button cartButton;
    private final ComponentFactory componentFactory;

    public CustomerView(Stage primaryStage, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;

        primaryStage.setTitle("Books Store");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        scene = new Scene(gridPane, 900, 600);
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

        TableColumn<Book, String> titleBook = new TableColumn<>("Title");
        titleBook.setMinWidth(200);
        titleBook.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorBook = new TableColumn<>("Author");
        authorBook.setMinWidth(200);
        authorBook.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, LocalDate> publishedDateBook = new TableColumn<>("Published Date");
        publishedDateBook.setMinWidth(200);
        publishedDateBook.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        TableColumn<Book, Integer> stockBook = new TableColumn<>("Stock");
        stockBook.setMinWidth(100);
        stockBook.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Book, Integer> priceBook = new TableColumn<>("Price");
        priceBook.setMinWidth(100);
        priceBook.setCellValueFactory(new PropertyValueFactory<>("price"));

        books = getBook();

        tableBooks = new TableView<>();
        tableBooks.setItems(books);
        tableBooks.getColumns().addAll(titleBook, authorBook, publishedDateBook, stockBook, priceBook);
        gridPane.add(tableBooks, 0, 2, 2, 1);

        Label label = new Label("Choose a Book:");
        gridPane.add(label, 0, 4);

        comboBoxBooks = new ComboBox<>();
        comboBoxBooks.setItems(extractBookTitles(books));
        comboBoxBooks.setPrefWidth(200);
        gridPane.add(comboBoxBooks, 0, 5);

        quantitySpinner = new Spinner<>(1, 10, 1);
        Label quantityLabel = new Label("Select Quantity:");
        gridPane.add(quantityLabel, 1, 4);
        gridPane.add(quantitySpinner, 1, 5);

        addButton = new Button("Add");
        HBox addButtonHBox = new HBox(10);
        addButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        addButtonHBox.getChildren().add(addButton);
        gridPane.add(addButtonHBox, 0, 6);

        cartButton = new Button("View cart");
        HBox cartButtonHBox = new HBox(10);
        cartButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        cartButtonHBox.getChildren().add(cartButton);
        gridPane.add(cartButtonHBox, 1, 6);
    }

    public Scene getScene() {
        return scene;
    }
    public ObservableList<Book>  getBook(){
        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(componentFactory.getBookService().findAll());
        return books;
    }

    public ObservableList<String> extractBookTitles(ObservableList<Book> books) {
        ObservableList<String> titles = FXCollections.observableArrayList();
        for (Book book : books) {
            titles.add(book.getTitle());
        }
        return titles;
    }
    public String getSelectedBook(){
        return comboBoxBooks.getValue();
    }

    public Integer getSelectedQuantity(){
        return quantitySpinner.getValue();
    }
    public void addAddButtonListener(EventHandler<ActionEvent> addButtonListener) {
        addButton.setOnAction(addButtonListener);
    }

    public void addCartButtonListener(EventHandler<ActionEvent> cartButtonListener) {
        cartButton.setOnAction(cartButtonListener);
    }

}
