package hw6_peters_client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ClientRecords {

    ArrayList<ProductSpecification> productClientCatalog;

    ArrayList<SalesRecord> productSaleRecords;

    BigDecimal eodTotal,subTotal,subTotalTax;

    ClientRecords(){
        // Initialize client side catalog and sale record
        productClientCatalog = new ArrayList<>();
        productSaleRecords = new ArrayList<>();

        // Initialize total fields
        eodTotal = new BigDecimal(0);
        subTotal = new BigDecimal(0);
        subTotalTax = new BigDecimal(0);
    }

    public void addProductSale(String serverInput, int quantityInput){
        // Split serverInput into code, name, and price inputs
        String[] splitInput = serverInput.split(Pattern.quote("|"));
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
    }

    /**
     * Checks code input against productClientCatalog code fields. If true return, if not add new specification.
     *
     * @param codeInput String, code input to check for specification
     */
    public void addOrCreateSpecification(String codeInput, String nameInput, BigDecimal priceInput) {
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

//    /**
//     * Checks code input against productClientCatalog code fields. If found return true, if not found return false.
//     *
//     * @param codeInput String, code input to check for
//     * @return boolean, true = found / false = not found
//     */
//    public boolean checkIfSpecificationCreated(String codeInput) {
//        // Loop array list to check productClientCatalog for code
//        for (ProductSpecification specification : productClientCatalog) {
//            if (specification.getProductCode().equals(codeInput)) {
//                // Found
//                return true;
//            }
//        }
//
//        // Not found
//        return false;
//    }

    /**
     * Searches for a productSpecification object using a giving code. If found will return object else will return null
     *
     * @param codeInput String, item's code
     * @return ProductSpecification, if found return productSpecification else will return null
     */
    public ProductSpecification getProductSpecification(String codeInput) {
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
    public void addOrCreateSaleRecord(ProductSpecification specification, int productQuantity, BigDecimal productTotalPrice) {
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


}
