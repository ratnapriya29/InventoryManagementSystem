package main;

import model.Product;
import service.InventoryManagementSystem;
import comparators.*;

public class Main {
    public static void main(String[] args) {

        InventoryManagementSystem ims = new InventoryManagementSystem();

        ims.addProduct(new Product("P1", "Laptop", "Electronics", 70000, 20));
        ims.addProduct(new Product("P2", "Mouse", "Electronics", 1000, 5));

        ims.sortAndDisplay(new PriceComparator());
    }
}