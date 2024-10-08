package lk.ads.app.greenelec.model;

public class Product {
    private String name;
    private String category;
    private String brand;
    private String description;
    private String colour;
    private double price;
    private double deliverPrice;
    private String image;
    private int qty;
    public Product(){}

    public Product(String name, String category, String brand, String description, String colour, double price, double deliverPrice, String image, int qty) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.colour = colour;
        this.price = price;
        this.deliverPrice = deliverPrice;
        this.image = image;
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDeliverPrice() {
        return deliverPrice;
    }

    public void setDeliverPrice(double deliverPrice) {
        this.deliverPrice = deliverPrice;
    }
}
