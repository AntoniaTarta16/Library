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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import model.CartItem;
import service.book.BookService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CartView {

    private Scene scene;
    private TableView<CartItem> tableCart;
    private List<Book> books;
    private List<Integer> quantities;
    private int total = 0;

    private List<CartItem> cartItems;

    private Label totalLabel;
    private Button buyButton;

    public CartView(Stage primaryStage, List<Book> book, List<Integer> quantities) {

        this.books = book;
        this.quantities =quantities;
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
        Text sceneTitle = new Text("Your cart");
        sceneTitle.setFont(Font.font("Tahome", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);
    }

    private void initializeFields(GridPane gridPane){

        buyButton = new Button("Buy");
        HBox buyButtonHBox = new HBox(10);
        buyButtonHBox.setAlignment(Pos.BOTTOM_LEFT);
        buyButtonHBox.getChildren().add(buyButton);
        gridPane.add(buyButtonHBox, 0, 6);

        totalLabel = new Label("Total: " + total);
        gridPane.add(totalLabel, 0, 3, 2, 1);

        TableColumn<CartItem, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        TableColumn<CartItem, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);


        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        tableCart = new TableView<>();
        tableCart.getColumns().addAll(titleColumn, quantityColumn, priceColumn);
        gridPane.add(tableCart, 0, 2, 2, 1);


        cartItems = createCartItems(books, quantities);
        tableCart.setItems(FXCollections.observableArrayList(cartItems));


    }

    private List<CartItem> createCartItems(List<Book> books, List<Integer> quantities) {
        List<CartItem> cartItems = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            cartItems.add(new CartItem(books.get(i).getTitle(), quantities.get(i), books.get(i).getPrice()));
            total += quantities.get(i) * books.get(i).getPrice();
        }

        totalLabel.setText("Total: " + total);

        return cartItems;
    }
    public Scene getScene() {
        return scene;
    }

    public List<CartItem> getCartItems(){
        return cartItems;
    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener) {
        buyButton.setOnAction(buyButtonListener);
    }
}
