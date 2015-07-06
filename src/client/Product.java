/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package client;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.HashMap;

public class Product {

    private StringProperty name;
    private DoubleProperty price;
    private int stock;
    private ObjectProperty<LocalDate> expirationDate;
    private StringProperty supplier;
    private HashMap<String, Integer> notificationList; // <NomeDoCliente, quantidadeDesejada>

    public Product(String name, double price, int stock, LocalDate expirationDate, String supplier) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.stock = stock;
        this.expirationDate = new SimpleObjectProperty<>(expirationDate);
        this.supplier = new SimpleStringProperty(supplier);
        notificationList = new HashMap<>();
    }

    public final String getName() {
        return name.get();
    }

    public final double getPrice() {
        return price.get();
    }

    public final LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public ObjectProperty<LocalDate> expirationDateProperty() {
        return expirationDate;
    }

    public final String getSupplier() {
        return supplier.get();
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<LocalDate> expirationProperty() {
        return expirationDate;
    }

    public HashMap<String, Integer> getNotificationList() {
        return notificationList;
    }

    public StringProperty supplierProperty() {
        return supplier;
    }
}
