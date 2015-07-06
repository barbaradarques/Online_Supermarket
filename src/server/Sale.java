/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;

import java.time.LocalDate;

public class Sale {
    private String productName;
    private String username;
    private LocalDate date;
    private int quantity;

    public Sale(String productName, String username, LocalDate date, int quantity) {
        this.productName = productName;
        this.username = username;
        this.date = date;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }
}
