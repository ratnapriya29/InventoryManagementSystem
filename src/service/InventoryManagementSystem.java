package service;

import model.Product;
import comparators.PriceComparator;
import comparators.ValueComparator;

import java.util.*;
import java.util.function.Predicate;

public class InventoryManagementSystem {

    // ================= COLLECTIONS =================
    private HashSet<Product> productSet;
    private TreeSet<Product> sortedProducts;
    private HashMap<String, Product> productMap;

    private LinkedList<String> transactionHistory;

    private Deque<Product> undoStack;
    private Deque<Product> redoStack;

    private Queue<Product> updateQueue;

    private PriorityQueue<Product> lowStockQueue;

    // ================= CONSTRUCTOR =================
    public InventoryManagementSystem() {
        productSet = new HashSet<>();
        sortedProducts = new TreeSet<>();
        productMap = new HashMap<>();

        transactionHistory = new LinkedList<>();

        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();

        updateQueue = new LinkedList<>();

        lowStockQueue = new PriorityQueue<>(Comparator.comparingInt(Product::getQuantity));
    }

    // ================= ADD PRODUCT =================
    public void addProduct(Product product) {

        if (productSet.add(product)) {

            sortedProducts.add(product);
            productMap.put(product.getSku(), product);

            logTransaction("ADD", product);

            // Low stock check
            if (product.getQuantity() < 10) {
                lowStockQueue.offer(product);
            }

            System.out.println("✅ Product added successfully!");

        } else {
            System.out.println("❌ Product with this SKU already exists!");
        }
    }

    // ================= UPDATE PRODUCT =================
    public void updateQuantity(String sku, int newQuantity) {

        Product product = productMap.get(sku);

        if (product == null) {
            System.out.println("❌ Product not found!");
            return;
        }

        // Save for undo
        undoStack.push(new Product(
                product.getSku(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity()
        ));

        // Clear redo stack
        redoStack.clear();

        int oldQty = product.getQuantity();
        product.setQuantity(newQuantity);

        updateQueue.offer(product);

        logTransaction("UPDATE (" + oldQty + " → " + newQuantity + ")", product);

        System.out.println("✅ Quantity updated!");
    }

    // ================= UNDO =================
    public void undo() {

        if (undoStack.isEmpty()) {
            System.out.println("❌ Nothing to undo!");
            return;
        }

        Product prev = undoStack.pop();

        Product current = productMap.get(prev.getSku());

        // Save current for redo
        redoStack.push(new Product(
                current.getSku(),
                current.getName(),
                current.getCategory(),
                current.getPrice(),
                current.getQuantity()
        ));

        current.setQuantity(prev.getQuantity());

        System.out.println("✅ Undo successful!");
    }

    // ================= REDO =================
    public void redo() {

        if (redoStack.isEmpty()) {
            System.out.println("❌ Nothing to redo!");
            return;
        }

        Product next = redoStack.pop();

        updateQuantity(next.getSku(), next.getQuantity());

        System.out.println("✅ Redo successful!");
    }

    // ================= SEARCH =================
    public List<Product> search(Predicate<Product> condition) {

        List<Product> result = new ArrayList<>();

        for (Product product : productSet) {
            if (condition.test(product)) {
                result.add(product);
            }
        }

        return result;
    }

    // ================= SORT =================
    public void sortAndDisplay(Comparator<Product> comparator) {

        List<Product> list = new ArrayList<>(productSet);

        list.sort(comparator);

        System.out.println("\n=== SORTED PRODUCTS ===");

        for (Product p : list) {
            System.out.println(p);
        }
    }

    // ================= LOW STOCK =================
    public void displayLowStock() {

        System.out.println("\n⚠️ Low Stock Products:");

        if (lowStockQueue.isEmpty()) {
            System.out.println("No low stock items.");
            return;
        }

        for (Product p : lowStockQueue) {
            System.out.println(p);
        }
    }

    // ================= TRANSACTIONS =================
    private void logTransaction(String type, Product product) {

        String log = type + " → " + product.getSku() + " (" + new Date() + ")";
        transactionHistory.addFirst(log);
    }

    public void showTransactions(int count) {

        System.out.println("\n=== TRANSACTIONS ===");

        int i = 0;

        for (String t : transactionHistory) {
            if (i >= count) break;
            System.out.println(t);
            i++;
        }
    }

    // ================= STATISTICS =================
    public void displayStatistics() {

        System.out.println("\n=== INVENTORY STATISTICS ===");

        double totalValue = 0;

        Map<String, Double> categoryMap = new HashMap<>();

        for (Product p : productSet) {

            double value = p.getInventoryValue();
            totalValue += value;

            categoryMap.put(
                    p.getCategory(),
                    categoryMap.getOrDefault(p.getCategory(), 0.0) + value
            );
        }

        System.out.println("Total Products: " + productSet.size());
        System.out.println("Total Inventory Value: ₹" + totalValue);

        System.out.println("\nCategory-wise:");

        for (String cat : categoryMap.keySet()) {
            System.out.println(cat + " → ₹" + categoryMap.get(cat));
        }
    }

    // ================= GENERIC FILTER =================
    public <T> List<T> filter(Collection<T> data, Predicate<T> condition) {

        List<T> result = new ArrayList<>();

        for (T item : data) {
            if (condition.test(item)) {
                result.add(item);
            }
        }

        return result;
    }
}