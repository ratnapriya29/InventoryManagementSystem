package model;

import java.util.Date;

public class Product implements Comparable<Product> {

    private String sku;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private Date lastUpdated;

    // Constructor
    public Product(String sku, String name, String category, double price, int quantity) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    // ================= GETTERS =================
    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    // ================= SETTERS =================
    public void setPrice(double price) {
        this.price = price;
        this.lastUpdated = new Date();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    // ================= BUSINESS LOGIC =================
    public double getInventoryValue() {
        return price * quantity;
    }

    // ================= COMPARABLE (SORT BY SKU) =================
    @Override
    public int compareTo(Product other) {
        return this.sku.compareTo(other.sku);
    }

    // ================= HASHSET SUPPORT =================
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        return sku.equals(product.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

    // ================= DISPLAY =================
    @Override
    public String toString() {
        return "SKU: " + sku +
               ", Name: " + name +
               ", Category: " + category +
               ", Price: ₹" + price +
               ", Quantity: " + quantity +
               ", Value: ₹" + getInventoryValue();
    }
}