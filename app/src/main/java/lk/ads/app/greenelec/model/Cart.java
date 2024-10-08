package lk.ads.app.greenelec.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private String email;
    private String product;
    private int quantity;
    private String image;
    private double price;

    public Cart(){
    }

    public Cart(String email, String product, int quantity, String image, double price) {
        this.email = email;
        this.product = product;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //    private List<Product> cartItems;
//
//    public Cart(){
//        cartItems = new ArrayList<>();
//    }
//
//    public void addItemToCart(Product product){
//        cartItems.add(product);
//    }
//
//    public List<Product> getCartItems(){
//        return cartItems;
//    }
}
