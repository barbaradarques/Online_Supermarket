/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;

public class Notifiable {
    String username;
    String productName;
    int quantity;

    public Notifiable(String productName, String username, int quantity) {
        this.username = username;
        this.productName = productName;
        this.quantity = quantity;
    }
}
