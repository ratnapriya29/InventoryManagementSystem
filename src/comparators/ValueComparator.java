package comparators;

import java.util.Comparator;
import model.Product;

public class ValueComparator implements Comparator<Product> {
    public int compare(Product a, Product b) {
        return Double.compare(b.getInventoryValue(), a.getInventoryValue());
    }
}