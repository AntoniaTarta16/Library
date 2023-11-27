package model;

public class CartItem {
    private String title;
    private int quantity;
    private int price;

    public CartItem(String title, int quantity, int price) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }
    public int getPrice(){
        return price;
    }
}
