package hw6_peters_client;

import java.math.BigDecimal;

public class TableEntry {

    private String name,quantity,subtotal,total;

    TableEntry(String nameInput, String quantityInput, String subtotalInput, String totalInput) {
        name = nameInput;
        quantity = quantityInput;
        subtotal = subtotalInput;
        total = totalInput;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getTotal() {
        return total;
    }
}
