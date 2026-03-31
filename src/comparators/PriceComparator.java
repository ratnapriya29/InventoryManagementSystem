package comparators;

import java.util.Comparator;
import model.Product;

public class PriceComparator implements Comparator<Product> {
    public int compare(Product a, Product b) {
        return Double.compare(a.getPrice(), b.getPrice());
    }
}