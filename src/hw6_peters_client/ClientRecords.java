package hw6_peters_client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class ClientRecords {

    ArrayList<ProductSpecification> productClientCatalog;

    ArrayList<SalesRecord> productSaleRecords;

    ArrayList<TableEntry> tableList;

    BigDecimal eodTotal,subTotal,subTotalTax;

    DecimalFormat currencyFormat;

    ClientRecords(DecimalFormat decimalFormat){
        // Initialize client side catalog, sale and table record
        productClientCatalog = new ArrayList<>();
        productSaleRecords = new ArrayList<>();
        tableList = new ArrayList<>();

        // Initialize total fields
        eodTotal = new BigDecimal(0);
        subTotal = new BigDecimal(0);
        subTotalTax = new BigDecimal(0);

        // Set currency format
        currencyFormat = decimalFormat;
    }

    public void addProductSale(String serverInput, int quantityInput){
        // Split serverInput into code, name, and price inputs
        String[] splitInput = serverInput.split(Pattern.quote(","));
        String codeInput = splitInput[0], nameInput = splitInput[1];
        BigDecimal priceInput = BigDecimal.valueOf(Double.parseDouble(splitInput[2]));

        // Check if productSpecification in productClientCatalog is created and add new entry if not
        addOrCreateSpecification(codeInput,nameInput,priceInput);

        // Find specification
        ProductSpecification specification = getProductSpecification(codeInput);

        // Calc price total
        BigDecimal productPriceTotal = (specification.getProductPrice().multiply(BigDecimal.valueOf(quantityInput)));

        // Add inputTotals to class fields
        eodTotal = eodTotal.add(productPriceTotal);
        subTotal = subTotal.add(productPriceTotal);
        if (specification.getProductTaxable()) {
            // Taxable
            subTotalTax = subTotalTax.add(productPriceTotal.add(productPriceTotal.multiply(BigDecimal.valueOf(.06))) );
        } else {
            // Non-taxable
            subTotalTax = subTotalTax.add(productPriceTotal);
        }

        // Check if specification is included in productSaleRecords and add new entry if not
        addOrCreateSaleRecord(specification,quantityInput,productPriceTotal);

        // Create tableList entry
        tableList.add(new TableEntry(specification.getProductName(),
                String.valueOf(quantityInput),
                currencyFormat.format(subTotal),
                currencyFormat.format(subTotalTax)));
    }

    public String createReceipt(Double changeAmount) {
        // Initialize String formats
        String receiptString = "\nItems list:\n";
        String quantityNameTotalFormat = "%4s %-19s$%7s%n",
                subtotalFormat = "%-26s$%7s%n%-21s$%7s",
                changeFormat = "%n%-25s$%7s%n%s%n";

        // Sort into alphabetical order by name
        Collections.sort(productSaleRecords);

        // Loop through salesLineItemArrayList objects
        for (SalesRecord salesRecordTracker : productSaleRecords) {
            // Add product's name, quantity, total, and new line char to return string
            receiptString = receiptString.concat(
                    String.format(quantityNameTotalFormat,
                            salesRecordTracker.getProductQuantity(),
                            salesRecordTracker.getProductName(),
                            currencyFormat.format(salesRecordTracker.getProductTotal())) );
        }

        // Compile subtotals then format strings and concat to receipt string
        receiptString = receiptString.concat(
                String.format(subtotalFormat,
                        "Subtotal",
                        currencyFormat.format(subTotal),
                        "Total with tax(6%)",
                        currencyFormat.format(subTotalTax)));


        return receiptString.concat(
                String.format(changeFormat,
                    "Change",
                    currencyFormat.format(changeAmount),
                    "----------------------------"));
    }

    /**
     * Checks code input against productClientCatalog code fields. If true return, if not add new specification.
     *
     * @param codeInput String, code input to check for specification
     */
    private void addOrCreateSpecification(String codeInput, String nameInput, BigDecimal priceInput) {
        // Loop array list to check productClientCatalog for code
        for (ProductSpecification specification : productClientCatalog) {
            if (specification.getProductCode().equals(codeInput)) {
                // Found
                return;
            }
        }

        // Not found
        productClientCatalog.add(new ProductSpecification(codeInput,nameInput,priceInput));
    }

    /**
     * Searches for a productSpecification object using a giving code. If found will return object else will return null
     *
     * @param codeInput String, item's code
     * @return ProductSpecification, if found return productSpecification else will return null
     */
    private ProductSpecification getProductSpecification(String codeInput) {
        // Loop array list to check productClientCatalog for code
        for (ProductSpecification specification : productClientCatalog) {
            if (specification.getProductCode().equals(codeInput)) {
                // Found
                return specification;
            }
        }

        // Not found
        return null;
    }

    /**
     * Checks code input against productSaleRecords code fields. If found add new total and quantity amounts to existing
     * entry, if not create new entry with fields.
     *
     * @param specification
     * @param productQuantity
     * @param productTotalPrice
     */
    private void addOrCreateSaleRecord(ProductSpecification specification, int productQuantity, BigDecimal productTotalPrice) {
        // Loop array list to check productSaleRecords for code
        for (SalesRecord salesRecord : productSaleRecords) {
            if (salesRecord.getProductCode().equals(specification.getProductCode())) {
                // SalesRecord tracker already created
                // Set the SalesRecord's price to the new total plus the old total
                salesRecord.setProductTotal((salesRecord.getProductTotal()).add(productTotalPrice));

                // Set the SalesRecord's quantity to the new amount plus the old amount
                salesRecord.setProductQuantity(productQuantity + salesRecord.getProductQuantity());

                return;

            }
        }

        // Product sale not found, create new item tracker
        productSaleRecords.add(new SalesRecord(specification,productQuantity,productTotalPrice));
    }

    // Return true if payment input is greater than or equal than subtotalTax
    public boolean checkPayment(Double input) {
        // Check if payment input is greater than or equal to subtotalTax
        return ((BigDecimal.valueOf(input)).compareTo(subTotalTax) >= 0);
    }

    public double checkout(Double input) {
        // Calc change amount
        double changeAmount = Double.parseDouble(String.valueOf(BigDecimal.valueOf(input).subtract(subTotalTax)));

        // Set up grandTotal tableList entry
        tableList.add(new TableEntry("Grand Total", "", "", currencyFormat.format(eodTotal)));

        // Return change amount
        return changeAmount;
    }

    public void resetClientRecords() {
        // Reset client side catalog, sale and table record
        productClientCatalog.clear();
        productSaleRecords.clear();

        // Reset total fields
        subTotal = new BigDecimal(0);
        subTotalTax = new BigDecimal(0);
    }

    public ObservableList<TableEntry> getTableEntryObservableList() {
        return FXCollections.observableList(tableList);
    }

    public BigDecimal getSubTotalTax() {
        return subTotalTax;
    }
}
